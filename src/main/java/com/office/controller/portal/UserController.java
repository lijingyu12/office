package com.office.controller.portal;

import com.office.common.Const;
import com.office.common.RedisShardedPool;
import com.office.common.ResponseCode;
import com.office.common.ServerResponse;
import com.office.dao.UserMapper;
import com.office.pojo.User;
import com.office.service.IUserService;
import com.office.util.CookieUtil;
import com.office.util.JsonUtil;
import com.office.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    IUserService iUserService;

    @PostMapping(value = "login")
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse){
        ServerResponse<User> response = iUserService.login(username,password);
        if(response.isSuccess()){
            User user = response.getData();
            CookieUtil.writeLoginToken(httpServletResponse,session.getId());
            RedisShardedPoolUtil.setEx(session.getId(),JsonUtil.obj2String(user),Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            return response;
        }
        return response;
    }


    @PostMapping(value = "logout")
    @ResponseBody
    public ServerResponse logout(HttpServletRequest request,HttpServletResponse response) {
       String loginToken = CookieUtil.readLoginToken(request);
       CookieUtil.delLoginToken(response,request);
       RedisShardedPoolUtil.del(loginToken);
       return ServerResponse.createBySuccess();

    }




    @PostMapping(value = "reset_password")
    public ServerResponse<String> resetPassword(String username,String idNum, String passwordNew) {
        ServerResponse<String> check = iUserService.checkIdNumberAndUserName(idNum,username);
        if(check.getStatus()==ResponseCode.SUCCESS.getCode()){
            return iUserService.resetPasswordByUsername(username,passwordNew);
        }
        return check;
    }

    @PostMapping(value = "update_password")
    public ServerResponse<String> updatePassword(HttpServletRequest request, String passwordOld, String passwordNew){
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录无法获取当前用户信息");
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User currentUser =JsonUtil.string2Obj(userJsonStr,User.class);
        if(currentUser == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }

        return iUserService.updatePassword(passwordOld,passwordNew,currentUser);

    }

    @RequestMapping(value = "get_user_info")
    public ServerResponse<User> getUserInfo(HttpServletRequest request){
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录无法获取当前用户信息");
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user!=null){
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("用户未登录无法获取当前用户信息");
    }

}
