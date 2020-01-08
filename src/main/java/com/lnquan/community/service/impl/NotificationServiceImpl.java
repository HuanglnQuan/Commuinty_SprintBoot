package com.lnquan.community.service.impl;

import com.lnquan.community.beans.*;
import com.lnquan.community.dao.CommentDao;
import com.lnquan.community.dao.NotificationDao;
import com.lnquan.community.dao.QuestionDao;
import com.lnquan.community.dao.UserDao;
import com.lnquan.community.dto.NotificationDTO;
import com.lnquan.community.enums.CommentType;
import com.lnquan.community.enums.NotificationType;
import com.lnquan.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationDao notificationDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private QuestionDao questionDao;

    @Override
    public int getUnReadNotificationNum(int id) {
        return notificationDao.getUnReadNotificationNum(id);
    }

    @Override
    public void updateNotificationStatus(int id) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andIdEqualTo(id);
        Notification notification = new Notification();
        notification.setStatus("1");
        notificationDao.updateByExampleSelective(notification, notificationExample);
    }

    @Override
    public int getNotificationNum(int id) {
        return notificationDao.getNotificationNum(id);
    }

    @Override
    public List<NotificationDTO> queryByUserIdAndTypePerPage(int id, int curPage, int size) {
        int offset = (curPage - 1) * size;
        offset = Math.max(0, offset);
        List<Notification> notifications = notificationDao.queryByUserIdAndTypePerPage(id, offset, size);
        List<NotificationDTO> res = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDTO tmp = new NotificationDTO();
            tmp.setId(notification.getId());
            tmp.setNotificationType(notification.getType());
            tmp.setStatus(Integer.parseInt(notification.getStatus()));

            Date createDate = new Date(notification.getGmtCreate());
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            tmp.setTime(sd.format(createDate));

            User notifier = userDao.queryByPrimaryKey(notification.getNotifier()+"");

            tmp.setNotifier(notifier);

            if (notification.getType().equals(NotificationType.REPLY_TO_QUESTION.getType()+"")
                || notification.getType().equals(NotificationType.LIKE_TO_QUESTION.getType()+"")){
                Question question = questionDao.queryByPrimaryKey(notification.getOuterId());
                if (question == null)
                    continue;
                tmp.setOuterId(question.getId());
                if (question.getTitle().length() < 45)
                    tmp.setTitle(question.getTitle());
                else if (question.getTitle().length() >= 45)
                    tmp.setTitle(question.getTitle() + "...");
            }else if (notification.getType().equals(NotificationType.REPLY_TO_COMMENT.getType()+"")
                    || notification.getType().equals(NotificationType.LIKE_TO_COMMENT.getType()+"")){
                Comment comment = commentDao.selectById(notification.getOuterId());
                if (comment == null)
                    continue;
                if (comment.getType() == CommentType.QUESTION.getType())
                    tmp.setOuterId(comment.getParentId());
                else if (comment.getType() == CommentType.COMMENT.getType()){
                    Comment comment1 = commentDao.selectById(comment.getParentId());
                    tmp.setOuterId(comment1.getParentId());
                }
                if (comment.getContent().length() < 45)
                    tmp.setTitle(comment.getContent());
                else if (comment.getContent().length() >= 45)
                    tmp.setTitle(comment.getContent() + "...");
            }

            res.add(tmp);
        }

        return res;
    }
}
