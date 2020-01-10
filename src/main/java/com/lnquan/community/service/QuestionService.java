package com.lnquan.community.service;

import com.lnquan.community.beans.Question;
import com.lnquan.community.dto.QuestionDTO;

import java.util.List;

public interface QuestionService {

    int getQuestionNumBySearch(String condition);

    List<QuestionDTO> queryRelativeQuestions(String tag);

    void addQuestion(Question question);

    List<QuestionDTO> queryAllQuestions();

    QuestionDTO queryQuestionById(int id);

    int getQuestionNum();

    int getUserQuestionNum(int id);

    List<QuestionDTO> queryQuestionPerPage(String search, int curPage, int size);

    List<QuestionDTO> queryByUserAccountId(String accountId, int offset, int size);

    void updateQuestion(QuestionDTO question);

    void incViewCount(int id);

    int incLikeCount(int id, int receiver, int notifier, int type);
}
