package com.lnquan.community.controllers;

import com.lnquan.community.beans.User;
import com.lnquan.community.dto.CommentDTO;
import com.lnquan.community.dto.CommentToParentDTO;
import com.lnquan.community.dto.ResultDTO;
import com.lnquan.community.enums.CommentType;
import com.lnquan.community.exception.CustomizeException;
import com.lnquan.community.service.CommentService;
import com.lnquan.community.utils.ExceptionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/comment")
    @ResponseBody
    public ResultDTO doComment(@RequestBody CommentDTO commentDTO, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");

        if (user == null){
            return new ResultDTO().errorOf(ExceptionStatus.USER_NOT_LOGIN);
        }
        try {
            commentService.insert(commentDTO, user.getId());
        }catch (Exception e){
            return new ResultDTO().errorOf((CustomizeException) e);
        }
        return new ResultDTO().success();
    }

    @RequestMapping("/comment/{id}")
    @ResponseBody
    public ResultDTO getComments(@PathVariable(name = "id") int id,
                                 HttpServletRequest request){

        User user = (User) request.getSession().getAttribute("user");

        if (user == null){
            return new ResultDTO().errorOf(ExceptionStatus.USER_NOT_LOGIN);
        }

        List<CommentToParentDTO> commentToParentDTOS = commentService.queryAllCommentsByQuestionId(id, CommentType.COMMENT.getType());

        return new ResultDTO().success(commentToParentDTOS);
    }
}
