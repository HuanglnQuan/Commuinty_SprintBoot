package com.lnquan.community.controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lnquan.community.dto.AccessTokenDTO;
import com.lnquan.community.dto.User;
import com.lnquan.community.utils.GitHubAuthUtil;
import com.lnquan.community.utils.NetWorkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.processing.SupportedSourceVersion;
import javax.servlet.http.HttpServletRequest;

@Controller
@PropertySource("classpath:github.properties")
public class AuthorizeController {
    @Autowired
    private GitHubAuthUtil gitHubAuthUtil;
    // 如果不适用Value注解，就必须配置 @ConfigurationProperties 注解指定前缀，并且为要注入的属性添加get和set方法
    @Value("${github.client.id}")
    private String client_id;
    @Value("${github.client.secret}")
    private String client_secret;
    @Value("${github.client.redirect_uri}")
    private String redirect_uri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request) throws Exception {
        AccessTokenDTO dto = new AccessTokenDTO();
        dto.setClient_id(client_id);
        dto.setClient_secret(client_secret);
        dto.setCode(code);
        dto.setRedirect_uri(redirect_uri);
        dto.setState(state);
        try {
            String accessToken = gitHubAuthUtil.getAccessToken(dto);
            accessToken = accessToken.split("&")[0].split("=")[1];
            String userInfo = gitHubAuthUtil.getUserInfo(accessToken);
            User user = JSON.parseObject(userInfo, User.class);
            if (user != null){
                request.getSession().setAttribute("user", user);
                return "redirect:/";
            }else{
                return "redirect:/";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "index";
    }
}
