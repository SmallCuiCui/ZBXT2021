package org.lxzx.boot.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.lxzx.boot.Utils.CommonUtil;
import org.lxzx.boot.bean.Dynamic;
import org.lxzx.boot.dto.PageResult;
import org.lxzx.boot.mapper.DynamicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DynamicService {

    @Autowired
    private DynamicMapper dynamicMapper;

    public PageResult<Dynamic> getDynamicByPage(int pageNum, int pageSize, String type) {
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

    public int addRecord(Dynamic dynamic) {
        dynamic.setDynamicId(CommonUtil.getUUId());
        int num = dynamicMapper.insertDynamic(dynamic);
        return num;
    }
}
