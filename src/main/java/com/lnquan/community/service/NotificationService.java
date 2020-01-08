package com.lnquan.community.service;

import com.lnquan.community.dto.NotificationDTO;

import java.util.List;

public interface NotificationService {
    int getUnReadNotificationNum(int id);
    int getNotificationNum(int id);
    List<NotificationDTO> queryByUserIdAndTypePerPage(int id, int curPage, int size);
    void updateNotificationStatus(int id);
}
