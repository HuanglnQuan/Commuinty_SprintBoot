package com.lnquan.community.utils;

import com.lnquan.community.dto.AccessTokenDTO;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.nio.ch.Net;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Component
public class GitHubAuthUtil {
    private static String accessTokenUrl = "https://github.com/login/oauth/access_token";
    private static String userInfoUrl = "https://api.github.com/user";
    @Autowired
    private NetWorkUtil netWorkUtil;

    public String getAccessToken(AccessTokenDTO dto) throws URISyntaxException {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("code", dto.getCode()));
        parameters.add(new BasicNameValuePair("state", dto.getState()));
        parameters.add(new BasicNameValuePair("client_id", dto.getClient_id()));
        parameters.add(new BasicNameValuePair("redirect_uri", dto.getRedirect_uri()));
        parameters.add(new BasicNameValuePair("client_secret", dto.getClient_secret()));
        return netWorkUtil.post(accessTokenUrl, parameters);
    }

    public String getUserInfo(String access_token) throws URISyntaxException {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("access_token", access_token));
        return netWorkUtil.get(userInfoUrl, parameters);
    }
}
