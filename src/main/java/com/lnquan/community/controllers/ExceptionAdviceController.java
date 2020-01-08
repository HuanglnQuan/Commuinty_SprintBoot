package com.lnquan.community.controllers;

import com.alibaba.fastjson.JSON;
import com.lnquan.community.dto.ResultDTO;
import com.lnquan.community.exception.CustomizeException;
import com.lnquan.community.utils.ExceptionStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class ExceptionAdviceController {
    @ExceptionHandler
    public ModelAndView handler(Throwable e, Model model, HttpServletResponse response,
                                HttpServletRequest request){
        ResultDTO resultDTO;
        e.printStackTrace();
        if ("application/json".equals(request.getContentType())){
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setStatus(200);
            if (e instanceof CustomizeException){
                resultDTO = new ResultDTO().errorOf((CustomizeException) e);
            }else {
                resultDTO = new ResultDTO().errorOf(ExceptionStatus.SYS_ERROR);
            }
            try {
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
                return null;
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
        }else {
            if (e instanceof CustomizeException){
                model.addAttribute("message", e.getMessage());
                return new ModelAndView("error");
            }else {
                model.addAttribute("message", ExceptionStatus.SYS_ERROR.getMessage());
                return new ModelAndView("error");
            }
        }



    }
}
