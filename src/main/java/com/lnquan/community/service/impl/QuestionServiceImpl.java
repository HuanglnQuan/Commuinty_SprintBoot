package com.lnquan.community.service.impl;

import com.lnquan.community.beans.Question;
import com.lnquan.community.dao.QuestionDao;
import com.lnquan.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionDao questionDao;

    @Override
    public void addQuestion(Question question) {
        questionDao.addQuestion(question);
    }
}
