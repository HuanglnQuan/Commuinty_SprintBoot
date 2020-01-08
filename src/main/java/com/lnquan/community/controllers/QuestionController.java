package com.lnquan.community.controllers;

import com.lnquan.community.beans.User;
import com.lnquan.community.dto.CommentToParentDTO;
import com.lnquan.community.dto.QuestionDTO;
import com.lnquan.community.dto.ResultDTO;
import com.lnquan.community.enums.CommentType;
import com.lnquan.community.exception.CustomizeException;
import com.lnquan.community.service.CommentService;
import com.lnquan.community.service.QuestionService;
import com.lnquan.community.utils.ExceptionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "/question/{id}")
    public String editQuestion(@PathVariable(name = "id") Integer id, Model model,
                               HttpServletRequest request){
        QuestionDTO questionDTO = questionService.queryQuestionById(id);

        List<CommentToParentDTO> commentToParentDTOS = commentService.queryAllCommentsByQuestionId(id, CommentType.QUESTION.getType());
        questionService.incViewCount(id);
        List<QuestionDTO> relativeQuestions = questionService.queryRelativeQuestions(questionDTO.getTag());
        model.addAttribute("relatives", relativeQuestions);
        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", commentToParentDTOS);
        return "question";
    }

    @GetMapping(value = "/like/{id}/{receiver}/{type}")
    @ResponseBody
    public ResultDTO like(@PathVariable(name = "id") int id,
                       @PathVariable(name = "receiver") int receiver,
                       @PathVariable(name = "type") int type,
                       HttpServletRequest request){

        User user = (User) request.getSession().getAttribute("user");
        if (user == null)
            return new ResultDTO().errorOf(ExceptionStatus.USER_NOT_LOGIN);

        int likeCount = questionService.incLikeCount(id, receiver, user.getId(), type);
        return new ResultDTO<Integer>().success(likeCount);
    }
}
