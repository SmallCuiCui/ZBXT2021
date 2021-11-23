package org.lxzx.boot.controller;

import org.lxzx.boot.dto.Result;
import org.lxzx.boot.enums.DepartmentEnum;
import org.lxzx.boot.enums.LimitedEnum;
import org.lxzx.boot.enums.PositionEnum;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/common")
public class CommonController {

    @RequestMapping(value = "/getDepartment", method = RequestMethod.GET)
    public Result getDepartMentList() {
        return Result.ok().message("请求成功").data(DepartmentEnum.getAllEnum());
    }

    @RequestMapping(value = "/getLimited", method = RequestMethod.GET)
    public Result getLimitedList() {
        return Result.ok().message("请求成功").data(LimitedEnum.getAllEnum());
    }

    @RequestMapping(value = "/getPosition", method = RequestMethod.GET)
    public Result getPositionList() {
        return Result.ok().message("请求成功").data(PositionEnum.getAllEnum());
    }
}
