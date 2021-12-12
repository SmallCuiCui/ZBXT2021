package org.lxzx.boot.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

//在位记录表
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ZaiWeiRecord {
    private String zwRecordId;
    private String targetUserCode;
    private String targetUserName;
    private Date startTime;
    private Date endTime;
    private String originStatus;
    private String changeStatus;
    private Date commitTime;
    private String commitUserName;
    private String commitUserCode;
}
