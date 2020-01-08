package com.lnquan.community.controllers;

import com.alibaba.fastjson.JSON;
import com.lnquan.community.beans.GithubUser;
import com.lnquan.community.beans.User;
import com.lnquan.community.dto.AccessTokenDTO;
import com.lnquan.community.service.UserService;
import com.lnquan.community.utils.GitHubAuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;

@Controller
@PropertySource("classpath:github.properties")
public class AuthorizeController {
    @Autowired
    private GitHubAuthUtil gitHubAuthUtil;
    @Autowired
    private UserService userService;
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
                           HttpServletResponse response) throws Exception {
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
            GithubUser githubUser = JSON.parseObject(userInfo, GithubUser.class);
            if (githubUser != null){
                User user = new User();
                user.setAccountId(githubUser.getId());
                user.setName(githubUser.getLogin());
                String token = UUID.randomUUID().toString();
                user.setToken(token);
                user.setGmtCreate(new Date().getTime());
                user.setGmtModified(new Date().getTime());
                user.setAvatarUrl(githubUser.getAvatar_url());
                user.setBio(githubUser.getBio());
                userService.addUser(user);
                response.addCookie(new Cookie("token", token));
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
