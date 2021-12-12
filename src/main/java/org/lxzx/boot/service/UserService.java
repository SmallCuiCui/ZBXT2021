package org.lxzx.boot.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.lxzx.boot.Utils.CommonUtil;
import org.lxzx.boot.Utils.MD5Util;
import org.lxzx.boot.bean.ZaiWeiRecord;
import org.lxzx.boot.dto.PageResult;
import org.lxzx.boot.bean.User;
import org.lxzx.boot.enums.*;
import org.lxzx.boot.mapper.UserMapper;
import org.lxzx.boot.mapper.ZaiWeiRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ZaiWeiRecordMapper zaiWeiRecordMapper;

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

    public PageResult<User> queryAllUser(int pageNum, int pageSize) {
        PageResult<User> result = new PageResult<>();
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<User> userList = userMapper.getAll();
            PageInfo<User> pageInfo = new PageInfo<>(userList);
            result.setData(pageInfo);
            result.setResult(userList);
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
        List<User> userList = userMapper.getUsersByDept(deptId);
        return userList;
    }
    @Transactional
    public int editUserStatus(ZaiWeiRecord zaiWeiRecord, User sessionUser) {
        int num = 0;
        try {
            num = userMapper.editUserStatus(zaiWeiRecord.getChangeStatus(), zaiWeiRecord.getTargetUserCode());
            if(num == 1) {
                zaiWeiRecord.setZwRecordId(CommonUtil.getUUId());
                zaiWeiRecord.setCommitTime(new Date());
                zaiWeiRecord.setCommitUserCode(sessionUser.getUserCode());
                zaiWeiRecord.setCommitUserName(sessionUser.getUserName());
                int v = zaiWeiRecordMapper.insertZaiWeiRecord(zaiWeiRecord);
                if(v != 1) {
                    throw new Exception();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return num;
    }


}
