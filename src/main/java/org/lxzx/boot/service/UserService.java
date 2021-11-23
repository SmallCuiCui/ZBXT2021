package org.lxzx.boot.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.lxzx.boot.Utils.CommonUtil;
import org.lxzx.boot.Utils.MD5Util;
import org.lxzx.boot.dto.PageResult;
import org.lxzx.boot.bean.User;
import org.lxzx.boot.enums.LimitedEnum;
import org.lxzx.boot.enums.StatusEnum;
import org.lxzx.boot.enums.StringEnum;
import org.lxzx.boot.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User checkUserLogin(User user) {
        User u = userMapper.login(user.getUserCode(), MD5Util.getMD5(user.getPassword()));
        return u;
    }

    public int addUser(User user, User sessionUser) {
        if(userMapper.getOneByCode(user.getUserCode()) != null) {
            return 0;
        } else {
            user.setUserId(CommonUtil.getUUId());
            user.setStatus(StatusEnum.ZAIWEI.getValue());
            user.setLimitedName(LimitedEnum.stateOf(user.getLimitedId()).getName());
            user.setCreateTime(new Date());
            user.setCreateUserId(sessionUser.getUserId());
            user.setCreateUserCode(sessionUser.getUserCode());
            user.setCreateUserName(sessionUser.getUserName());
            user.setPassword(MD5Util.getMD5(StringEnum.DEFAULTPASSWORD.getInfo()));
            int num = userMapper.insertUser(user);
            return num;
        }
    }

    public int changePassword(String userCode, String originPassword, String newPassword) {
        User user = userMapper.getOneByCode(userCode);
        if(user != null) {
            if(user.getPassword().equals(MD5Util.getMD5(originPassword))) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }

    public PageResult<User> queryAllUser(int pageNum, int pageSize) {
        PageResult<User> result = new PageResult<>();
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<User> userList = userMapper.getAll();
            PageInfo<User> pageInfo = new PageInfo<>(userList);
            result.setData(pageInfo);
            result.setResult(userList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            PageHelper.clearPage();
        }
        return result;
    }

    public List<User> queryUserByDeptId(String deptId) {
        List<User> userList = userMapper.getUsersByDept(deptId);
        return userList;
    }


}
