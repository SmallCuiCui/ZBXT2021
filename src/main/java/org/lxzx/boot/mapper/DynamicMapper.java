package org.lxzx.boot.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import org.lxzx.boot.bean.Dynamic;
import org.lxzx.boot.bean.Notice;

import java.util.Date;
import java.util.List;

public interface DynamicMapper {

    @Results(id = "dynamicMap", value = {
            @Result(property = "dynamicId", column = "dynamic_id", id=true, jdbcType= JdbcType.VARCHAR),
            @Result(property = "changeTime", column = "change_time"),
            @Result(property = "discription", column = "discription", jdbcType= JdbcType.VARCHAR),
            @Result(property = "originStatus", column = "origin_status", jdbcType= JdbcType.VARCHAR),
            @Result(property = "newStatus", column = "new_status", jdbcType= JdbcType.VARCHAR),
            @Result(property = "targetUser", column = "target_user", jdbcType= JdbcType.VARCHAR),
            @Result(property = "handleUser", column = "handle_user", jdbcType= JdbcType.VARCHAR),
            @Result(property = "recordType", column = "record_type", jdbcType= JdbcType.INTEGER),
            @Result(property = "createTime", column = "create_time"),
    })
    @Select("SELECT * FROM dynamic WHERE record_type = #{type} ORDER BY create_time DESC")
    List<Dynamic> getAll(int type);

    @Select("SELECT count(*) FROM dynamic WHERE record_type = #{type} AND create_time >= #{startTime} AND create_time <=#{endTime} ORDER BY create_time DESC")
    int getDynamicCount(int type, Date startTime, Date endTime);

    @Insert("INSERT INTO dynamic (dynamic_id, change_time, discription, origin_status, new_status, target_user, handle_user, record_type, create_time) values (#{dynamicId}, #{changeTime}, #{discription}, #{originStatus}, #{newStatus}, #{targetUser}, #{handleUser}, #{recordType}, #{createTime})")
    int insertDynamic(Dynamic dynamic);
}
