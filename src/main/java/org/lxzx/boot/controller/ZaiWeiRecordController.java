package org.lxzx.boot.controller;

import org.lxzx.boot.bean.User;
import org.lxzx.boot.bean.ZaiWeiRecord;
import org.lxzx.boot.dto.PageResult;
import org.lxzx.boot.dto.Result;
import org.lxzx.boot.enums.ResultCode;
import org.lxzx.boot.service.ZaiWeiRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/zaiWeiRecord")
public class ZaiWeiRecordController {

    @Autowired
    private ZaiWeiRecordService zaiWeiRecordService;

    @RequestMapping(value = "/findAllByPage", method = RequestMethod.GET)
    public Result findAllByPage(@RequestParam(defaultValue = "1", value = "pageNum") int pageNum,
                                @RequestParam(defaultValue = "10", value = "pageSize") int pageSize,
                                @RequestParam( value = "userCode") String userCode,
                                HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("userInfo");
        if(sessionUser == null) {
            return  Result.error(ResultCode.AUTHORIZATION_PASS);
        }
        PageResult<ZaiWeiRecord> pageResult = new PageResult<>();
        pageResult = zaiWeiRecordService.queryRecordByUserCode(pageNum, pageSize, userCode);
        return Result.ok().message("查询成功").data("datalist", pageResult);
    }

    @RequestMapping(value = "/deletRecordById", method = RequestMethod.GET)
    public Result deleteByRecordId(@RequestParam(value = "zwRecordId") String zwRecordId,
                                   HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("userInfo");
        if(sessionUser == null) {
            return Result.error(ResultCode.AUTHORIZATION_PASS);
        }
        int num = zaiWeiRecordService.deleteById(zwRecordId);
        if(num == 1) {
            return Result.ok().message("操作成功");
        } else {
           return Result.error(ResultCode.CREDENTIALS_EXPIRED);
        }
    }
}
