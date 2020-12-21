package org.example.mapper;

import org.apache.ibatis.annotations.*;
import org.example.entity.UserEntity;

/**
 * @author: create by wangmh
 * @projectName: 11_redis_cache_uniformity
 * @description:
 * @date: 2020/12/21
 **/
public interface UserMapper {
    @Insert("INSERT INTO USERS(NAME, AGE) VALUES(#{name}, #{age})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    int insert(UserEntity userEntity);

    @Update("UPDATE  USERS SET NAME=#{NAME}    WHERE ID=#{id}")
    int updateUser(@Param("NAME") String name, @Param("id") Long id);

    @Select("select * from users where id= #{id}")
    UserEntity getUser(@Param("id") Long id);
//
//    @Delete("delete from USERS where id=#{}")
//    int deleteUser(@Param("id") Long id);
}