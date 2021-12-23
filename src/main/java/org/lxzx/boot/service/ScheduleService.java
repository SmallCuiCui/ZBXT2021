package org.lxzx.boot.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.lxzx.boot.Utils.CommonUtil;
import org.lxzx.boot.Utils.Festival;
import org.lxzx.boot.bean.Schedule;
import org.lxzx.boot.bean.User;
import org.lxzx.boot.dto.PageResult;
import org.lxzx.boot.dto.StatisticData;
import org.lxzx.boot.enums.LimitedEnum;
import org.lxzx.boot.enums.StringEnum;
import org.lxzx.boot.mapper.ScheduleMapper;
import org.lxzx.boot.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private UserMapper userMapper;

    public PageResult<Schedule> queryallByPage(int pageNum, int pageSize, String type, String remark) {
        PageResult<Schedule> result = new PageResult<>();
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<Schedule> scheduleList = new ArrayList<>();
            if(type.equals(StringEnum.EIGHT.getInfo())) {
                scheduleList = scheduleMapper.getAll(type);
            } else {
                scheduleList = scheduleMapper.queryByRemark(type, remark);
            }
            PageInfo<Schedule> pageInfo = new PageInfo<>(scheduleList);
            result.setData(pageInfo);
            result.setResult(scheduleList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            PageHelper.clearPage();
        }
        return result;
    }

    @Transactional
    public int addAndEditSchedule(Schedule schedule) throws Exception {
        int num = 1;
//       判断时间上是否存在已排班的安排
        List<Schedule> list1 = scheduleMapper.queryByStartTime(schedule.getStartTime());
        List<Schedule> list2 = scheduleMapper.queryByEndTime(schedule.getEndTime());
        if( list1 != null && list1.size() > 0 || list2 != null && list2.size() > 0) {
            return -1;
        }
//        判断时间是否为节假日---一般来说节假日和非节假日是不一样的安排
        Festival festival = new Festival();
        Map map = festival.getFestival(schedule.getStartTime());
        if((boolean)map.get("festival")) {
            schedule.setWeekday(false);
            schedule.setRemark((String) map.get("festivalName"));
        } else {
            schedule.setWeekday(true);
        }
        if(schedule.getScheduleId() != null && schedule.getScheduleId().length() > 0) {
//            编辑操作
            num = scheduleMapper.handleEditSchedule(schedule);
            return num;
        }
        schedule.setCreateTime(new Date());
        schedule.setScheduleId(CommonUtil.getUUId());
        schedule.setScheduleType(StringEnum.EIGHT.getInfo());
        schedule.setDayNum(CommonUtil.differentDays(schedule.getStartTime(), schedule.getEndTime()));
        num = scheduleMapper.insertSchedule(schedule);
        if(num > 0) {
            int v = this.addScheduleOne(schedule, festival);
            if(v != schedule.getDayNum()) {
                throw new Exception();
            }
        }
        return num;
    }

//    根据行政值班新增岗哨值班
    public int addScheduleOne(Schedule schedule, Festival festival){
        Date startTime = schedule.getStartTime();
        List<Schedule> scheduleList = new ArrayList<>();
        for(int i = 0; i < schedule.getDayNum(); i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startTime);
            int day1 = calendar.get(Calendar.DATE);
            calendar.set(Calendar.DATE, day1 + i);
            Date start = calendar.getTime();
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            Date end = calendar.getTime();
            Schedule schedule1 = new Schedule(CommonUtil.getUUId(), start, end, 1, StringEnum.ONE.getInfo(), schedule.getScheduleId());
//            判断是否为工作日
            if(festival.isWorkDay(schedule1.getStartTime())) {
                schedule1.setWeekday(true);
            } else {
                schedule1.setWeekday(false);
            }
            scheduleList.add(schedule1);
        }
        int v = scheduleMapper.insertScheduleOneList(scheduleList);
        return v;
    }

//    编辑行政值班、判断时间是否修改、如果修改则需要对应修改该时间下的岗哨值班

//    编辑岗哨值班
    @Transactional
    public int EditZhiBan(List<Schedule> scheduleList) throws Exception {
        int num = 0;
        for (int i = 0; i < scheduleList.size(); i++) {
            int v = scheduleMapper.handleEditZhiQin(scheduleList.get(i));
            if( v > 0) {
                num ++;
            } else {
                throw new Exception();
            }
        }
        if(num != scheduleList.size()) {
            throw new Exception();
        }
        return num;
    }

//    删除行政值班、同时删除该时间段下的岗哨值班
    @Transactional
    public int deleteById(String scheduleId) throws Exception {
        Schedule checkSchedule = scheduleMapper.queryWorkById(scheduleId);
        if( checkSchedule == null) {
            return 0;
        }else if (checkSchedule.getStartTime().getTime() <= new Date().getTime()) {
            return -1;
        }
        int num = scheduleMapper.deleteById(scheduleId);
        if(num == 1) {
            List<Schedule> schedulesOne = scheduleMapper.queryByRemark(StringEnum.ONE.getInfo(), scheduleId);
            int num1 = 0;
            for(int i = 0; i < schedulesOne.size(); i++) {
                int v = scheduleMapper.deleteById(schedulesOne.get(i).getScheduleId());
                if( v > 0) {
                    num1 ++;
                } else {
                    throw new Exception();
                }
            }
            if(num1 != schedulesOne.size()) {
                throw new Exception();
            }
        }
        return num;
    }

    public List<Schedule> getThisWeekZB() {
        return scheduleMapper.queryThisWeek(new Date());
    }

//    获取所有人的值班统计(不含领导)
    public List<StatisticData> getAllZhiBanStatistic() {

        List<User> userList = userMapper.getAll();
        List<StatisticData> list = new ArrayList<>();
        for (int i = 0; i < userList.size(); i++) {
            if(userList.get(i).getLimitedId().equals(LimitedEnum.LEADER.getValue())) {
                continue;
            }
            User u = userList.get(i);
            StatisticData s = scheduleMapper.getStatistics(u.getUserCode());
            s.setUserCode(u.getUserCode());
            s.setUserName(u.getUserName());
            s.setPositionId(u.getPositionId());
            s.setPositionName(u.getPositionName());
            if(s != null) {
                list.add(s);
            }
        }
        return list;
    }
}
