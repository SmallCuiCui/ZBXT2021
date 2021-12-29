package org.lxzx.boot.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.lxzx.boot.bean.User;
import org.lxzx.boot.dto.DashboardData;

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
            @Result(property = "isDelete", column = "is_delete", jdbcType= JdbcType.TINYINT),
    })
    @Select("SELECT * FROM users WHERE is_delete = false ORDER BY create_time DESC")
    List<User> getAll();

//    查询指定单位下的用户、是否包含领导
    @Select("<script>" +
            " SELECT * FROM users WHERE is_delete = false" +
            " <if test='deptId != null and deptId.trim() != &quot;&quot;'>" +
            " AND dept_id=#{deptId}" +
            " </if>" +
            " <if test='noLeader != null and noLeader'>" +
            " AND limited_id != 'LEADER'" +
            " </if>" +
            " ORDER BY create_time DESC" +
            " </script>")
    @ResultMap("userMap")
    List<User> getUsersByDept(@Param(value="deptId") String deptId, @Param(value="noLeader") boolean noLeader);

    @Select("SELECT * FROM users WHERE user_code = #{userCode} AND is_delete = false")
    @ResultMap("userMap")
    User getOneByCode(String userCode);

    @Select("SELECT * FROM users WHERE user_code = #{userCode} AND password = #{password} AND is_delete = false")
    @ResultMap("userMap")
    User login(String userCode, String password);

    @Insert("INSERT INTO users (user_id, user_code, user_name, password, phone_num, dept_id, dept_name, position_id, position_name, limited_id, limited_name, status, create_user_id, create_user_code, create_user_name, create_time) values (#{userId}, #{userCode}, #{userName}, #{password}, #{phoneNum}, #{deptId}, #{deptName}, #{positionId}, #{positionName}, #{limitedId}, #{limitedName}, #{status}, #{createUserId}, #{createUserCode}, #{createUserName}, #{createTime})")
    int insertUser(User user);

    @Update("UPDATE users SET status = #{status} WHERE user_code = #{userCode}")
    int editUserStatus(String status, String userCode);

//    查询人员在位情况（非领导）
    @Select("SELECT" +
            " COUNT(*) AS allUserNum," +
            " SUM(CASE WHEN status='ZAIWEI' THEN 1 ELSE 0 END) AS zaiWeiNum," +
            " SUM(CASE WHEN status='XUEXI' THEN 1 ELSE 0 END) AS xueXiNum," +
            " SUM(CASE WHEN status='CHUCHAI' THEN 1 ELSE 0 END) AS chuChaiNum," +
            " SUM(CASE WHEN status='QINGJIA' THEN 1 ELSE 0 END) AS qingJiaNum," +
            " SUM(CASE WHEN status='LUNXIU' THEN 1 ELSE 0 END) AS lunXiuNum," +
            " SUM(CASE WHEN status='JIEDIAO' THEN 1 ELSE 0 END) AS jieDiaoNum," +
            " SUM(CASE WHEN status='ZHUYUAN' THEN 1 ELSE 0 END) AS zhuYuanNum," +
            " SUM(CASE WHEN status='PEIHU' THEN 1 ELSE 0 END) AS peiHuNum," +
            " SUM(CASE WHEN status='XIUIJA' THEN 1 ELSE 0 END) AS xiuJiaNum" +
            " FROM users WHERE is_delete = false"
    )
    DashboardData getZaiWeiCondition();

//  查询所有领导在位情况
    @Select("SELECT * FROM users WHERE is_delete = false AND limited_id = 'LEADER'")
    @ResultMap("userMap")
    List<User> getLeaderZaiWei();

    @Update("UPDATE users SET is_delete = true WHERE user_id = #{userId}")
    int deleteById(String userId);
}
