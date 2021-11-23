package org.lxzx.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lxzx.boot.bean.Work;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//工作汇总返回给前端对象
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkColectBean {

    private Date startTime;
    private Date endTime;
    private String workTime;
    private String workType;
    private List<Work> workList;

    public void addWorkToList(Work work) {
        if(workList == null) {
            this.workList = new ArrayList<>();
        }
        this.workList.add(work);
    }
}
