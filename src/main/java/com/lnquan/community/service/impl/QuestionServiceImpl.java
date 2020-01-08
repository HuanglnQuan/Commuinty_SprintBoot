package com.lnquan.community.service.impl;

import com.lnquan.community.beans.Notification;
import com.lnquan.community.beans.Question;
import com.lnquan.community.beans.QuestionExample;
import com.lnquan.community.beans.User;
import com.lnquan.community.dao.CommentDao;
import com.lnquan.community.dao.NotificationDao;
import com.lnquan.community.dao.QuestionDao;
import com.lnquan.community.dao.UserDao;
import com.lnquan.community.dto.QuestionDTO;
import com.lnquan.community.enums.NotificationType;
import com.lnquan.community.exception.CustomizeException;
import com.lnquan.community.service.QuestionService;
import com.lnquan.community.utils.ExceptionStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private NotificationDao notificationDao;

    @Autowired
    private CommentDao commentDao;

    @Override
    public void addQuestion(Question question) {
        questionDao.insertSelective(question);
    }

    @Override
    public List<QuestionDTO> queryAllQuestions() {
        List<Question> questions = questionDao.selectByExample(new QuestionExample());
        List<QuestionDTO> res = new ArrayList<>();
        WarpQuestion(questions, res);
        return res;
    }

    private void WarpQuestion(List<Question> questions, List<QuestionDTO> res) {
        for (Question question : questions) {
            String id = question.getCreator();
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);

            questionDTO.setUser(userDao.queryByPrimaryKey(id));
            System.out.println(questionDTO.getUser());

            Date createDate = new Date(question.getGmtCreate());
            Date modifiedDate = new Date(question.getGmtModified());
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            questionDTO.setGmtCreateDate(sd.format(createDate));
            questionDTO.setGmtModifiedDate(sd.format(modifiedDate));
            res.add(questionDTO);
        }
    }

    @Override
    public void incViewCount(int id) {
        questionDao.incViewCount(id);
    }


    @Override
    public List<QuestionDTO> queryRelativeQuestions(String tag) {
        QuestionExample questionExample = new QuestionExample();
        String[] split = tag.split(",");
        List<Question> questions = new ArrayList<>();
        for (String s : split) {
            questionExample.clear();
            questionExample.createCriteria().andTagLike("%"+s+"%");
            List<Question> tmp = questionDao.selectByExample(questionExample);
            questions.addAll(tmp);
        }
        List<QuestionDTO> res = new ArrayList<>();
        int index = Math.min(10, res.size());
        WarpQuestion(questions.subList(0, index), res);
        return res;
    }

    @Override
    public QuestionDTO queryQuestionById(int id) {
        Question question = questionDao.queryByPrimaryKey(id);

        if (question == null){
            throw new CustomizeException(ExceptionStatus.QuestionNotFound);
        }

        User user = userDao.queryByPrimaryKey(question.getCreator());
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        questionDTO.setUser(user);
        questionDTO.setCreator(Integer.parseInt(question.getCreator()));
        Date createDate = new Date(question.getGmtCreate());
        Date modifiedDate = new Date(question.getGmtModified());
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        questionDTO.setGmtCreateDate(sd.format(createDate));
        questionDTO.setGmtModifiedDate(sd.format(modifiedDate));
        return questionDTO;
    }

    @Override
    public void updateQuestion(QuestionDTO questionDTO) {
        Question question = new Question();
        question.setId(questionDTO.getId());
        question.setType(questionDTO.getType());
        question.setTitle(questionDTO.getTitle());
        question.setTag(questionDTO.getTag());
        question.setDescription(questionDTO.getDescription());
        question.setGmtModified(questionDTO.getGmtModified());

        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andIdEqualTo(questionDTO.getId());
        int flag = questionDao.updateByExampleSelective(question, questionExample);
        if (flag != 1){
            throw new CustomizeException(ExceptionStatus.UpdateFailure);
        }
    }

    @Override
    public int getQuestionNum() {
        return (int)questionDao.countByExample(new QuestionExample());
    }

    @Transactional
    @Override
    public int incLikeCount(int id, int receiver, int notifier, int type) {
        Notification notification = new Notification();
        notification.setType(type+"");
        notification.setReceiver(receiver);
        notification.setNotifier(notifier);
        notification.setOuterId(id);
        notification.setGmtCreate(new Date().getTime());

        notificationDao.insertSelective(notification);
        if (type == NotificationType.LIKE_TO_QUESTION.getType()) {
            questionDao.incLikeCount(id);
            return questionDao.queryByPrimaryKey(id).getLikeCount();
        }
        else if (type == NotificationType.LIKE_TO_COMMENT.getType()) {
            commentDao.incLikeCount(id);
            return commentDao.selectById(id).getLikeCount();
        }else
            return 0;
    }

    @Override
    public int getUserQuestionNum(int id) {
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andIdEqualTo(id);
        return (int)questionDao.countByExample(questionExample);
    }

    @Override
    public List<QuestionDTO> queryQuestionPerPage(int curPage, int size) {
        int offset = Math.max(0, (curPage - 1) * size);

        List<Question> questions = questionDao.queryQuestionPerPage(offset, size);
        List<QuestionDTO> res = new ArrayList<>();
        WarpQuestion(questions, res);
        return res;
    }

    @Override
    public List<QuestionDTO> queryByUserAccountId(String accountId, int curPage, int size) {
        int offset = Math.max(0, (curPage - 1) * size);
        List<Question> questions = questionDao.queryByUserAccountId(accountId, offset, size);
        List<QuestionDTO> res = new ArrayList<>();
        WarpQuestion(questions, res);
        return res;
    }
}
