package org.lxzx.boot.controller;

import org.lxzx.boot.bean.Schedule;
import org.lxzx.boot.bean.User;
import org.lxzx.boot.dto.PageResult;
import org.lxzx.boot.dto.Result;
import org.lxzx.boot.enums.ResultCode;
import org.lxzx.boot.enums.StringEnum;
import org.lxzx.boot.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @RequestMapping(value = "/findAllByPage", method = RequestMethod.GET)
    public Result findAllByPage(@RequestParam(defaultValue = "1", value = "pageNum") int pageNum,
                                @RequestParam(defaultValue = "10", value = "pageSize") int pageSize,
                                @RequestParam(value = "type") String scheduleType,
                                @RequestParam(value = "remark", required = false, defaultValue = "hyccccc") String remark,
                                HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("userInfo");
        if(sessionUser == null) {
            return  Result.error(ResultCode.AUTHORIZATION_PASS);
        }
        PageResult<Schedule> pageResult = null;
        pageResult = scheduleService.queryallByPage(pageNum, pageSize, scheduleType, remark);
        return Result.ok().message("查询成功").data("datalist", pageResult);
    }

    @RequestMapping(value = "/addAndEditSchedule", method = RequestMethod.POST)
    public Result addAndEditSchedule(@RequestBody Schedule schedule, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("userInfo");
        if(sessionUser == null) {
            return  Result.error(ResultCode.AUTHORIZATION_PASS);
        }
        int insertNum = scheduleService.addAndEditSchedule(schedule);
        if(insertNum == 1) {
            return Result.ok().message("添加成功");
        } else if(insertNum == -1) {
            return Result.error().message("存在时间段有重合的排班记录，请核实后操作");
        } else {
            return Result.error(ResultCode.CREDENTIALS_EXPIRED);
        }
    }

    @RequestMapping(value = "/editZhiQin", method = RequestMethod.POST)
    public Result editZhiQin(@RequestBody List<Schedule> scheduleList, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("userInfo");
        if(sessionUser == null) {
            return  Result.error(ResultCode.AUTHORIZATION_PASS);
        }
        int insertNum = scheduleService.EditZhiBan(scheduleList);
        if(insertNum == scheduleList.size()) {
            return Result.ok().message("添加成功");
        } else {
            return Result.error(ResultCode.CREDENTIALS_EXPIRED);
        }
    }

    @RequestMapping(value = "/deletScheduleById", method = RequestMethod.GET)
    public Result deletScheduleById(@RequestParam(value = "scheduleId") String scheduleId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("userInfo");
        if(sessionUser == null) {
            return  Result.error(ResultCode.AUTHORIZATION_PASS);
        }
        int num = scheduleService.deleteById(scheduleId);
        if(num == 1) {
            return Result.ok().message("删除成功");
        } else if(num == -1) {
            return Result.error().message("此条数据已执行、无法删除");
        } else if (num == 0) {
            return Result.error().message("没有此条数据！请刷新界面再操作");
        }
        return Result.error(ResultCode.CREDENTIALS_EXPIRED);
    }
}
