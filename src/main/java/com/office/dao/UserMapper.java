package com.office.dao;

import com.office.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername(String username);

    User selectLogin(@Param("username") String username, @Param("password") String password);

    int checkPassword(@Param("password") String password,@Param("userId") int userId);

    int checkIdNumberAndUserName(@Param("idNum") String idNum,@Param("username") String username);

    int resetPasswordByUserName(@Param("username") String username,@Param("passwordNew") String passwordNew);

    User selectUserByUsername(String username);

    int updateByDepartId(Integer departId);

    int selectbyDepart(Integer departId);

    int selectbyPosition(Integer posId);

}