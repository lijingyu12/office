package com.office.service;

import com.office.common.ServerResponse;
import com.office.pojo.User;

public interface IUserService {



    ServerResponse<User> login(String username,String password);
    ServerResponse<String> resetPasswordByUsername(String username, String passwordNew);
    ServerResponse<String> checkIdNumberAndUserName(String idNumber,String username);
    ServerResponse<User> addUser(User user);
    ServerResponse<String> delUser(Integer id);
    ServerResponse<User> updateUser(User user);
    ServerResponse<String> updatePassword(String passwordOld,String passwordNew,User user);



}
