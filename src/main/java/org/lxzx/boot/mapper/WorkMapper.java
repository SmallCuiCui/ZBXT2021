package org.lxzx.boot.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.lxzx.boot.bean.User;
import org.lxzx.boot.bean.Work;

import java.util.Date;
import java.util.List;

public interface WorkMapper {

    @Results(id = "workMap", value = {
            @Result(property = "workId", column = "work_id", id=true, jdbcType= JdbcType.VARCHAR),
            @Result(property = "deptId", column = "dept_id", jdbcType= JdbcType.VARCHAR),
            @Result(property = "deptName", column = "dept_name", jdbcType= JdbcType.VARCHAR),
            @Result(property = "fillUserCodes", column = "filluser_codes", jdbcType= JdbcType.VARCHAR),
            @Result(property = "fillUserNames", column = "filluser_names", jdbcType= JdbcType.VARCHAR),
            @Result(property = "startTime", column = "start_time", jdbcType= JdbcType.DATE),
            @Result(property = "endTime", column = "end_time", jdbcType= JdbcType.DATE),
            @Result(property = "workTime", column = "work_time", jdbcType= JdbcType.VARCHAR),
            @Result(property = "workContent", column = "work_content", jdbcType= JdbcType.VARCHAR),
            @Result(property = "workContentPlan", column = "work_content_plan", jdbcType= JdbcType.VARCHAR),
            @Result(property = "workType", column = "work_type", jdbcType= JdbcType.VARCHAR),
            @Result(property = "status", column = "status", jdbcType= JdbcType.VARCHAR),
            @Result(property = "relatedUsers", column = "related_users", jdbcType= JdbcType.VARCHAR),
            @Result(property = "commitTime", column = "commit_time", jdbcType= JdbcType.DATE),
            @Result(property = "createTime", column = "create_time", jdbcType= JdbcType.DATE)
    })
    @Select("SELECT * FROM works ORDER BY create_time ASC")
    List<Work> getAll();

    @Select("SELECT * FROM works WHERE dept_id = #{deptId} ORDER BY create_time ASC")
    @ResultMap("workMap")
    List<Work> getAllByDeptId(String deptId);

//    @Select("SELECT * FROM (SELECT * FROM works WHERE status = 'PUBLISH' ORDER BY dept_id desc) as a GROUP BY a.start_time, a.end_time, a.dept_id")
    @Select("<script>" +
            " SELECT * FROM" +
            " (SELECT * FROM works WHERE status = 'PUBLISH'" +
            " <if test='work" +
            "Type != null and workType.trim() != &quot;&quot;'>" +
            " AND work_type=#{workType}" +
            " </if>" +
            " <if test='startTime != null and workType.trim() != &quot;&quot;'>" +
            " AND start_time=#{startTime}" +
            " </if>" +
            " <if test='endTime != null and workType.trim() != &quot;&quot;'>" +
            " AND end_time=#{endTime}" +
            " </if>" +
            " <if test='deptId != null and workType.trim() != &quot;&quot;'>" +
            " AND dept_id=#{deptId}" +
            " </if>" +
            " ORDER BY dept_id desc) " +
            " as a GROUP BY a.start_time, a.end_time, a.dept_id" +
            " </script>")
    @ResultMap("workMap")
    List<Work> getWorkGroupByTime(Work work);

    @Select("SELECT * FROM works WHERE work_id = #{workId}")
    @ResultMap("workMap")
    Work queryWorkById(String workId);

    @Select("SELECT * FROM works WHERE start_time = #{startTime} AND end_time = #{endTime} AND dept_id = #{deptId}")
    Work queryByTimeAndDept(Date startTime, Date endTime, String deptId);

//    编辑--相关人员
    @Update("update works set filluser_codes=#{fillUserCodes},filluser_names=#{fillUserNames},work_content=#{workContent},status=#{status},related_users=#{relatedUsers},commit_time=#{commitTime},work_content_plan=#{workContentPlan} where work_id=#{workId}")
    int updateWorkById(Work work);

    @Insert("INSERT INTO works (work_id, dept_id, dept_name, filluser_codes, filluser_names, start_time, end_time, work_time, work_content, work_type, status, related_users, commit_time, work_content_plan, create_time) values (#{workId}, #{deptId}, #{deptName}, #{fillUserCodes}, #{fillUserNames}, #{startTime}, #{endTime}, #{workTime}, #{workContent}, #{workType}, #{status}, #{relatedUsers}, #{commitTime}, #{workContentPlan}, #{createTime})")
    int insertWork(Work work);

    @Delete("DELETE FROM works WHERE work_id=#{workId}")
    int deleteById(String workId);

//    提交
    @Update("UPDATE works SET status = #{status},commit_time = #{date} where work_id = #{workId}")
    int publicWorkById(String status, String workId, Date date);

}
