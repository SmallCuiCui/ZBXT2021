package org.lxzx.boot.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.lxzx.boot.Utils.CommonUtil;
import org.lxzx.boot.bean.Dynamic;
import org.lxzx.boot.bean.User;
import org.lxzx.boot.bean.ZaiWeiRecord;
import org.lxzx.boot.dto.PageResult;
import org.lxzx.boot.mapper.DynamicMapper;
import org.lxzx.boot.mapper.UserMapper;
import org.lxzx.boot.mapper.ZaiWeiRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ZaiWeiRecordService {

    @Autowired
    private ZaiWeiRecordMapper zaiWeiRecordMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DynamicService dynamicService;

    @Scheduled(cron = "5 0 0 * * ?")
    public void updateStatus() {
//        每天零点检查更新在位情况
        List<ZaiWeiRecord> zaiWeiRecords = zaiWeiRecordMapper.getExecuteRecord(new Date());
        if(zaiWeiRecords.size() > 0) {
            for(int i = 0; i < zaiWeiRecords.size(); i++) {
                zaiWeiRecordMapper.editRecordExecute(zaiWeiRecords.get(i).getZwRecordId());
//                在位变动类型为1
                dynamicService.addRecord(zaiWeiRecords.get(i), 1);
                userMapper.editUserStatus(zaiWeiRecords.get(i).getChangeStatus(), zaiWeiRecords.get(i).getTargetUserCode());
            }
        }
    }

    public PageResult<ZaiWeiRecord> queryRecordByUserCode(int pageNum, int pageSize, String userCode) {
        PageResult<ZaiWeiRecord> result = new PageResult<>();
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<ZaiWeiRecord> userList = zaiWeiRecordMapper.getRecordByUserCode(userCode);
            PageInfo<ZaiWeiRecord> pageInfo = new PageInfo<>(userList);
            result.setData(pageInfo);
            result.setResult(userList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            PageHelper.clearPage();
        }
        return result;
    }

    public int deleteById(String recordId) {
        int num = 0;
        num = zaiWeiRecordMapper.deleteById(recordId);
        return num;
    }
}
