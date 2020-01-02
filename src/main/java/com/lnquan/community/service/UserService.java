package com.lnquan.community.service;

import com.lnquan.community.beans.User;

public interface UserService {
    void addUser(User user);
    User queryByToken(String token);
}
