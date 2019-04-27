package com.office.service.impl;

import com.office.common.Const;
import com.office.common.ServerResponse;
import com.office.dao.UserMapper;
import com.office.pojo.User;
import com.office.service.IUserService;
import com.office.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if(resultCount == 0) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        // 使用md5加密 密码  ，目的是为了让数据库里面的密码不是明文
        // 判断是否为初始密码 ，不是初始密码就加密再查询
        String md5Password;
        if(!password.equals(Const.DEFAULT_PASSWORD)){
            md5Password = MD5Util.MD5EncodeUtf8(password);
        }else{
            md5Password = password;
            System.out.println(md5Password+"md5");
        }

        User user = userMapper.selectLogin(username,md5Password);
        if(user == null ){
            return ServerResponse.createByErrorMessage("密码错误");
        }

        return ServerResponse.createBySuccess("登陆成功",user);
    }
    public ServerResponse<String> checkIdNumberAndUserName(String idNumber,String username){
        int resultCount = userMapper.checkIdNumberAndUserName(idNumber,username);
        if(resultCount>0){
            return ServerResponse.createBySuccessMessage("验证成功");
        }
        return ServerResponse.createByErrorMessage("工号或身份证号错误");
    }

    public ServerResponse<String> resetPasswordByUsername(String username, String passwordNew){
        String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
        int resultCount = userMapper.resetPasswordByUserName(username,md5Password);
        if(resultCount>0){
            return ServerResponse.createBySuccessMessage("修改密码成功");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    @Override
    public ServerResponse<User> addUser(User user) {
        //判断用户是否存在在
        int isExsist = userMapper.checkUsername(user.getUsername());
        if(isExsist>0){
            return ServerResponse.createByErrorMessage("工号已存在");
        }
        user.setCreateTime(DateTime.now().toDate());
        user.setUpdateTime(DateTime.now().toDate());
        int resultCount = userMapper.insert(user);
        if(resultCount>0){
            User returnUser = userMapper.selectUserByUsername(user.getUsername());
            return ServerResponse.createBySuccess("添加成功",returnUser);
        }
        return ServerResponse.createByErrorMessage("添加失败，请重试");
    }

    @Override
    public ServerResponse<String> delUser(Integer id) {
        int resultCount = userMapper.deleteByPrimaryKey(id);
        if(resultCount>0){
            return ServerResponse.createBySuccessMessage("删除成功");
        }
        return ServerResponse.createByErrorMessage("删除失败,请重试");
    }

    @Override
    public ServerResponse<User> updateUser(User user) {
        int resultCount = userMapper.updateByPrimaryKeySelective(user);
        if(resultCount>0){
            User returnUser = userMapper.selectByPrimaryKey(user.getId());
            return ServerResponse.createBySuccess("修改成功",returnUser);
        }
        return ServerResponse.createByErrorMessage("修改失败,请重试");
    }

    @Override
    public ServerResponse<String> updatePassword(String passwordOld, String passwordNew, User user) {
        if(!StringUtils.equals(passwordOld,"123456")){
            passwordOld = MD5Util.MD5EncodeUtf8(passwordOld);
        }
        int resultCount = userMapper.checkPassword(passwordOld,user.getId());
        if(resultCount == 0){
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount>0){
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");
    }
}
