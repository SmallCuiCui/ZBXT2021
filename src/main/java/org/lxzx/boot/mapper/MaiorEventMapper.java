package org.lxzx.boot.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.lxzx.boot.bean.MaiorEvent;
import org.lxzx.boot.bean.Notice;
import org.lxzx.boot.bean.User;

import java.util.Date;
import java.util.List;

public interface MaiorEventMapper {

    @Results(id = "userMap", value = {
            @Result(property = "eventId", column = "event_id", id=true, jdbcType= JdbcType.VARCHAR),
            @Result(property = "eventDate", column = "event_date"),
            @Result(property = "adress", column = "adress", jdbcType= JdbcType.VARCHAR),
            @Result(property = "content", column = "content", jdbcType= JdbcType.VARCHAR),
            @Result(property = "title", column = "title", jdbcType= JdbcType.VARCHAR),
            @Result(property = "fileURL", column = "fileURL", jdbcType= JdbcType.VARCHAR),
            @Result(property = "isLock", column = "is_lock", jdbcType= JdbcType.TINYINT),
    })
    @Select("<script>" +
            " SELECT * FROM major_events WHERE 1=1" +
            " <if test='startDate != null'>" +
            " <![CDATA[ AND event_date >= #{startDate}]]>" +
            " </if>" +
            " <if test='endDate != null'>" +
            " <![CDATA[ AND event_date <= #{endDate}]]>" +
            " </if>" +
            " <if test='searchValue != null and searchValue.trim() != &quot;&quot;'>" +
            " AND title LIKE CONCAT(CONCAT('%', #{searchValue}), '%') OR content LIKE CONCAT(CONCAT('%', #{searchValue}), '%')" +
            " </if>" +
            " ORDER BY event_date DESC" +
            " </script>")
    List<MaiorEvent> getAll(Date startDate, Date endDate, String searchValue);

    @Insert("INSERT INTO major_events (event_id, event_date, adress, content, title, fileURL, is_lock) values (#{eventId}, #{eventDate}, #{adress}, #{content}, #{title}, #{fileURL}, #{isLock})")
    int insertEvent(MaiorEvent event);

    @Delete("DELETE FROM major_events WHERE event_id=#{eventId}")
    int deleteById(String eventId);

    @Select("SELECT * FROM major_events WHERE event_id=#{eventId}")
    MaiorEvent getEventById(String eventId);

    @Update("UPDATE major_events SET is_lock = true where event_id = #{eventId}")
    int lockEventById(String eventId);

//    编辑
    @Update("UPDATE major_events SET event_date = #{eventDate}, content = #{content}, title = #{title} where event_id = #{eventId}")
    int handleEditEventById(MaiorEvent event);
}
