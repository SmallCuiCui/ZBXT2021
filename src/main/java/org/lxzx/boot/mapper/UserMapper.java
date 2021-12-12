package org.lxzx.boot.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.lxzx.boot.bean.Schedule;
import org.lxzx.boot.bean.User;

import java.util.List;

public interface UserMapper {

    @Results(id = "userMap", value = {
            @Result(property = "userId", column = "user_id", id=true, jdbcType= JdbcType.VARCHAR),
            @Result(property = "userCode", column = "user_code", jdbcType= JdbcType.VARCHAR),
            @Result(property = "userName", column = "user_name", jdbcType= JdbcType.VARCHAR),
            @Result(property = "password", column = "password", jdbcType= JdbcType.VARCHAR),
            @Result(property = "phoneNum", column = "phone_num", jdbcType= JdbcType.VARCHAR),
            @Result(property = "deptId", column = "dept_id", jdbcType= JdbcType.VARCHAR),
            @Result(property = "deptName", column = "dept_name", jdbcType= JdbcType.VARCHAR),
            @Result(property = "positionId", column = "position_id", jdbcType= JdbcType.VARCHAR),
            @Result(property = "positionName", column = "position_name", jdbcType= JdbcType.VARCHAR),
            @Result(property = "status", column = "status", jdbcType= JdbcType.VARCHAR),
            @Result(property = "createUserId", column = "create_user_id", jdbcType= JdbcType.VARCHAR),
            @Result(property = "createUserName", column = "create_user_name", jdbcType= JdbcType.VARCHAR),
            @Result(property = "createUserCode", column = "create_user_code", jdbcType= JdbcType.VARCHAR),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "limitedId", column = "limited_id", jdbcType= JdbcType.VARCHAR),
            @Result(property = "limitedName", column = "limited_name", jdbcType= JdbcType.VARCHAR),
    })
    @Select("SELECT * FROM users")
    List<User> getAll();

    @Select("<script>" +
            " SELECT * FROM users WHERE 1 = 1" +
            " <if test='deptId != null and deptId.trim() != &quot;&quot;'>" +
            " AND dept_id=#{deptId}" +
            " </if>" +
            " <if test='deptId != null and deptId.trim() != &quot;&quot;'>" +
            " AND dept_id=#{deptId}" +
            " </if>" +
            " </script>")
    @ResultMap("userMap")
    List<User> getUsersByDept(String deptId);

    @Select("SELECT * FROM users WHERE user_code = #{userCode}")
    @ResultMap("userMap")
    User getOneByCode(String userCode);

    @Select("SELECT * FROM users WHERE user_code = #{userCode} AND password = #{password}")
    @ResultMap("userMap")
    User login(String userCode, String password);

    @Insert("INSERT INTO users (user_id, user_code, user_name, password, phone_num, dept_id, dept_name, position_id, position_name, limited_id, limited_name, status, create_user_id, create_user_code, create_user_name, create_time) values (#{userId}, #{userCode}, #{userName}, #{password}, #{phoneNum}, #{deptId}, #{deptName}, #{positionId}, #{positionName}, #{limitedId}, #{limitedName}, #{status}, #{createUserId}, #{createUserCode}, #{createUserName}, #{createTime})")
    int insertUser(User user);

    @Update("UPDATE users SET status = #{status} WHERE userId = #{userId}")
    int editUserStatus(String status, String userId);
}
