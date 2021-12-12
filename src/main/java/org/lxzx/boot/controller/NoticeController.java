package org.lxzx.boot.controller;

import org.lxzx.boot.bean.Notice;
import org.lxzx.boot.bean.User;
import org.lxzx.boot.dto.PageResult;
import org.lxzx.boot.dto.Result;
import org.lxzx.boot.enums.ResultCode;
import org.lxzx.boot.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @RequestMapping("/addAndEditNotice")
    public Result addAndEditNotice(@RequestBody Notice notice, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("userInfo");
        if(sessionUser == null) {
            return  Result.error(ResultCode.AUTHORIZATION_PASS);
        }
        if(notice.getNoticeTitle() == null || notice.getNoticeContent() == null) {
            return Result.error(ResultCode.PARAMS_NOT_FULL);
        }
        int insertNum = noticeService.addAndEditNotice(notice, sessionUser);
        if(insertNum == 1) {
            return Result.ok().message(notice.isPublish() ? "保存成功" : "发布成功");
        } else {
            return Result.error(ResultCode.CREDENTIALS_EXPIRED);
        }
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
        PageResult<Notice> pageResult = null;
        pageResult = noticeService.queryAllUser(pageNum, pageSize);
        return Result.ok().message("查询成功").data("datalist", pageResult);
    }

    @RequestMapping(value = "/deletNoticeById", method = RequestMethod.GET)
    public Result deletNoticeById(@RequestParam(value = "noticeId") String noticeId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("userInfo");
        if(sessionUser == null) {
            return  Result.error(ResultCode.AUTHORIZATION_PASS);
        }
        int num= noticeService.deleteById(noticeId);
        if(num == 1) {
            return Result.ok().message("删除成功");
        } else if(num == -1) {
            return Result.error().message("此条数据已发布、无法删除");
        } else if (num == 0) {
            return Result.error().message("没有此条数据！请刷新界面再操作");
        }
        return Result.error(ResultCode.CREDENTIALS_EXPIRED);
    }

    @RequestMapping(value = "/handlePublishNow", method = RequestMethod.GET)
    public Result handlePublishNow(@RequestParam(value = "workId") String workId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("userInfo");
        if(sessionUser == null) {
            return  Result.error(ResultCode.AUTHORIZATION_PASS);
        }
        int num= noticeService.handlePublishNow(workId);
        if( num == 1) {
            return Result.ok().message("发布成功");
        } else if(num == -1) {
            return Result.error().message("此条数据已发布");
        } else if (num == 0) {
            return Result.error().message("没有此条数据！请刷新界面再操作");
        }
        return Result.error(ResultCode.CREDENTIALS_EXPIRED);
    }

    @RequestMapping(value = "/queryAllPublishNotice", method = RequestMethod.GET)
    public Result queryAllPublishNotice(@RequestParam(defaultValue = "1", value = "pageNum") int pageNum,
                                        @RequestParam(defaultValue = "10", value = "pageSize") int pageSize,
                                        HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("userInfo");
        if(sessionUser == null) {
            return  Result.error(ResultCode.HANDLE_FAIL);
        }
        PageResult<Notice> pageResult = null;
        pageResult = noticeService.handlequeryPublish(pageNum, pageSize, sessionUser.getUserCode());
        return Result.ok().message("查询成功").data("datalist", pageResult);
    }

    @RequestMapping(value = "/handleUserRead", method = RequestMethod.POST)
    public Result handleRead(@RequestBody Map<String, String> mapParam, HttpServletRequest request) {
        String noticeId = mapParam.get("noticeId");
        String userCode = mapParam.get("userCode");
        String readUserCodes = mapParam.get("readUserCodes");
        if(noticeId == null || userCode == null || readUserCodes == null) {
            return Result.error(ResultCode.HANDLE_FAIL);
        }
        int num = noticeService.handleUserRead(noticeId, userCode, readUserCodes);
        if(num == 1) {
            return Result.ok().message("标记已读成功");
        } else {
            return Result.error(ResultCode.HANDLE_FAIL);
        }
    }

    @RequestMapping(value = "/queryScreenNotice", method = RequestMethod.GET)
    public Result queryScreenNotice(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("userInfo");
        if(sessionUser == null) {
            return  Result.error(ResultCode.AUTHORIZATION_PASS);
        }
        List<Notice> dataList = noticeService.handleQueryScreen();
        return Result.ok().message("查询成功").data("datalist", dataList);
    }

}
