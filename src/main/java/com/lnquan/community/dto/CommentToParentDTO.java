package com.lnquan.community.dto;

import com.lnquan.community.beans.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentToParentDTO {
    private int id;
    private int parentId;
    private String avatarUrl;
    private String userName;
    private String content;
    private int likeCount;
    private int commentCount;
    private String time;
    private int creator;
}
