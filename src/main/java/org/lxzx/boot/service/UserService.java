package org.lxzx.boot.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.lxzx.boot.Utils.CommonUtil;
import org.lxzx.boot.Utils.MD5Util;
import org.lxzx.boot.bean.ZaiWeiRecord;
import org.lxzx.boot.dto.DashboardData;
import org.lxzx.boot.dto.PageResult;
import org.lxzx.boot.bean.User;
import org.lxzx.boot.enums.*;
import org.lxzx.boot.mapper.UserMapper;
import org.lxzx.boot.mapper.ZaiWeiRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ZaiWeiRecordMapper zaiWeiRecordMapper;

    @Autowired
    private DynamicService dynamicService;

    public User checkUserLogin(User user) {
        User u = userMapper.login(user.getUserCode(), MD5Util.getMD5(user.getPassword()));
        return u;
    }

    public int addUser(User user, User sessionUser) {
        if(userMapper.getOneByCode(user.getUserCode()) != null) {
            return 0;
        } else {
            user.setUserId(CommonUtil.getUUId());
            user.setStatus(StatusEnum.ZAIWEI.getValue());
            user.setLimitedName(LimitedEnum.stateOf(user.getLimitedId()).getName());
            user.setCreateTime(new Date());
            user.setCreateUserId(sessionUser.getUserId());
            user.setCreateUserCode(sessionUser.getUserCode());
            user.setCreateUserName(sessionUser.getUserName());
            user.setDelete(false);
            user.setPassword(MD5Util.getMD5(StringEnum.DEFAULTPASSWORD.getInfo()));
//            权限（领导、科室管理员、普通、值班员）
            if(user.getPositionId().equals(PositionEnum.ZHUREN.getValue()) || user.getPositionId().equals(PositionEnum.XIELI.getValue()) || user.getPositionId().equals(PositionEnum.FUZHUREN.getValue()) && user.getDeptId().equals(DepartmentEnum.DEPT1.getValue())) {
//                主任、协理、中心副主任
                user.setLimitedId(LimitedEnum.LEADER.getValue());
                user.setLimitedName(LimitedEnum.LEADER.getName());
            } else if(user.getLimitedId().equals(LimitedEnum.MANAGE.getValue())) {
                user.setLimitedName(LimitedEnum.MANAGE.getName());
            } else {
                user.setLimitedId(LimitedEnum.NORMAL.getValue());
                user.setLimitedName(LimitedEnum.NORMAL.getName());
            }
            int num = userMapper.insertUser(user);
            return num;
        }
    }

    public int changePassword(String userCode, String originPassword, String newPassword) {
        User user = userMapper.getOneByCode(userCode);
        if(user != null) {
            if(user.getPassword().equals(MD5Util.getMD5(originPassword))) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }

    public PageResult<User> queryAllUser(int pageNum, int pageSize, String deptId) {
        PageResult<User> result = new PageResult<>();
        try {
            if(deptId == null) {
                PageHelper.startPage(pageNum, pageSize);
                List<User> userList = userMapper.getAll();
                PageInfo<User> pageInfo = new PageInfo<>(userList);
                result.setData(pageInfo);
                result.setResult(userList);
            } else {
                PageHelper.startPage(pageNum, pageSize);
                List<User> userList = userMapper.getUsersByDept(deptId, false);
                PageInfo<User> pageInfo = new PageInfo<>(userList);
                result.setData(pageInfo);
                result.setResult(userList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            PageHelper.clearPage();
        }
        return result;
    }

    public List<User> queryUserByDeptId(String deptId) {
        if(deptId.equals("all")) {
            deptId = null;
        }
        List<User> userList = userMapper.getUsersByDept(deptId, false);
        return userList;
    }

    @Transactional
    public int editUserStatus(ZaiWeiRecord zaiWeiRecord, User sessionUser) throws Exception{
        int num = 0;
//      在位-->其他
        if(!zaiWeiRecord.getChangeStatus().equals(StatusEnum.ZAIWEI.getValue())) {
//                获取该时段 是否存在已提交的其他在位状态记录
            List<ZaiWeiRecord> zaiWeiRecords = zaiWeiRecordMapper.getByStartTime(zaiWeiRecord.getTargetUserCode(), new Date());
            boolean tag = true;
            if(zaiWeiRecords.size() > 0) {
                for(int i=0; i< zaiWeiRecords.size(); i++) {
                    if(CommonUtil.compareTwoTime(zaiWeiRecords.get(i).getStartTime(), zaiWeiRecords.get(i).getEndTime(), zaiWeiRecord.getStartTime(), zaiWeiRecord.getEndTime())) {
                        tag = false;
                        break;
                    }
                }
            }
            if(tag) {
                zaiWeiRecord.setZwRecordId(CommonUtil.getUUId());
                zaiWeiRecord.setCommitTime(new Date());
                zaiWeiRecord.setCommitUserCode(sessionUser.getUserCode());
                zaiWeiRecord.setCommitUserName(sessionUser.getUserName());
                zaiWeiRecord.setDayNum(CommonUtil.differentDays(zaiWeiRecord.getStartTime(), zaiWeiRecord.getEndTime()));
                if(zaiWeiRecord.getStartTime().getTime() <= new Date().getTime()) {
                    zaiWeiRecord.setIsExecute(true);
                } else {
                    zaiWeiRecord.setIsExecute(false);
                }
                num = zaiWeiRecordMapper.insertZaiWeiRecord(zaiWeiRecord);
                if(num == 1) {
//                    添加人员提交记录
                    if(dynamicService.addRecord(zaiWeiRecord, 2) != 1) {
                        throw new Exception();
                    }
//                  如果开始时间<=当前时间，更改用户状态、添加在位变动记录
                    if(zaiWeiRecord.getStartTime().getTime() <= new Date().getTime()) {
                        if(userMapper.editUserStatus(zaiWeiRecord.getChangeStatus(), zaiWeiRecord.getTargetUserCode()) != 1) {
                            throw new Exception();
                        }
                        if(dynamicService.addRecord(zaiWeiRecord, 1) != 1) {
                            throw new Exception();
                        }
                    }
                }
            } else {
                num = -1;
            }
        } else {
//          其他--->在位。手动销假情况，直接修改当前记录
            List<ZaiWeiRecord> zaiWeiRecords = zaiWeiRecordMapper.getByTime(zaiWeiRecord.getTargetUserCode(), new Date());
            if(zaiWeiRecords.size() == 1) {
                num = zaiWeiRecordMapper.editRecordTime(zaiWeiRecord.getEndTime(), CommonUtil.differentDays(zaiWeiRecords.get(0).getStartTime(), zaiWeiRecord.getEndTime()), zaiWeiRecords.get(0).getZwRecordId());
                if(num == 1) {
                    ZaiWeiRecord zaiWeiRecord1 = new ZaiWeiRecord();
                    zaiWeiRecord1.clone(zaiWeiRecords.get(0));
                    int v = dynamicService.addRecord(zaiWeiRecord1, 2);
                    if(v != 1) {
                        throw new Exception();
                    }
                }
            } else {
                num = -2;//数据异常。不存在记录或者当前时间存在多条记录
            }
        }
        return num;
    }

//  获取各室人员情况、统计人员在位情况
    public DashboardData getZaiWeiConditions() {
        DashboardData dashboardData = userMapper.getZaiWeiCondition();
//        查询各室的人员情况（不包括领导）
        List<DepartmentEnum> enumList = DepartmentEnum.getAllEnum();
        for(int i = 0; i < enumList.size(); i++) {
            String deptId = enumList.get(i).getValue();
            List<User> userList = userMapper.getUsersByDept(deptId, true);
            Map map = new HashMap();
            map.put("deptId", deptId);
            map.put("deptName", enumList.get(i).getName());
            map.put("userList", userList);
            dashboardData.addZaiWei((HashMap) map, false);
        }
//        查询所有领导在位情况
        List<User> leaderList = userMapper.getLeaderZaiWei();
        Map map = new HashMap();
        map.put("deptId", LimitedEnum.LEADER.getValue());
        map.put("deptName", LimitedEnum.LEADER.getName());
        map.put("userList", leaderList);
        dashboardData.addZaiWei((HashMap) map, true);
        return dashboardData;
    }

}
