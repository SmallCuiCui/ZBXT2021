package org.lxzx.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DetailData {
    private int qingJiaNum;
    private int zhiBanNum;//总的值班天数
    private int workZbDay; // 工作日值班
    private int festivalZbDay; // 节假日值班
    private int xiuJiaNum;
    private int chuChaiNum;
    private int zaiWeiNum;
    private int lunXiuNum;
    private int jiaQiRemain;
}
