package org.lxzx.boot.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Work {

    private String workId; // id
    private String deptId; // 部门id
    private String deptName; // 部门name
    private String fillUserCodes; // 填写人id
    private String fillUserNames; // 填写人姓名
    private Date startTime; // 工作开始时间
    private Date endTime; // 工作结束时间
    private String workTime; // 时间字符串、根据工作类型及时间确定
    private String workContent; // 工作内容、本周工作内容
    private String workContentPlan; // 下周工作计划
    private String workType; // 工作类型  日工作day、周工作week、年度工作year
    private String status; // 状态，提交、草稿
    private String relatedUsers; // 相关人员
    private Date commitTime; // 提交时间
    private Date createTime; // 提交时间
}
