package com.lnquan.community.controllers;

import com.lnquan.community.beans.User;
import com.lnquan.community.dto.NotificationDTO;
import com.lnquan.community.dto.PaginationDTO;
import com.lnquan.community.dto.QuestionDTO;
import com.lnquan.community.service.NotificationService;
import com.lnquan.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "/profile/{action}")
    public String myQuestion(@PathVariable(name = "action") String action,
                             HttpServletRequest request,
                             Model model,
                             @RequestParam(value = "page", defaultValue = "1") Integer curPage,
                             @RequestParam(value = "size", defaultValue = "5") Integer size){

        User user = (User)request.getSession().getAttribute("user");

        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "index";
        }

        if ("questions".equals(action)){
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我发布的问题");

            int questionCount = questionService.getUserQuestionNum(user.getId());
            int pageCount = (questionCount % size == 0) ? questionCount / size : (questionCount / size) + 1;
            curPage = Math.max(1, curPage);
            curPage = Math.min(curPage, pageCount);

            List<QuestionDTO> questionDTOList = questionService.queryByUserAccountId(user.getAccountId(), curPage, size);
            PaginationDTO pagination = new PaginationDTO();
            pagination.setPageInfo(curPage, pageCount);
            pagination.setQuestions(questionDTOList);

            model.addAttribute("pagination", pagination);
        }else if ("replies".equals(action)){
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");

            int replyCount = notificationService.getNotificationNum(user.getId());
            int pageCount = (replyCount % size == 0) ? replyCount / size : (replyCount / size) + 1;
            curPage = Math.max(1, curPage);
            curPage = Math.min(curPage, pageCount);
            List<NotificationDTO> notificationDTOS = notificationService.queryByUserIdAndTypePerPage(user.getId(), curPage, size);

            PaginationDTO pagination = new PaginationDTO();
            pagination.setPageInfo(curPage, pageCount);
            pagination.setNotifications(notificationDTOS);
            model.addAttribute("pagination", pagination);

        }
        return "profile";
    }
}
