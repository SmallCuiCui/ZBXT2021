package org.lxzx.boot.controller;

import org.lxzx.boot.Utils.*;
import org.lxzx.boot.bean.User;
import org.lxzx.boot.bean.ZaiWeiRecord;
import org.lxzx.boot.dto.PageResult;
import org.lxzx.boot.dto.Result;
import org.lxzx.boot.enums.ResultCode;
import org.lxzx.boot.enums.StringEnum;
import org.lxzx.boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;


    @RequestMapping(value = "/userLogin", method = RequestMethod.POST)
    public Result userLogin(@RequestBody User user,
                            HttpServletRequest request) {
        if(user == null || user.getUserCode() == null || user.getPassword() == null) {
            return Result.error().message("参数异常");
        }
        if(user.getUserCode().equals(StringEnum.ChaoJiYongHu.getInfo())) {
            if(user.getPassword().equals(StringEnum.ChaoJiYongHuMiMa.getInfo())) {
                HttpSession session = request.getSession();
                session.setMaxInactiveInterval(TokenUtil.EXPIRE_TIME);
                user.setUserName(StringEnum.ChaoJiYongHuMingChen.getInfo());
                session.setAttribute("userInfo", user);
                return Result.ok().message("登录成功").data("token", TokenUtil.sign(user)).data("userInfo", user);
            } else {
                return Result.error(ResultCode.BAD_CREDENTIALS);
            }
        } else {
//            数据库查询
            User u = userService.checkUserLogin(user);
            if(u != null) {
                HttpSession session = request.getSession();
                session.setMaxInactiveInterval(TokenUtil.EXPIRE_TIME);
                session.setAttribute("userInfo", u);
                return Result.ok().message("登录成功").data("token", TokenUtil.sign(user)).data("userInfo", u);
            } else {
                return Result.error(ResultCode.BAD_CREDENTIALS);
            }
        }
    }

    @RequestMapping(value = "/loginOut", method = RequestMethod.GET)
    public Result loginOut(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("userInfo");
        if(sessionUser == null) {
            return  Result.error(ResultCode.AUTHORIZATION_PASS);
        }
        session.invalidate();

        return Result.ok().message("退出登录成功");
    }

    @RequestMapping("/addUser")
    public Result addUser(@RequestBody User user, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("userInfo");
        if(sessionUser == null) {
            return  Result.error(ResultCode.AUTHORIZATION_PASS);
        }
        if(user.getUserCode() == null || user.getUserName() == null || user.getPhoneNum() == null) {
            return Result.error(ResultCode.PARAMS_NOT_FULL);
        }
        int insertNum = userService.addUser(user, sessionUser);
        if( insertNum== 0) {
            return Result.error().message("该用户名已存在！");
        } else if(insertNum == 1) {
            return Result.ok().message("添加成功!初始密码为'123456',请尽快登录修改");
        } else {
            return Result.error(ResultCode.CREDENTIALS_EXPIRED);
        }
    }

    @RequestMapping(value = "/deleteUser" ,method = RequestMethod.GET)
    public Result deleteUser(@RequestParam(value = "userId") String userId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("userInfo");
        if(sessionUser == null) {
            return  Result.error(ResultCode.AUTHORIZATION_PASS);
        }
        if(userId.equals("")) {
            return Result.error().message("参数不能为空!");
        }
        int deleteNum = userService.deleteUserById(userId);
        if(deleteNum == 1) {
            return Result.ok().message("删除成功！");
        } else {
            return Result.error(ResultCode.CREDENTIALS_EXPIRED);
        }
    }

    @RequestMapping(value = "/editUserStatus", method = RequestMethod.POST)
    public Result editUserStatus(@RequestBody ZaiWeiRecord zaiWeiRecord, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("userInfo");
        if(sessionUser == null) {
            return  Result.error(ResultCode.AUTHORIZATION_PASS);
        }
//        校验数据不能为空
        if(zaiWeiRecord.getStartTime() == null || zaiWeiRecord.getEndTime() == null || zaiWeiRecord.getTargetUserCode() == null || zaiWeiRecord.getChangeStatus() == null) {
            return Result.error(ResultCode.PARAMS_NOT_FULL);
        }
        try{
            int insertNum = userService.editUserStatus(zaiWeiRecord, sessionUser);
            System.out.println(insertNum);
            if( insertNum== -1) {
                return Result.error().message("该时间段存在还未生效的记录，请核实后提交！");
            } else if(insertNum == -2) {
                return Result.error().message("数据异常!请联系管理员");
            } else if(insertNum == 1) {
                return Result.ok().message("提交成功!");
            } else {
                return Result.error(ResultCode.CREDENTIALS_EXPIRED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error(ResultCode.CREDENTIALS_EXPIRED);
    }



    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public Result changePassword(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("userInfo");
        if(sessionUser == null) {
            return  Result.error(ResultCode.AUTHORIZATION_PASS);
        }
        String userCode = params.get("userCode").toString();
        String orginPassword = params.get("orginPassword").toString();
        String newPassword = params.get("newPassword").toString();
        if(userCode == null || orginPassword == null || newPassword == null) {
            return Result.error().message("参数信息不全");
        }
        if(userCode.equals(StringEnum.ChaoJiYongHu)) {
            return Result.error().message("抱歉！您没有权限修改超级管理员账户密码");
        }
        if(userService.changePassword(userCode, orginPassword, newPassword) == 1) {
            return Result.ok().message("修改密码成功");
        } else if(userService.changePassword(userCode, orginPassword, newPassword) == 0) {
            return Result.error().message("原密码错误");
        }
        return Result.error(ResultCode.CREDENTIALS_EXPIRED);
    }

    @RequestMapping(value = "/findAllByPage", method = RequestMethod.GET)
    public Result findAllByPage(@RequestParam(defaultValue = "1", value = "pageNum") int pageNum,
                                @RequestParam(defaultValue = "10", value = "pageSize") int pageSize,
                                HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("userInfo");
        if(sessionUser == null) {
            return  Result.error(ResultCode.AUTHORIZATION_PASS);
        }
        PageResult<User> pageResult = new PageResult<>();
        if(sessionUser.getUserCode().equals(StringEnum.ChaoJiYongHu.getInfo())) {
            pageResult = userService.queryAllUser(pageNum, pageSize, null);
            return Result.ok().message("查询成功").data("datalist", pageResult);
        } else {
            String deptId = sessionUser.getDeptId();
            pageResult = userService.queryAllUser(pageNum, pageSize, deptId);
            return Result.ok().message("查询成功").data("datalist", pageResult);
        }
    }

//    查询所属部门的用户
    @RequestMapping(value = "/findUserByDeptId", method = RequestMethod.GET)
    public Result findUserByDeptId(@RequestParam("deptId") String deptId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("userInfo");
        if(sessionUser == null) {
            return  Result.error(ResultCode.AUTHORIZATION_PASS);
        }
        List<User> userList = userService.queryUserByDeptId(deptId);
        if(userList != null && userList.size() > 0) {
            return Result.ok().message("操作成功").data(userList);
        } else {
            return Result.ok().message("该单位下用户为空").data(null);
        }
    }

//    查询人员不在位情况、值班情况
    @RequestMapping(value = "/queryUserDetail", method = RequestMethod.GET)
    public Result queryUserDetail(@RequestParam(value = "userCode") String userCode, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("userInfo");
        if(sessionUser == null) {
            return  Result.error(ResultCode.AUTHORIZATION_PASS);
        }
        if(userCode.equals("")) {
            return Result.error().message("参数不能为空!");
        }

        Map<String, Object> map = userService.queryUserDetailById(userCode);
        if(map != null) {
            return Result.ok().data(map);
        }
        return Result.error(ResultCode.CREDENTIALS_EXPIRED);
    }
}
