package org.lxzx.boot.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.lxzx.boot.bean.Schedule;
import org.lxzx.boot.dto.StatisticData;

import java.util.Date;
import java.util.List;

public interface ScheduleMapper {

    @Results(id = "scheduleMap", value = {
            @Result(property = "scheduleId", column = "schedule_id", id=true, jdbcType= JdbcType.VARCHAR),
            @Result(property = "zhuGuanId", column = "zhuguan_id", jdbcType= JdbcType.VARCHAR),
            @Result(property = "zhuGuan", column = "zhuguan", jdbcType= JdbcType.VARCHAR),
            @Result(property = "lingDaoId", column = "lingdao_id", jdbcType= JdbcType.VARCHAR),
            @Result(property = "lingDao", column = "lingdao", jdbcType= JdbcType.VARCHAR),
            @Result(property = "zhiBanYuanId", column = "zhibanyuan_id", jdbcType= JdbcType.VARCHAR),
            @Result(property = "zhiBanYuan", column = "zhibanyuan", jdbcType= JdbcType.VARCHAR),
            @Result(property = "beiBanYuanId", column = "beibanyuan_id", jdbcType= JdbcType.VARCHAR),
            @Result(property = "beiBanYuan", column = "beibanyuan", jdbcType= JdbcType.VARCHAR),
            @Result(property = "qinWu", column = "qinwu", jdbcType= JdbcType.VARCHAR),
            @Result(property = "jiaShiYuan", column = "jiashiyuan", jdbcType= JdbcType.VARCHAR),
            @Result(property = "lianzhi1", column = "lianzhi1", jdbcType= JdbcType.INTEGER),
            @Result(property = "lianzhi2", column = "lianzhi2", jdbcType= JdbcType.VARCHAR),
            @Result(property = "remark", column = "remark", jdbcType= JdbcType.VARCHAR),
            @Result(property = "remark", column = "remark", jdbcType= JdbcType.VARCHAR),
            @Result(property = "startTime", column = "start_time"),
            @Result(property = "endTime", column = "end_time"),
            @Result(property = "dayNum", column = "day_num", jdbcType= JdbcType.INTEGER),
            @Result(property = "isWeekday", column = "is_weekday", jdbcType= JdbcType.TINYINT),
            @Result(property = "scheduleType", column = "schedule_type", jdbcType= JdbcType.VARCHAR),
    })
    @Select("SELECT * FROM schedule WHERE schedule_type = #{type} ORDER BY start_time DESC")
    List<Schedule> getAll(String type);


    @Select("SELECT * FROM schedule WHERE schedule_type = #{type} and remark=#{remark} ORDER BY start_time ASC")
    @ResultMap("scheduleMap")
    List<Schedule> queryByRemark(String type, String remark);

//    查询时间重合1
    @Select("SELECT * FROM schedule WHERE start_time <= #{startTime} AND end_time > #{startTime}")
    @ResultMap("scheduleMap")
    List<Schedule> queryByStartTime(Date startTime);


 //    查询时间重合2
    @Select("SELECT * FROM schedule WHERE start_time < #{endTime} AND #{endTime} <= end_time")
    @ResultMap("scheduleMap")
    List<Schedule> queryByEndTime(Date endTime);

//    新增值班
    @Insert("INSERT INTO schedule (schedule_id, zhuguan_id, zhuguan, lingdao_id, lingdao, zhibanyuan_id, zhibanyuan, beibanyuan_id, beibanyuan, qinwu, jiashiyuan, lianzhi1, lianzhi2, remark, start_time, end_time, day_num, is_weekday, schedule_type, create_time) " +
            "values (#{scheduleId}, #{zhuGuanId}, #{zhuGuan}, #{lingDaoId}, #{lingDao}, #{zhiBanYuanId}, #{zhiBanYuan}, #{beiBanYuanId}, #{beiBanYuan}, #{qinWu}, #{jiaShiYuan}, #{lianzhi1}, #{lianzhi2}, #{remark}, #{startTime}, #{endTime}, #{dayNum}, #{isWeekday}, #{scheduleType}, #{createTime})")
    int insertSchedule(Schedule schedule);

//    在插入后台值班的同时，根据时间插入一楼值班、人员未安排。
    @Insert("<script>" +
            " INSERT INTO schedule (schedule_id, remark, start_time, end_time, day_num, schedule_type, is_weekday)" +
            " values " +
            " <foreach collection = \"list\" item=\"item\" index=\"index\" separator=\",\">" +
            " (#{item.scheduleId}, #{item.remark}, #{item.startTime}, #{item.endTime}, #{item.dayNum}, #{item.scheduleType}, #{item.isWeekday})" +
            " </foreach>" +
            "</script>")
    int insertScheduleOneList(@Param("list") List<Schedule> scheduleList);

    @Update("UPDATE schedule SET zhuguan_id = #{zhuGuanId}, zhuguan = #{zhuGuan}, lingdao_id = #{lingDaoId}, lingdao = #{lingDao}, zhibanyuan_id = #{zhiBanYuanId}," +
            " zhibanyuan = #{zhiBanYuan}, beibanyuan_id = #{beiBanYuanId}, beibanyuan = #{beiBanYuan}, qinwu = #{qinWu}, jiaShiYuan = #{jiashiyuan}," +
            " lianzhi1 = #{lianzhi1}, lianzhi2 = #{lianzhi2}, remark = #{remark}, startTime = #{start_time}, endTime = #{end_time}," +
            " dayNum = #{day_num}, isWeekday = #{is_weekday}, scheduleType = #{schedule_type}" +
            " where schedule_id = #{scheduleId}")
    int handleEditSchedule(Schedule schedule);

    @Update("UPDATE schedule SET zhibanyuan_id = #{zhiBanYuanId}, zhibanyuan = #{zhiBanYuan} where schedule_id = #{scheduleId}")
    int handleEditZhiQin(Schedule schedule);

    @Select("SELECT * FROM schedule WHERE schedule_id = #{scheduleId}")
    @ResultMap("scheduleMap")
    Schedule queryWorkById(String scheduleId);

    @Delete("DELETE FROM schedule WHERE schedule_id=#{scheduleId}")
    int deleteById(String scheduleId);

//    获取本周值班--时间判断+八楼排班
    @Select("SELECT * FROM schedule WHERE start_time <= #{nowTime} AND #{endTime} <= end_time AND schedule_type = 'EIGHT'")
    @ResultMap("scheduleMap")
    List<Schedule> queryThisWeek(Date nowTime);

//    值班统计获取
    @Select("SELECT zhibanyuan_id, zhibanyuan," +
            " COUNT(*) AS allZhiBanNum, " +
            " SUM(CASE WHEN is_weekday=TRUE THEN 1 ELSE 0 END) AS workNum," +
            " SUM(CASE WHEN is_weekday=FALSE THEN 1 ELSE 0 END) AS holidayNum" +
            " FROM schedule WHERE zhibanyuan_id = #{nowTime}"
    )
    StatisticData getStatistics(String userCode);
}
