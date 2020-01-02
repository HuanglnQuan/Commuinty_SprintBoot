package com.lnquan.community.service.impl;

import com.lnquan.community.beans.User;
import com.lnquan.community.dao.UserDao;
import com.lnquan.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public void addUser(User user) {
        userDao.addUser(user);
    }

    @Override
    public User queryByToken(String token) {
        return userDao.queryByToken(token);
    }
}
