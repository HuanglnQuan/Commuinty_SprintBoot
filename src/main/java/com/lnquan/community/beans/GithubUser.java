package com.lnquan.community.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GithubUser {
    private String login;
    private String id;
    private String bio;
    private String email;
    private String avatar_url;
}
