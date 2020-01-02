package com.lnquan.community.dao;

import com.lnquan.community.beans.User;
import org.springframework.stereotype.Component;

@Component
public interface UserDao {
    void addUser(User user);
    User queryByToken(String token);
}
