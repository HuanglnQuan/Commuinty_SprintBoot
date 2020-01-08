package com.lnquan.community.utils;

public enum ExceptionStatus {
    UpdateFailure(1000, "更新问题失败，请重试！"),
    SUCCESS(200, "请求成功"),
    QuestionNotFound(1001, "问题不存在或已被删除"),
    USER_NOT_LOGIN(1002, "操作需要登录，请登录后重试"),
    TARGET_NOT_SPECIFIED(1003, "未指定问题或评论进行回复"),
    SYS_ERROR(1004, "服务器冒烟了！请稍后再试。。。"),
    ILLEGAL_COMMENT_TYPE(1005, "非法的评论类型"),
    COMMENT_NOT_FIND(1006, "操作的评论不存在");

    private int status;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private ExceptionStatus(int status, String message){
        this.status = status;
        this.message = message;
    }
}
