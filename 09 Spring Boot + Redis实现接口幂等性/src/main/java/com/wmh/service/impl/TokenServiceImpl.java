package com.wmh.service.impl;

import com.wmh.execption.BusinessException;
import com.wmh.service.TokenService;
import com.wmh.utils.ApiResult;
import com.wmh.utils.Constant;
import com.wmh.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @author: create by wangmh
 * @projectName: 09 Spring Boot + Redis实现接口幂等性
 * @packageName: com.wmh.service
 * @description:
 * @date: 2020/11/16
 **/
@Service("tokenService")
public class TokenServiceImpl implements TokenService {

    @Autowired
    RedisUtil redisUtil;

    /***
     * 创建token
     * @return
     */
    @Override
    public String createToken() {
        String str = UUID.randomUUID().toString().replace("-", "");
        StringBuilder token = new StringBuilder();
        try {
            token.append(Constant.TOKEN_PREFIX).append(str);
            redisUtil.set(token.toString(), token.toString(), 10000L);
            if (!StringUtils.isEmpty(token.toString())) {
                return token.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * 校验token
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public boolean checkToken(HttpServletRequest request) throws Exception {
        String token = request.getHeader(Constant.ACCESS_TOKEN);
        if (StringUtils.isEmpty(token)) {// header中不存在token
            token = request.getParameter(Constant.ACCESS_TOKEN);
            if (StringUtils.isEmpty(token)) {// parameter中也不存在token
                throw new BusinessException(ApiResult.REPETITIVE_OPERATION);
            }
        }
        if (redisUtil.get(token) == null) {
            throw new BusinessException(ApiResult.REPETITIVE_OPERATION);
        }
        redisUtil.del(token);
        return true;
    }
}
