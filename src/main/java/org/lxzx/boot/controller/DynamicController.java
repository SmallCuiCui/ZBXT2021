package org.lxzx.boot.controller;

import org.lxzx.boot.bean.Dynamic;
import org.lxzx.boot.bean.User;
import org.lxzx.boot.dto.PageResult;
import org.lxzx.boot.dto.Result;
import org.lxzx.boot.enums.ResultCode;
import org.lxzx.boot.service.DynamicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/dynamic")
public class DynamicController {

    @Autowired
    private DynamicService dynamicService;

    public Result findRecordByPage(@RequestParam(defaultValue = "1", value = "pageNum") int pageNum,
                                   @RequestParam(defaultValue = "10", value = "pageSize") int pageSize,
                                   @RequestParam(value = "recordType") String recordType,
                                   HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("userInfo");
        if(sessionUser == null) {
            return  Result.error(ResultCode.AUTHORIZATION_PASS);
        }
        PageResult<Dynamic> pageResult = dynamicService.getDynamicByPage(pageNum, pageSize, recordType);
        return Result.ok().message("查询成功").data("datalist", pageResult);
    }
}
