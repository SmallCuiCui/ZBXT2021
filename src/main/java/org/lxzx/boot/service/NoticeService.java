package org.lxzx.boot.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.lxzx.boot.Utils.CommonUtil;
import org.lxzx.boot.bean.Notice;
import org.lxzx.boot.bean.User;
import org.lxzx.boot.dto.PageResult;
import org.lxzx.boot.enums.StringEnum;
import org.lxzx.boot.mapper.NoticeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    public int addAndEditNotice(Notice notice, User sessionUser) {
        int num = 0;
        // 立即发布
        if(notice.isPublish()) {
            notice.setPublishTime(new Date());
        }
        if(notice.getNoticeId() != null && notice.getNoticeId().length() > 0) {
            num = noticeMapper.handleEditNoticeById(notice);
        } else {
            notice.setNoticeId(CommonUtil.getUUId());
            notice.setCreateUserId(sessionUser.getUserId());
            notice.setCreateUserName(sessionUser.getUserName());
            notice.setCreateTime(new Date());
            //发布时间小于当前时间时--发布状态
            if(notice.getPublishTime() != null && notice.getPublishTime().getTime() <= notice.getCreateTime().getTime()) {
                notice.setPublish(true);
            }
            //创建人默认已读
            notice.setReadUserCodes(sessionUser.getUserCode());
            num = noticeMapper.insertNotice(notice);
        }
        return num;
    }

    public PageResult<Notice> queryAllUser(int pageNum, int pageSize) {
        PageResult<Notice> result = new PageResult<>();
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<Notice> noticeList = noticeMapper.getAll();
            PageInfo<Notice> pageInfo = new PageInfo<>(noticeList);
            result.setData(pageInfo);
            result.setResult(noticeList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            PageHelper.clearPage();
        }
        return result;
    }

    public int deleteById(String workId) {
        Notice checkNotice = noticeMapper.queryWorkById(workId);
        if( checkNotice == null) {
            return 0;
        }else if (checkNotice.isPublish()) {
            return -1;
        }
        return noticeMapper.deleteById(workId);
    }


    public int handlePublishNow(String workId) {
        Notice checkNotice = noticeMapper.queryWorkById(workId);
        if( checkNotice == null) {
            return 0;
        }else if (checkNotice.isPublish() == true) {
            return -1;
        }
        return noticeMapper.publishNowNoticeById(workId, new Date(), true);
    }

    public PageResult<Notice> handlequeryPublish(int pageNum, int pageSize, String userCode) {
        PageResult<Notice> result = new PageResult<>();
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<Notice> noticeList = noticeMapper.getAllPublish();
//           遍历判断该用户是否已读消息
            for (Notice notice: noticeList) {
                if(notice.getReadUserCodes() != null && notice.getReadUserCodes().indexOf(userCode) != -1) {
                    notice.setRead(true);
                }
            }
            PageInfo<Notice> pageInfo = new PageInfo<>(noticeList);
            result.setData(pageInfo);
            result.setResult(noticeList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            PageHelper.clearPage();
        }
        return result;
    }

    public int handleUserRead(String noticeId, String userCode, String readUserCodes) {
        String newStr = readUserCodes + "," + userCode;
        return noticeMapper.handleUserRead(noticeId, newStr);
    }

//    定时任务，查询预发布通知，将通知状态更改为发布--一分钟查询一次
    @Scheduled(cron = "0/60 * * * * *")
    public void updateNotice() {
        List<Notice> noticeList = noticeMapper.getAllNoPublish();
        for(int i = 0; i < noticeList.size(); i++) {
            if(noticeList.get(i).getPublishTime() != null && noticeList.get(i).getPublishTime().getTime() <= new Date().getTime()) {
                if(noticeMapper.modifyPublish(noticeList.get(i).getNoticeId()) == 1) {
                    Logger logger = LoggerFactory.getLogger(getClass());
                    logger.info("thread id:{},定时更新通知状态，noticeId:{}", Thread.currentThread().getId(), noticeList.get(i).getNoticeId());
                }
            }
        }
    }

    public List<Notice> handleQueryScreen() {
        List<Notice> noticeList = noticeMapper.getOneScreen();
        return  noticeList;
    }
}
