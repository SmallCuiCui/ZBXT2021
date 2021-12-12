package org.lxzx.boot.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.lxzx.boot.bean.ZaiWeiRecord;

import java.util.List;

public interface ZaiWeiRecordMapper {

    @Results(id = "RecordMap", value = {
            @Result(property = "zwRecordId", column = "zw_record_id", id=true, jdbcType= JdbcType.VARCHAR),
            @Result(property = "targetUserCode", column = "target_user_code", jdbcType= JdbcType.VARCHAR),
            @Result(property = "targetUserName", column = "target_user_name", jdbcType= JdbcType.VARCHAR),
            @Result(property = "startTime", column = "start_time"),
            @Result(property = "endTime", column = "end_time"),
            @Result(property = "originStatus", column = "origin_status", jdbcType= JdbcType.VARCHAR),
            @Result(property = "changeStatus", column = "change_status", jdbcType= JdbcType.VARCHAR),
            @Result(property = "commitTime", column = "commit_time"),
            @Result(property = "commitUserName", column = "commit_user_name", jdbcType= JdbcType.VARCHAR),
            @Result(property = "commitUserCode", column = "commit_user_code", jdbcType= JdbcType.VARCHAR),
    })
    @Select("SELECT * FROM zaiwei_records ORDER BY start_time DESC")
    List<ZaiWeiRecord> getAll();

    @Insert("INSERT INTO schedule (zw_record_id, target_user_code, target_user_name, start_time, end_time, origin_status, change_status, commit_time, commit_user_name, commit_user_code) " +
            "values (#{zwRecordId}, #{targetUserCode}, #{targetUserName}, #{startTime}, #{endTime}, #{originStatus}, #{changeStatus}, #{commitTime}, #{commitUserName}, #{commitUserCode})")
    int insertZaiWeiRecord(ZaiWeiRecord zaiWeiRecord);

//    在今天的时间<开始时间时可以删除
    @Delete("DELETE FROM zaiwei_records WHERE zw_record_id=#{zwRecordId}")
    int deleteById(String zwRecordId);
}
