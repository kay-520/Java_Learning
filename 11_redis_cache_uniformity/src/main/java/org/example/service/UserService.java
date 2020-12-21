package org.example.service;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.example.entity.UserEntity;
import org.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: create by wangmh
 * @projectName: 11_redis_cache_uniformity
 * @description: 基于直接删除redis缓存（适用于小型项目）
 * @date: 2020/12/21
 **/
@RestController
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * insert
     *
     * @param userEntity
     * @return
     */
    @PostMapping("/insert")
    public String updateUser(@RequestBody UserEntity userEntity) {
        int result = userMapper.insert(userEntity);
        if (result <= 0) {
            return "fail";
        }
        // 存放到缓存中
        redisTemplate.opsForValue().set(userEntity.getId() + "", JSONObject.toJSONString(userEntity));
        return "ok";
    }

    /**
     * 查询数据
     *
     * @param id
     * @return
     */
    @RequestMapping("/getUser")
    public Object getUser(Long id) {
        String json = redisTemplate.opsForValue().get(id + "");
        if (!StringUtils.isEmpty(json)) {
            return ResponseEntity.status(200).body(json);
        }
        // 未查询到数据，则查询db
        UserEntity user = userMapper.getUser(id);
        if (user == null) {
            return ResponseEntity.status(500).body("db不存在该数据");
        }
        // 在将数据同步到Redis中
        redisTemplate.opsForValue().set(id + "", JSONObject.toJSONString(user));
        return ResponseEntity.status(200).body(user);
    }

    /**
     * 更新
     *
     * @param userId
     * @param name
     * @return
     */
    @RequestMapping("/updateUser")
    public String updateUser(Long userId, String name) {
        int result = userMapper.updateUser(name, userId);
        if (result <= 0) {
            return "fail";
        }
        redisTemplate.delete(userId + "");
//        redisTemplate.opsForValue().set(userId, );
        return "ok";
    }
}
