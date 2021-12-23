package org.lxzx.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.lxzx.boot.bean.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DashboardData {

    private int allUserNum;//所有人员
    private int zaiWeiNum; // 在位
    private int xueXiNum; // 学习
    private int chuChaiNum; // 出差
    private int qingJiaNum; // 请假
    private int lunXiuNum; // 轮休
    private int jieDiaoNum; // 借调
    private int zhuYuanNum; // 住院
    private int peiHuNum; // 陪护
    private int xiuJiaNum; // 休假

    private List<HashMap> zaiWeiList; //各室人员在位详细情况
//    {deptCode、deptName、userList[]}

    public void addZaiWei(HashMap map, boolean tag) {
        if(this.zaiWeiList == null) {
            this.zaiWeiList = new ArrayList<>();
        }
        if(tag) {
//            在数组前面插入数据
            this.zaiWeiList.add(0, map);
        } else {
            this.zaiWeiList.add(map);
        }
    }
}
