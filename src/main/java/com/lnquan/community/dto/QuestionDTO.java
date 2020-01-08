package com.lnquan.community.dto;

import com.lnquan.community.beans.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    private int id;
    private String title;
    private String description;
    private String gmtCreateDate;
    private String gmtModifiedDate;
    private int creator;
    private int commentCount;
    private int viewCount;
    private int likeCount;
    private String tag;
    private String type;
    private long gmtModified;
    private User user;
}
