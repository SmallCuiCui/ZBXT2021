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
public class Schedule {
    private String scheduleId;
    private String zhuGuanId;
    private String zhuGuan;
    private String lingDaoId;
    private String lingDao;
    private String zhiBanYuanId;//实际存储的id均为userCode
    private String zhiBanYuan;
    private String beiBanYuanId;
    private String beiBanYuan;
    private String qinWu;
    private String jiaShiYuan;
    private String lianzhi1;
    private String lianzhi2;
    private String remark;
    private Date startTime;
    private Date endTime;
    private int dayNum;
    private boolean isWeekday;//是否是工作日
    private String scheduleType;
    private Date createTime;

//    过去的值班安排
    private boolean isPassed;

    public Schedule(String scheduleId, Date startTime, Date endTime, int dayNum, String scheduleType, String remark) {
        this.scheduleId = scheduleId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayNum = dayNum;
        this.scheduleType = scheduleType;
        this.remark = remark;
    }
}
