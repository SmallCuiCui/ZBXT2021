package org.lxzx.boot.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.lxzx.boot.bean.ZaiWeiRecord;
import org.lxzx.boot.dto.DetailData;

import java.util.Date;
import java.util.List;

public interface ZaiWeiRecordMapper {

    @Results(id = "recordMap", value = {
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
            @Result(property = "dayNum", column = "day_num", jdbcType= JdbcType.INTEGER),
            @Result(property = "isExecute", column = "is_execute", jdbcType= JdbcType.TINYINT),
    })
    @Select("SELECT * FROM zaiwei_records ORDER BY start_time DESC")
    List<ZaiWeiRecord> getAll();

    @Select("SELECT * FROM zaiwei_records WHERE target_user_code = #{userCode} and start_time>#{nowTime} ORDER BY start_time DESC")
    @ResultMap("recordMap")
    List<ZaiWeiRecord> getByStartTime(String userCode, Date nowTime);

    @Select("SELECT * FROM zaiwei_records WHERE target_user_code = #{userCode} AND start_time<#{nowTime} and end_time>#{nowTime} ORDER BY start_time DESC")
    @ResultMap("recordMap")
    List<ZaiWeiRecord> getByTime(String userCode, Date nowTime);

//    获取还未执行且开始时间小于当前时间的记录
    @Select("SELECT * FROM zaiwei_records WHERE is_execute=false AND start_time <= #{nowTime}")
    @ResultMap("recordMap")
    List<ZaiWeiRecord> getExecuteRecord(Date nowTime);

//    获取指定人员非在位记录-
    @Select("SELECT * FROM zaiwei_records WHERE target_user_code = #{userCode}")
    @ResultMap("recordMap")
    List<ZaiWeiRecord> getRecordByUserCode(String userCode);

    @Select("SELECT * FROM zaiwei_records WHERE target_user_code = #{userId}")
    @ResultMap("recordMap")
    List<ZaiWeiRecord> getRecordByUserId(String userId);

//    更新记录执行状态为已执行
    @Update("UPDATE zaiwei_records SET is_execute = true WHERE zw_record_id = #{zwRecordId}")
    int editRecordExecute(String zwRecordId);

    @Insert("INSERT INTO zaiwei_records (zw_record_id, target_user_code, target_user_name, start_time, end_time, origin_status, change_status, commit_time, commit_user_name, commit_user_code, day_num, is_execute) " +
            "values (#{zwRecordId}, #{targetUserCode}, #{targetUserName}, #{startTime}, #{endTime}, #{originStatus}, #{changeStatus}, #{commitTime}, #{commitUserName}, #{commitUserCode}, #{dayNum}, #{isExecute})")
    int insertZaiWeiRecord(ZaiWeiRecord zaiWeiRecord);

//    未生效的记录(今天的时间<开始时间)可以删除
    @Delete("DELETE FROM zaiwei_records WHERE zw_record_id=#{zwRecordId}")
    int deleteById(String zwRecordId);

//    其他状态修改为在位状态；修改结束时间、重新计算天数
    @Update("UPDATE zaiwei_records SET end_time = #{endTime}, day_num = #{dayNum} WHERE zw_record_id = #{zwRecordId}")
    int editRecordTime(Date endTime, int dayNum, String zwRecordId);

//    获取请假、休假、出差
    @Select("SELECT" +
            " SUM(day_num) AS zaiWeiNum," +
            " SUM(CASE WHEN change_status='QINGJIA' THEN 1 ELSE 0 END) AS qingJiaNum," +
            " SUM(CASE WHEN change_status='XIUJIA' THEN 1 ELSE 0 END) AS xiuJiaNum," +
            " SUM(CASE WHEN change_status='LUNXIU' THEN 1 ELSE 0 END) AS lunXiuNum," +
            " SUM(CASE WHEN change_status='CHUCHAI' THEN 1 ELSE 0 END) AS chuChaiNum " +
            " FROM zaiwei_records WHERE target_user_code = #{userCode} AND end_time <= #{nowDate}"
    )
    DetailData getZaiWeiCondition(String userCode, Date nowDate);
}
