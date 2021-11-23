package org.lxzx.boot.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.lxzx.boot.bean.Notice;

import java.util.Date;
import java.util.List;

public interface NoticeMapper {

    @Results(id = "noticeMap", value = {
            @Result(property = "noticeId", column = "notice_id", id=true, jdbcType= JdbcType.VARCHAR),
            @Result(property = "noticeTitle", column = "notice_title", jdbcType= JdbcType.VARCHAR),
            @Result(property = "noticeContent", column = "notice_content", jdbcType= JdbcType.VARCHAR),
            @Result(property = "isPublish", column = "is_publish", jdbcType= JdbcType.TINYINT),
            @Result(property = "whetherScreen", column = "whether_screen", jdbcType= JdbcType.TINYINT),
            @Result(property = "publishTime", column = "publish_time"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "createUserId", column = "create_user_id", jdbcType= JdbcType.VARCHAR),
            @Result(property = "createUserName", column = "create_user_name", jdbcType= JdbcType.VARCHAR),
            @Result(property = "readUserCodes", column = "read_user_codes", jdbcType= JdbcType.VARCHAR),
    })
    @Select("SELECT * FROM notices ORDER BY create_time DESC")
    List<Notice> getAll();


    @Insert("INSERT INTO notices (notice_id, notice_title, notice_content, is_publish, whether_screen, publish_time, create_time, create_user_id, create_user_name, read_user_codes) values (#{noticeId}, #{noticeTitle}, #{noticeContent}, #{isPublish}, #{whetherScreen}, #{publishTime}, #{createTime}, #{createUserId}, #{createUserName}, #{readUserCodes})")
    int insertNotice(Notice notice);

    @Select("SELECT * FROM notices WHERE notice_id = #{noticeId}")
    @ResultMap("noticeMap")
    Notice queryWorkById(String noticeId);

    @Delete("DELETE FROM notices WHERE notice_id=#{noticeId}")
    int deleteById(String noticeId);

//    立即发布
    @Update("UPDATE notices SET publish_time = #{date}, is_publish = #{isPublish} where notice_id = #{noticeId}")
    int publishNowNoticeById(String noticeId, Date date, boolean isPublish);

//    编辑
    @Update("UPDATE notices SET notice_title = #{noticeTitle}, notice_content = #{noticeContent}, whether_screen = #{whetherScreen}, is_publish = #{isPublish}, publish_time = #{publishTime} where notice_id = #{noticeId}")
    int handleEditNoticeById(Notice notice);
}
