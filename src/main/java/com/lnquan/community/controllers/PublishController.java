package com.lnquan.community.controllers;

import com.lnquan.community.beans.Question;
import com.lnquan.community.beans.User;
import com.lnquan.community.dto.QuestionDTO;
import com.lnquan.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish")
    public String publish(HttpServletRequest request, Model model){
        if(request.getSession().getAttribute("user") != null)
            return "publish";
        else{
            model.addAttribute("error", "用户未登录");
            return "index";
        }
    }

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") int id, Model model){
        QuestionDTO questionDTO = questionService.queryQuestionById(id);
        model.addAttribute("question", questionDTO);
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(@RequestParam("title") String title, @RequestParam("description") String description,
                            @RequestParam("tag") String tag, @RequestParam("type") String type,
                            @RequestParam(name = "id", required = false, defaultValue = "-1") Integer id,
                            HttpServletRequest request,
                            Model model){
        String userToken = "";
        User user = (User)request.getSession().getAttribute("user");
        if (user != null)
            userToken = user.getToken();
        else {
            model.addAttribute("error", "用户未登录");
            return "index";
        }

        if (id <= 0){
            Question question = new Question();
            question.setType(type);
            question.setCreator(user.getAccountId());
            question.setDescription(description);
            question.setTag(tag);
            question.setTitle(title);
            question.setGmtCreate(new Date().getTime());
            question.setGmtModified(new Date().getTime());
            questionService.addQuestion(question);
        }else{
            QuestionDTO questionDTO = questionService.queryQuestionById(id);
            questionDTO.setType(type);
            questionDTO.setDescription(description);
            questionDTO.setTag(tag);
            questionDTO.setTitle(title);
            questionDTO.setGmtModified(new Date().getTime());
            questionService.updateQuestion(questionDTO);
        }
        return "redirect:/";
    }
}
