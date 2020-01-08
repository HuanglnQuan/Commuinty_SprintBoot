package com.lnquan.community.service.impl;

import com.lnquan.community.beans.User;
import com.lnquan.community.beans.UserExample;
import com.lnquan.community.dao.UserDao;
import com.lnquan.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public void addUser(User user) {

        User tmp = userDao.queryByAccountId(user.getAccountId());

        if (tmp == null) {
            userDao.insert(user);
        }
        else{
            long time = new Date().getTime();
            User updateUser = new User();
            updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());
            updateUser.setGmtModified(time);
            updateUser.setBio(user.getBio());
            updateUser.setAvatarUrl(user.getAvatarUrl());

            UserExample userExample = new UserExample();
            userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());

            userDao.updateByExampleSelective(updateUser, userExample);
        }
    }

    @Override
    public User queryByToken(String token) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andTokenEqualTo(token);
        return userDao.selectByExample(userExample).get(0);
    }
}
