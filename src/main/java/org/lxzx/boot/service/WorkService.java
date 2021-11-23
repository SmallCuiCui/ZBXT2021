package org.lxzx.boot.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.lxzx.boot.Utils.CommonUtil;
import org.lxzx.boot.dto.PageResult;
import org.lxzx.boot.bean.Work;
import org.lxzx.boot.dto.WorkColectBean;
import org.lxzx.boot.enums.StringEnum;
import org.lxzx.boot.mapper.WorkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class WorkService {

    @Autowired
    private WorkMapper workMapper;

    public int handleAddAndEdit(Work work) {
        int num = 0;
        if(work.getWorkId().length() > 0) { // 编辑
            System.out.println(work.getWorkId());
            num = workMapper.updateWorkById(work);
        } else { // 新增
//            查询此单位下当前填报日工作、周工作是否已经存在填报数据
            if(!work.getWorkType().equals("year") && workMapper.queryByTimeAndDept(work.getStartTime(), work.getEndTime(), work.getDeptId()) != null) {
                num = -1;
            } else {
                work.setWorkId(CommonUtil.getUUId());
                work.setCreateTime(new Date());
                if(work.getStatus().equals(StringEnum.PUBLISH.getInfo())) {
                    work.setCommitTime(new Date());
                }
                num = workMapper.insertWork(work);
            }
        }
        return num;
    }

    public PageResult<Work> queryAllWork(int pageNum, int pageSize, String deptId) {
        PageResult<Work> result = new PageResult<>();
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<Work> workList = null;
            if(deptId != null) {
                workList = workMapper.getAllByDeptId(deptId);
            } else {
                workList = workMapper.getAll();
            }
            PageInfo<Work> pageInfo = new PageInfo<>(workList);
            result.setData(pageInfo);
            result.setResult(workList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            PageHelper.clearPage();
        }
        return result;
    }

    public int deleteById(String workId) {
        Work checkWork = workMapper.queryWorkById(workId);
        if( checkWork == null) {
            return 0;
        }else if (checkWork.getStatus().equals(StringEnum.PUBLISH.getInfo())) {
            return -1;
        }
        return workMapper.deleteById(workId);
    }

    public int handlePublish(String workId) {
        Work checkWork = workMapper.queryWorkById(workId);
        if( checkWork == null) {
            return 0;
        }else if (checkWork.getStatus().equals(StringEnum.PUBLISH.getInfo())) {
            return -1;
        }
        return workMapper.publicWorkById(StringEnum.PUBLISH.getInfo(), workId, new Date());
    }

//    查询所有单位汇总
    public List<WorkColectBean> handleWorkColect(Work workParam) {
        List<Work> workList = workMapper.getWorkGroupByTime(workParam);
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
                workColectBean.setWorkType(workCurrent.getWorkType());
                tag = true;
            }
            workColectBean.addWorkToList(workList.get(i));
            if(i!=(workList.size()-1) && !workCurrent.getWorkTime().equals(workList.get(i + 1).getWorkTime())) {
                tag = false;
                resultList.add(workColectBean);
            } else if(i == workList.size() - 1) {
                resultList.add(workColectBean);
            }
            i++;
        }
        return resultList;
    }

//    查询指定单位汇总
    public List<Work> handleWorkColect1(Work workParam) {
        List<Work> workList = workMapper.getWorkGroupByTime(workParam);
        return workList;
    }

}
