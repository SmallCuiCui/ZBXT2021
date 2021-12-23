package org.lxzx.boot.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.lxzx.boot.Utils.CommonUtil;
import org.lxzx.boot.bean.Dynamic;
import org.lxzx.boot.bean.ZaiWeiRecord;
import org.lxzx.boot.dto.PageResult;
import org.lxzx.boot.enums.StatusEnum;
import org.lxzx.boot.mapper.DynamicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class DynamicService {

    @Autowired
    private DynamicMapper dynamicMapper;

    public PageResult<Dynamic> getDynamicByPage(int pageNum, int pageSize, int type) {
        PageResult<Dynamic> result = new PageResult<>();
        try{
            PageHelper.startPage(pageNum, pageSize);
            List<Dynamic> dataList = dynamicMapper.getAll(type);
            PageInfo<Dynamic> pageInfo = new PageInfo<>(dataList);
            result.setData(pageInfo);
            result.setResult(dataList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            PageHelper.clearPage();
        }
        return result;
    }

    public int getDynamicCount(int recordType, int timeType) {
        int num = 0;
        if(timeType == 1) {
//            查询今日新增记录
            num = dynamicMapper.getDynamicCount(recordType, CommonUtil.getZeroTime(), CommonUtil.getTodayEndTime());
        } else {
//            查询本周新增记录
            num = dynamicMapper.getDynamicCount(recordType, CommonUtil.getBeginDayOfWeek(), CommonUtil.getEndDayOfWeek());
        }
        return num;
    }

    public int addRecord(ZaiWeiRecord zaiWeiRecord, int recordType) {
        Dynamic dynamic = new Dynamic(zaiWeiRecord.getOriginStatus(), zaiWeiRecord.getChangeStatus(), zaiWeiRecord.getTargetUserName(), zaiWeiRecord.getCommitUserName(), recordType);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startTime = null, endTime = null;
        if(zaiWeiRecord.getStartTime() != null) {
            startTime = sdf.format(zaiWeiRecord.getStartTime());
        }
        if(zaiWeiRecord.getEndTime() != null) {
            endTime = sdf.format(zaiWeiRecord.getEndTime());
        }
//        手动操作/系统根据时间自动生成
        dynamic.setDiscription(zaiWeiRecord.getCommitUserName() + " 变更 " + zaiWeiRecord.getTargetUserName() +" 在位状态为'"+ StatusEnum.getNameByValue(zaiWeiRecord.getChangeStatus())+"'，时间" + startTime + "至" + endTime + "，共四天");
        dynamic.setDynamicId(CommonUtil.getUUId());
        if(recordType == 1) {
            dynamic.setChangeTime(zaiWeiRecord.getStartTime());
        }
        dynamic.setCreateTime(new Date());
        int num = dynamicMapper.insertDynamic(dynamic);
        return num;
    }
}
