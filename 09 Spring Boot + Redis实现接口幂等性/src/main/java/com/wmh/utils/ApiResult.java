package com.wmh.utils;

public enum ApiResult {
    /**
     * 自定义异常
     */
    UNAUTHZ(506,"权限不足"),
    REPETITIVE_OPERATION(801,"重复性操作"),
    ;


    private Integer errno;

    private String errmsg;

    ApiResult(Integer errno, String errmsg) {
        this.errno = errno;
        this.errmsg = errmsg;
    }

    public Integer getErrno() {
        return errno;
    }

    public void setErrno(Integer errno) {
        this.errno = errno;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

}