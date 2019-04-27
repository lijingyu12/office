package com.office.common.interceptors;

import com.google.common.collect.Maps;
import com.office.common.Const;
import com.office.common.RedisShardedPool;
import com.office.common.ServerResponse;
import com.office.pojo.User;
import com.office.util.CookieUtil;
import com.office.util.JsonUtil;
import com.office.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle");
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBean().getClass().getSimpleName();
        StringBuffer requestParamBuffer = new StringBuffer();
        Map paramMap = request.getParameterMap();
        Iterator it = paramMap.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry entry =(Map.Entry) it.next();
            String mapKey = (String) entry.getKey();
            String mapValue = StringUtils.EMPTY;
            Object obj = entry.getValue();
            if(obj instanceof String[]){
                String[] strs = (String[]) obj;
                mapValue = Arrays.toString(strs);
            }
            requestParamBuffer.append(mapKey).append("=").append(mapValue);
        }
        if(StringUtils.equals(className,"UserManageController")&&StringUtils.equals(methodName,"login")){
            log.info("权限拦截器拦截到请求,className:{},methodName:{}",className,methodName);
            return true;
        }
        if(StringUtils.equals(className,"UserManageController")&&StringUtils.equals(methodName,"login"))
        log.info("权限拦截器拦截到请求,className:{},methodName:{},parame:{}",className,methodName,requestParamBuffer);
        User user =null;
        String loginToken = CookieUtil.readLoginToken(request);
        log.info("loginToken={}",loginToken);
        if(StringUtils.isNotEmpty(loginToken)){
            String userJsonstr = RedisShardedPoolUtil.get(loginToken);
            user = JsonUtil.string2Obj(userJsonstr,User.class);
        }


        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        if(user==null || user.getRole ().intValue() != Const.Role.ROLE_ADMIN){
            PrintWriter out = response.getWriter();
            if(user == null){
                out.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("用户未登录")));

            }else if(user.getRole ().intValue() != Const.Role.ROLE_ADMIN){
                out.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("无权限操作")));

            }
            out.flush();
            out.close();
            return false;

        }


        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("afterCompletion");
    }
}
