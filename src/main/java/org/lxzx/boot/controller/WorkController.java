package org.lxzx.boot.controller;

import org.lxzx.boot.dto.PageResult;
import org.lxzx.boot.dto.Result;
import org.lxzx.boot.bean.User;
import org.lxzx.boot.bean.Work;
import org.lxzx.boot.dto.WorkColectBean;
import org.lxzx.boot.enums.ResultCode;
import org.lxzx.boot.enums.StringEnum;
import org.lxzx.boot.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/work")
public class WorkController {
    @Autowired
    private WorkService workService;

    @RequestMapping("/addAndEditWork")
    public Result addAndEditWork(@RequestBody Work work, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("userInfo");
        if(sessionUser == null) {
            return  Result.error(ResultCode.AUTHORIZATION_PASS);
        }
        int num = workService.handleAddAndEdit(work);
        if( num == 1) {
            return Result.ok().message(work.getStatus().equals(StringEnum.DRAFT.getInfo()) ? "保存成功" : "提交成功");
        } else if(num == -1) {
            return Result.error().message(work.getWorkTime() +" 工作记录已存在!请勿重复提交");
        }

        return Result.error(ResultCode.CREDENTIALS_EXPIRED);
    }

    @RequestMapping(value = "/findAllWorkByPage", method = RequestMethod.GET)
    public Result findAllWorkByPage(@RequestParam(defaultValue = "1", value = "pageNum") int pageNum,
                                @RequestParam(defaultValue = "10", value = "pageSize") int pageSize,
                                HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("userInfo");
        if(sessionUser == null) {
            return  Result.error(ResultCode.AUTHORIZATION_PASS);
        }
        PageResult<Work> pageResult = new PageResult<>();
        if(sessionUser.getUserCode().equals(StringEnum.ChaoJiYongHu.getInfo())) {
//            超级管理员查询所有
            pageResult = workService.queryAllWork(pageNum, pageSize, null);
            return Result.ok().message("查询成功").data("datalist", pageResult);
        } else {
//            个人查询本单位下工作填报
            String deptId = sessionUser.getDeptId();
            pageResult = workService.queryAllWork(pageNum, pageSize, sessionUser.getDeptId());
            return Result.ok().message("查询成功").data("datalist", pageResult);
        }
    }

    @RequestMapping(value = "/deletWorkById", method = RequestMethod.GET)
    public Result deletWorkById(@RequestParam(value = "workId") String workId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("userInfo");
        if(sessionUser == null) {
            return  Result.error(ResultCode.AUTHORIZATION_PASS);
        }
        int num= workService.deleteById(workId);
        if(num == 1) {
            return Result.ok().message("删除成功");
        } else if(num == -1) {
            return Result.error().message("此条数据已提交、无法删除");
        } else if (num == 0) {
            return Result.error().message("没有此条数据！请刷新界面再操作");
        }
        return Result.error(ResultCode.CREDENTIALS_EXPIRED);
    }

    @RequestMapping(value = "/handleWorkPublish", method = RequestMethod.GET)
    public Result handleWorkPublish(@RequestParam(value = "workId") String workId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("userInfo");
        if(sessionUser == null) {
            return  Result.error(ResultCode.AUTHORIZATION_PASS);
        }
        int num= workService.handlePublish(workId);
        if( num == 1) {
            return Result.ok().message("提交成功");
        } else if(num == -1) {
            return Result.error().message("此条数据已提交");
        } else if (num == 0) {
            return Result.error().message("没有此条数据！请刷新界面再操作");
        }
        return Result.error(ResultCode.CREDENTIALS_EXPIRED);
    }

    @RequestMapping(value = "/getWorkColectList", method = RequestMethod.POST)
    public Result getWorkColectList(@RequestBody Work workParam, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("userInfo");
        if(sessionUser == null) {
            return  Result.error(ResultCode.AUTHORIZATION_PASS);
        }
        if(workParam.getDeptId() == null) {
            List<WorkColectBean> list = workService.handleWorkColect(workParam);
            return Result.ok().data(list);
        } else {
            List<Work> list = workService.handleWorkColect1(workParam);
            return Result.ok().data(list);
        }


    }
}
