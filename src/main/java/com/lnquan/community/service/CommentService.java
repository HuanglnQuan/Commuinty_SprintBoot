package com.lnquan.community.service;

import com.lnquan.community.beans.User;
import com.lnquan.community.dto.CommentDTO;
import com.lnquan.community.dto.CommentToParentDTO;

import java.util.List;

public interface CommentService {
    void insert(CommentDTO commentDTO, int id);
    List<CommentToParentDTO> queryAllCommentsByQuestionId(int parentId, int type);
}
