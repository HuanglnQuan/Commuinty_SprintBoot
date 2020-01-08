package com.lnquan.community.dto;

import com.lnquan.community.beans.User;
import com.lnquan.community.enums.NotificationType;
import lombok.Data;

@Data
public class NotificationDTO {
    private User notifier;
    private String title;
    private String notificationType;
    private int id;
    private int outerId;
    private String time;
    private int status;
}
