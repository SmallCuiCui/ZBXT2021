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
    private int dayNum;
    private Boolean isExecute;

    public void clone(ZaiWeiRecord v) {
        this.targetUserCode = v.targetUserCode;
        this.targetUserName = v.targetUserName;
        this.startTime = v.startTime;
        this.endTime = v.endTime;
        this.originStatus = v.originStatus;
        this.changeStatus = v.changeStatus;
        this.commitTime = v.commitTime;
        this.commitUserName = v.commitUserName;
        this.commitUserCode = v.commitUserCode;
        this.dayNum = v.dayNum;
    }
}
