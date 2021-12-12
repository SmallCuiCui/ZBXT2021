package org.lxzx.boot.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.lxzx.boot.Utils.CommonUtil;
import org.lxzx.boot.bean.Notice;
import org.lxzx.boot.bean.Schedule;
import org.lxzx.boot.bean.User;
import org.lxzx.boot.dto.PageResult;
import org.lxzx.boot.enums.StringEnum;
import org.lxzx.boot.mapper.ScheduleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleMapper scheduleMapper;

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
    public int addAndEditSchedule(Schedule schedule) {
        int num = 1;
//       判断时间上是否存在已排班的安排
        List<Schedule> list1 = scheduleMapper.queryByStartTime(schedule.getStartTime());
        List<Schedule> list2 = scheduleMapper.queryByEndTime(schedule.getEndTime());
        if( list1 != null && list1.size() > 0 || list2 != null && list2.size() > 0) {
            return -1;
        }
        if(schedule.getScheduleId() != null && schedule.getScheduleId().length() > 0) {
            num = scheduleMapper.handleEditSchedule(schedule);
            return num;
        }
        schedule.setCreateTime(new Date());
        schedule.setScheduleId(CommonUtil.getUUId());
        schedule.setScheduleType(StringEnum.EIGHT.getInfo());
        schedule.setDayNum(CommonUtil.differentDays(schedule.getStartTime(), schedule.getEndTime()));
        num = scheduleMapper.insertSchedule(schedule);
        if(num > 0) {
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
                scheduleList.add(schedule1);
            }
            scheduleMapper.insertScheduleOneList(scheduleList);
            System.out.println(scheduleList);
        }

        return num;
    }

    @Transactional
    public int EditZhiBan(List<Schedule> scheduleList) {
        int num = 0;
        for (int i = 0; i < scheduleList.size(); i++) {
            try{
                int v = scheduleMapper.handleEditZhiQin(scheduleList.get(i));
                if( v > 0) {
                    num ++;
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return num;
    }

    public int deleteById(String scheduleId) {
        Schedule checkSchedule = scheduleMapper.queryWorkById(scheduleId);
        if( checkSchedule == null) {
            return 0;
        }else if (checkSchedule.getStartTime().getTime() <= new Date().getTime()) {
            return -1;
        }
        return scheduleMapper.deleteById(scheduleId);
    }
}
