package com.lnquan.community.service.impl;

import com.lnquan.community.beans.*;
import com.lnquan.community.dao.CommentDao;
import com.lnquan.community.dao.NotificationDao;
import com.lnquan.community.dao.QuestionDao;
import com.lnquan.community.dao.UserDao;
import com.lnquan.community.dto.CommentDTO;
import com.lnquan.community.dto.CommentToParentDTO;
import com.lnquan.community.enums.CommentType;
import com.lnquan.community.enums.NotificationType;
import com.lnquan.community.exception.CustomizeException;
import com.lnquan.community.service.CommentService;
import com.lnquan.community.utils.ExceptionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private NotificationDao notificationDao;

    @Override
    public List<CommentToParentDTO> queryAllCommentsByQuestionId(int parentId, int type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andTypeEqualTo(type).andParentIdEqualTo(parentId);
        List<Comment> comments = commentDao.selectByPIdAndType(parentId, type);
        List<CommentToParentDTO> res = new ArrayList<>();
        WarpComment(parentId, comments, res);
        return res;
    }

    private void WarpComment(int parentId, List<Comment> comments, List<CommentToParentDTO> res) {

        for (Comment comment : comments) {
            User user = userDao.queryByPrimaryKey(comment.getCommentor());
            CommentToParentDTO dto = new CommentToParentDTO();
            dto.setId(comment.getId());
            dto.setAvatarUrl(user.getAvatarUrl());
            dto.setCommentCount(comment.getCommentCount());
            dto.setContent(comment.getContent());
            dto.setLikeCount(comment.getLikeCount());
            dto.setParentId(parentId);
            dto.setUserName(user.getName());
            dto.setCreator(user.getId());
            Date modifiedDate = new Date(comment.getGmtModified());
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            dto.setTime(sd.format(modifiedDate));
            res.add(dto);
        }
    }

    @Transactional
    @Override
    public void insert(CommentDTO commentDTO, int id) {
        Comment comment = new Comment();
        comment.setCommentor(id+"");
        comment.setContent(commentDTO.getContent());
        comment.setParentId(commentDTO.getParentId());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(new Date().getTime());
        comment.setGmtModified(new Date().getTime());

        if (commentDTO.getParentId() <= 0){
            throw new CustomizeException(ExceptionStatus.TARGET_NOT_SPECIFIED);
        }
        if (!CommentType.isExist(commentDTO.getType())){
            throw new CustomizeException(ExceptionStatus.ILLEGAL_COMMENT_TYPE);
        }

        Notification notification = new Notification();
        notification.setGmtCreate(new Date().getTime());
        notification.setNotifier(id);
        notification.setOuterId(commentDTO.getParentId());

        if (commentDTO.getType() == CommentType.COMMENT.getType()){
            Comment dbComment = commentDao.selectById(comment.getParentId());
            if (dbComment == null){
                throw new CustomizeException(ExceptionStatus.COMMENT_NOT_FIND);
            }else {
                notification.setReceiver(Integer.parseInt(dbComment.getCommentor()));
                notification.setType(NotificationType.REPLY_TO_COMMENT.getType()+"");

                commentDao.insertSelective(comment);
                commentDao.incCommentCount(commentDTO.getParentId());
            }
        }else if (commentDTO.getType() == CommentType.QUESTION.getType()){
            Question question = questionDao.queryByPrimaryKey(commentDTO.getParentId());
            if (question == null){
                throw new CustomizeException(ExceptionStatus.QuestionNotFound);
            }else {
                notification.setReceiver(Integer.parseInt(question.getCreator()));
                notification.setType(NotificationType.REPLY_TO_QUESTION.getType()+"");

                commentDao.insertSelective(comment);
                questionDao.incCommentCount(commentDTO.getParentId());
            }
        }
        notificationDao.insertSelective(notification);
    }
}
