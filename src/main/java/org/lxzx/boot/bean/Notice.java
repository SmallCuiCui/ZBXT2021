package org.lxzx.boot.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Notice {
    private String noticeId;
    private String noticeTitle;
    private String noticeContent;
    private boolean isPublish;
    private boolean whetherScreen;
    private Date publishTime;
    private Date createTime;
    private String createUserId;
    private String createUserName;
    private String readUserCodes;

//    不存数据库，用户是否已读标记
    private boolean isRead;

}
