package com.lnquan.community.controllers;

import com.lnquan.community.dto.FileDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FileController {

    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO fileUpload(){
        FileDTO fileDTO = new FileDTO();
        fileDTO.setMessage("上传成功");
        fileDTO.setSuccess(1);
        fileDTO.setUrl("/images/Dayida.jpg");
        return fileDTO;
    }
}
