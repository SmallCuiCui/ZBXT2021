package org.lxzx.boot.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StatisticData {
    private String userCode;
    private String userName;
    private String positionId;//职务前端用于过滤
    private String positionName;
    private int allZhiBanNum;
    private int workNum;
    private int holidayNum;
}
