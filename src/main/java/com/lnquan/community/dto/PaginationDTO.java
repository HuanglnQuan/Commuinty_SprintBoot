package com.lnquan.community.dto;

import com.lnquan.community.dto.QuestionDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationDTO {
    private List<QuestionDTO> questions;
    private List<NotificationDTO> notifications;
    private boolean showPrevious = true;
    private boolean showNext = true;
    private boolean showFirst = true;
    private boolean showLast = true;
    private String search;
    private int curPage;
    private List<Integer> pages;
    private int pageCount;
    private static int btnSize = 5;


    public void setPageInfo(Integer curPage, int pageCount) {
        this.pageCount = pageCount;
        curPage = Math.min(curPage, pageCount);
        this.curPage = curPage;
        this.pages = new ArrayList<>();
        int count = 0;
        if (curPage == 1){
            this.showPrevious = false;
            if(curPage == pageCount)
                this.showNext = false;
            while (count < btnSize){
                if (curPage+count > pageCount)
                    break;
                pages.add(curPage+count);
                process(curPage + count, pageCount);
                count++;
            }
        }else if (curPage == pageCount){
            this.showNext = false;
            int left = Math.max(1, curPage-btnSize);
            for (int i=left;i<=curPage;i++){
                pages.add(i);
                process(i, pageCount);
            }
        }else{
            count = btnSize / 2;
            int left = Math.max(1, curPage-count);
            int right = Math.min(pageCount, curPage+count);
            for (int i=left;i<=right;i++){
                pages.add(i);
                process(i, pageCount);
            }
        }
    }

    private void process(Integer tmp, int pageCount) {
        if (tmp == 1){
            showFirst = false;
        }
        if (tmp == pageCount){
            showLast = false;
        }
    }

}
