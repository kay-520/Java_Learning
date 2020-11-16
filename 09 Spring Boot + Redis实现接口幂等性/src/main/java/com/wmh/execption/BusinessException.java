package com.wmh.execption;

import com.wmh.utils.ApiResult;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: create by wangmh
 * @projectName: 09 Spring Boot + Redis实现接口幂等性
 * @packageName: com.wmh.execption
 * @description:
 * @date: 2020/11/16
 **/
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = -9138712164205129842L;

    private ApiResult apiResult;

    public BusinessException(ApiResult apiResult) {
        this.apiResult = apiResult;
    }

    public BusinessException(String message, ApiResult apiResult) {
        super(message);
        this.apiResult = apiResult;
    }

    public BusinessException(String message, Throwable cause, ApiResult apiResult) {
        super(message, cause);
        this.apiResult = apiResult;
    }

    public BusinessException(Throwable cause, ApiResult apiResult) {
        super(cause);
        this.apiResult = apiResult;
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ApiResult apiResult) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.apiResult = apiResult;
    }

    public ApiResult getApiResult() {
        return apiResult;
    }

    public void setApiResult(ApiResult apiResult) {
        this.apiResult = apiResult;
    }

    public Map<String, Object> getResultMap() {
        Map<String, Object> result = new HashMap<>(2);
        result.put("errno", apiResult.getErrno());
        result.put("errmsg", apiResult.getErrmsg());
        return result;
    }

    @Override
    public String toString() {
        return "BusinessException{" +
                "apiResult=" + apiResult.toString() +
                '}';
    }
}
