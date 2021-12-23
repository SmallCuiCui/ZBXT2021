package org.lxzx.boot.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String userId;
    private String userCode; //登录时使用
    private String userName;
    private String password; //密码
    private String phoneNum; //电话号码

    private String deptId; //部门id
    private String deptName;//部门名称
    private String positionId; //职务类别id
    private String positionName; //职务类别名称

    private String limitedId; //用户权限
    private String limitedName; //权限名称

    private String status;//在位状态


    private String createUserId; // 创建人id
    private String createUserCode;// 创建人code
    private String createUserName; // 创建人name
    private Date createTime; //创建时间

    private boolean isDelete; //是否删除

    public User(String userCode, String userName, String password, String phoneNum) {
        this.userCode = userCode;
        this.userName = userName;
        this.password = password;
        this.phoneNum = phoneNum;
    }

    public User(String userCode, String userName) {
        this.userCode = userCode;
        this.userName = userName;
    }
}
