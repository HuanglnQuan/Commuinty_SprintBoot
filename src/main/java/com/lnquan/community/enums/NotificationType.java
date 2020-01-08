package com.lnquan.community.enums;

import com.lnquan.community.beans.Notification;

public enum NotificationType {
    REPLY_TO_QUESTION(0),
    REPLY_TO_COMMENT(1),
    LIKE_TO_QUESTION(2),
    LIKE_TO_COMMENT(3);
    private int type;

    public int getType() {
        return type;
    }

    private NotificationType(int type){
        this.type = type;
    }
}
