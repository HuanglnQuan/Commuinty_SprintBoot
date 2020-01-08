package com.lnquan.community.service.impl;

import com.lnquan.community.dao.NotificationDao;
import com.lnquan.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationDao notificationDao;

    @Override
    public int getUnReadNotificationNum(int id) {
        return notificationDao.getUnReadNotificationNum(id);
    }
}
