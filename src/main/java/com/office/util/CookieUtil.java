package com.office.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CookieUtil {
    private final static String COOKIE_DOMAIN = "localhost";
    private final static String COOKIE_NAME = "office_login_token";

    public static String readLoginToken(HttpServletRequest request){
        Cookie[] cks = request.getCookies();
        if (cks != null) {
            for(Cookie ck :cks){
                log.info("cookieName:{},cookieValue:{}", ck.getName(), ck.getValue());
                if(StringUtils.equals(ck.getName(),COOKIE_NAME)){
                    log.info("return cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());
                    return ck.getValue();
                }
            }
        }
        return null;
    }

    //X: domain=".happymmall.com"
    //a: A.happymmall.com           cookie: domain = A.happymmall.com; path="/"
    //b: B.happymmall.com           cookie: domain = B.happymmall.com; path="/"
    //c: A.happymmall.com/test/cc   cookie: domain = A.happymmall.com; path="/test/cc"
    //d: A.happymmall.com/test/dd   cookie: domain = A.happymmall.com; path="/test/dd"
    //e: A.happymmall.com/test      cookie: domain = A.happymmall.com; path="/test"
    public static void writeLoginToken(HttpServletResponse response, String token){
        Cookie ck = new Cookie(COOKIE_NAME,token);
        ck.setDomain(COOKIE_DOMAIN);
        ck.setPath("/");//代表设置在根目录
        ck.setHttpOnly(true);//不许通过脚本访问cookie
        //单位是秒 如果不设置 cookie就不会写入硬盘而是写入内存 只在当前页面有效
        ck.setMaxAge(60*60*24*365);//-1代表永久 现在是设置为一年的有效期
        log.info("write cookieName:{},cookieValue:{}", ck.getName(), ck.getValue());
        response.addCookie(ck);
    }

    public static void delLoginToken(HttpServletResponse response, HttpServletRequest request){
        Cookie[] cks = request.getCookies();
        if (cks != null) {
            for(Cookie ck:cks){
                if(StringUtils.equals(ck.getName(),COOKIE_NAME)){
                    ck.setDomain(COOKIE_DOMAIN);
                    ck.setPath("/office");
                    ck.setMaxAge(0);//0代表删除cookie
                    log.info("del cookieName:{},cookieValue:{}", ck.getName(), ck.getValue());
                    response.addCookie(ck);
                    return;
                }
            }
        }

    }
}
