package org.lxzx.boot.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.lxzx.boot.Utils.CommonUtil;
import org.lxzx.boot.Utils.MD5Util;
import org.lxzx.boot.bean.MaiorEvent;
import org.lxzx.boot.bean.Notice;
import org.lxzx.boot.bean.User;
import org.lxzx.boot.dto.PageResult;
import org.lxzx.boot.enums.*;
import org.lxzx.boot.mapper.MaiorEventMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MaiorEventService {

    @Autowired
    private MaiorEventMapper maiorEventMapper;

    public int addAndEditMaiorEvent(MaiorEvent event) {
        if(event.getTitle() == null || event.getContent() == null || event.getEventDate() == null) {
            return 0;
        } else {
            int num = 0;
            if(event.getEventId() != null && event.getEventId().length() > 0) {
//                编辑大事记
                num = maiorEventMapper.handleEditEventById(event);
            } else {
                event.setEventId(CommonUtil.getUUId());
                num = maiorEventMapper.insertEvent(event);
            }
            return num;
        }
    }

    public PageResult<MaiorEvent> queryAllEvent(int pageNum, int pageSize, Date startT, Date endT, String searchValue) {
        PageResult<MaiorEvent> result = new PageResult<>();
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<MaiorEvent> eventList = maiorEventMapper.getAll(startT, endT, searchValue);
            PageInfo<MaiorEvent> pageInfo = new PageInfo<>(eventList);
            result.setData(pageInfo);
            result.setResult(eventList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            PageHelper.clearPage();
        }
        return result;
    }

    public int deleteByEventId(String eventId) {
        MaiorEvent event = maiorEventMapper.getEventById(eventId);
        if(event == null) {
            return 0;//没有此数据
        } else if(event.isLock()) {
            return -1; // 数据被锁定，无法修改
        }
        return maiorEventMapper.deleteById(eventId);
    }

    public int lockByEventId(String eventId) {
        MaiorEvent event = maiorEventMapper.getEventById(eventId);
        if(event == null) {
            return 0;//没有此数据
        } else if(event.isLock()) {
            return -1; // 数据被锁定，无法修改
        }
        return maiorEventMapper.lockEventById(eventId);
    }
}
