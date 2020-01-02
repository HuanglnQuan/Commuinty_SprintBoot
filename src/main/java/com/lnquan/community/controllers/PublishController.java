package com.lnquan.community.controllers;

import com.lnquan.community.beans.Question;
import com.lnquan.community.beans.User;
import com.lnquan.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
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

    @PostMapping("/publish")
    public String doPublish(@RequestParam("title") String title, @RequestParam("description") String description,
                            @RequestParam("tag") String tag,
                            HttpServletRequest request,
                            Model model){
        int userId = -1;
        User user = (User)request.getSession().getAttribute("user");
        if (user != null)
            userId = user.getId();
        else {
            System.out.println("用户未登录");
            model.addAttribute("error", "用户未登录");
            return "publish";
        }
        Question question = new Question();
        question.setCreator(userId);
        question.setDescription(description);
        question.setTag(tag);
        question.setTitle(title);
        question.setGmtCreate(new Date().getTime());
        question.setGmtModified(new Date().getTime());

        questionService.addQuestion(question);

        return "redirect:/";
    }
}
