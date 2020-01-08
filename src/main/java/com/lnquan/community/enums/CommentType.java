package com.lnquan.community.enums;

public enum CommentType {
    COMMENT(1),
    QUESTION(2);

    private int type;

    public int getType() {
        return type;
    }

    private CommentType(int type){
        this.type = type;
    }

    public static boolean isExist(int type){
        for (CommentType value : CommentType.values()) {
            if (value.getType() == type)
                return true;
        }
        return false;
    }
}
