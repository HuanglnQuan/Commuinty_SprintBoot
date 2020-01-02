package com.lnquan.community.dao;

import com.lnquan.community.beans.Question;
import org.springframework.stereotype.Component;

@Component
public interface QuestionDao {
    void addQuestion(Question question);
}
