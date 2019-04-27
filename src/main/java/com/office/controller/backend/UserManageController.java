package com.office.controller.backend;

import com.office.common.Const;
import com.office.common.ServerResponse;
import com.office.pojo.User;
import com.office.service.IUserService;
import com.office.util.CookieUtil;
import com.office.util.JsonUtil;
import com.office.util.RedisShardedPoolUtil;
import org.apache.catalina.Server;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.print.DocFlavor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manage/user")
public class UserManageController {
    @Autowired
    IUserService iUserService;

    /**
     * 登陆
     * @param username
     * @param password
     * @param session
     * @param httpServletResponse
     * @return
     */
    @PostMapping(value = "login")
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse){
        ServerResponse<User> response = iUserService.login(username,password);
        if(response.isSuccess()){
            User user =response.getData();
            if(user.getRole() == Const.Role.ROLE_CUSTOMER){
                CookieUtil.writeLoginToken(httpServletResponse,session.getId());
                RedisShardedPoolUtil.setEx(session.getId(),JsonUtil.obj2String(user),Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
                return response;
            }else{
                return ServerResponse.createByErrorMessage("不是管理员无法登陆");
            }
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

    @PostMapping(value = "add_user")
    @ResponseBody
    public ServerResponse<User> addUser(@RequestBody User user) {
      //拦截器已经验证了登录权限 才会执行这个Controller
        return iUserService.addUser(user);
    }

    @RequestMapping(value = "del_user")
    public ServerResponse<String> delUser(Integer id){
        return iUserService.delUser(id);
    }

    @PostMapping(value = "update_user")
    public ServerResponse<User> updateUser(@RequestBody User user) {
        return iUserService.updateUser(user);
    }




}
