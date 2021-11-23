package funTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lxzx.boot.MainAplication;
import org.lxzx.boot.dto.Result;
import org.lxzx.boot.bean.Work;
import org.lxzx.boot.dto.WorkColectBean;
import org.lxzx.boot.enums.LimitedEnum;
import org.lxzx.boot.mapper.WorkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {MainAplication.class})
public class Mytest {


    @Autowired
    private WorkMapper workMapper;

    @Test
    public void testEnum() {
        String limitedId = "NORMAL";
        System.out.println(LimitedEnum.stateOf(limitedId).getName());;
    }

    @Test
    public void testResult() {
        Result r = Result.ok().data("data", "123123123").data("data2", "huangyongcui");
        System.out.println(r);
    }

//    测试分组查询，获取工作汇总

    @Test
    public void testHandleWork() {
        List<Work> workList = workMapper.getWorkGroupByTime(new Work());
        List<WorkColectBean> resultList = new ArrayList<>();
        boolean tag = false;//当前汇总对象构造中
        WorkColectBean workColectBean = null;
        Work workCurrent = null;
        int i = 0;
        while (i <= workList.size() - 1) {
            workCurrent =  workList.get(i);
            if(!tag) {
                workColectBean = new WorkColectBean();
                workColectBean.setStartTime(workCurrent.getStartTime());
                workColectBean.setEndTime(workCurrent.getEndTime());
                workColectBean.setWorkTime(workCurrent.getWorkTime());
                tag = true;
            }
            workColectBean.addWorkToList(workList.get(i));
            if(i!=(workList.size()-1) && !workCurrent.getWorkTime().equals(workList.get(i + 1).getWorkTime())) {
                tag = false;
                resultList.add(workColectBean);
            }
            i++;
        }
        System.out.println(resultList);
    }
}
