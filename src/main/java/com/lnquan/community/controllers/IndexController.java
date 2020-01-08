package com.lnquan.community.controllers;

import com.lnquan.community.beans.User;
import com.lnquan.community.dao.NotificationDao;
import com.lnquan.community.dto.PaginationDTO;
import com.lnquan.community.dto.QuestionDTO;
import com.lnquan.community.service.NotificationService;
import com.lnquan.community.service.QuestionService;
import com.lnquan.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    private String token = "token";

    @RequestMapping(value = "/")
    public String goIndex(HttpServletRequest request, Model model,
                          @RequestParam(value = "page", defaultValue = "1") Integer curPage,
                          @RequestParam(value = "size", defaultValue = "5") Integer size){

        int questionCount = questionService.getQuestionNum();
        int pageCount = (questionCount % size == 0) ? questionCount / size : (questionCount / size) + 1;
        curPage = Math.max(1, curPage);
        curPage = Math.min(curPage, pageCount);
        List<QuestionDTO> questionDTOList = questionService.queryQuestionPerPage(curPage, size);

        PaginationDTO pagination = new PaginationDTO();
        pagination.setQuestions(questionDTOList);

        pagination.setPageInfo(curPage, pageCount);
        model.addAttribute("pagination", pagination);

        User user = (User)request.getSession().getAttribute("user");
        if (user != null) {
            int notificationNum = notificationService.getUnReadNotificationNum(user.getId());
            request.getSession().setAttribute("notificationNum", notificationNum);
        }
        return "index";
    }


    @RequestMapping(value = "/logout")
    public String logOut(HttpServletRequest request, HttpServletResponse response){
        request.getSession().removeAttribute("user");
        Cookie token = new Cookie("token", null);
        token.setMaxAge(0);
        response.addCookie(token);
        return "redirect:/";
    }
}
