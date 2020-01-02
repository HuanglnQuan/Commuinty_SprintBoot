package com.lnquan.community.controllers;

import com.lnquan.community.beans.User;
import com.lnquan.community.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @Autowired
    private UserDao userDao;
    private String token = "token";

    @RequestMapping(value = "/")
    public String goIndex(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (token.equals(cookie.getName())){
                User user = userDao.queryByToken(cookie.getValue());
                if (user != null)
                    request.getSession().setAttribute("user", user);
                break;
            }
        }
        return "index";
    }
}
