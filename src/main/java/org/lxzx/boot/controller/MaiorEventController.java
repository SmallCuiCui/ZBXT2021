package org.lxzx.boot.controller;

import org.lxzx.boot.Utils.Festival;
import org.lxzx.boot.bean.MaiorEvent;
import org.lxzx.boot.bean.Notice;
import org.lxzx.boot.bean.User;
import org.lxzx.boot.dto.PageResult;
import org.lxzx.boot.dto.Result;
import org.lxzx.boot.enums.ResultCode;
import org.lxzx.boot.service.MaiorEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@RestController
@RequestMapping("/maiorEvent")
public class MaiorEventController {

    @Autowired
    private MaiorEventService maiorEventService;

    @PostMapping("addAndEditEvent")
    public Result addAndEditEvent(@RequestBody MaiorEvent event, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("userInfo");
        if(sessionUser == null) {
            return  Result.error(ResultCode.AUTHORIZATION_PASS);
        }
        int num = maiorEventService.addAndEditMaiorEvent(event);
        if(num == 0) {
            return Result.error().message("参数不全");
        } else if(num == 1){
            return Result.ok().message("添加成功");
        }
        return Result.error(ResultCode.CREDENTIALS_EXPIRED);
    }

    @RequestMapping(value = "/findEventByPage", method = RequestMethod.GET)
    public Result findEventByPage(@RequestParam(defaultValue = "1", value = "pageNum") int pageNum,
                                  @RequestParam(defaultValue = "10", required = false, value = "pageSize") int pageSize,
                                  @RequestParam(required = false, value = "startT") String startT,
                                  @RequestParam(required = false, value = "endT") String endT,
                                  @RequestParam(required = false, value = "searchValue") String searchValue,
                                HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("userInfo");
        if(sessionUser == null) {
            return  Result.error(ResultCode.AUTHORIZATION_PASS);
        }
        PageResult<MaiorEvent> pageResult = null;
        Date startDate = startT != null ? Festival.getDate(startT) : null;
        Date endDate = endT != null ? Festival.getDate(endT) : null;
        pageResult = maiorEventService.queryAllEvent(pageNum, pageSize, startDate, endDate, searchValue);
        return Result.ok().message("查询成功").data("datalist", pageResult);
    }

    @GetMapping("deleteEventById")
    public Result deleteEventById(@RequestParam(value = "eventId") String eventId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("userInfo");
        if(sessionUser == null) {
            return  Result.error(ResultCode.AUTHORIZATION_PASS);
        }
        int num = maiorEventService.deleteByEventId(eventId);
        if(num == 0) {
            return Result.error().message("没有查询到此数据，请刷新界面后再尝试！");
        } else if(num == 1){
            return Result.ok().message("添加成功");
        } else if(num == -1) {
            return Result.error().message("此数据已锁定，不可删除！");
        }
        return Result.error(ResultCode.CREDENTIALS_EXPIRED);
    }

    @GetMapping("lockEventById")
    public Result lockEventById(@RequestParam(value = "eventId") String eventId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("userInfo");
        if(sessionUser == null) {
            return  Result.error(ResultCode.AUTHORIZATION_PASS);
        }
        int num = maiorEventService.lockByEventId(eventId);
        if(num == 0) {
            return Result.error().message("没有查询到此数据，请刷新界面后再尝试！");
        } else if(num == 1){
            return Result.ok().message("锁定成功，锁定后的数据不能再进行修改！");
        } else if(num == -1) {
            return Result.error().message("此数据已锁定，不可重复操作！");
        }
        return Result.error(ResultCode.CREDENTIALS_EXPIRED);
    }
}
