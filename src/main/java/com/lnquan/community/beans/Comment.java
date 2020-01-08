package com.lnquan.community.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private Integer id;
    private Integer parentId;
    private Integer type;
    private String commentor;
    private Integer likeCount;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer commentCount;
    private String content;
}
