package com.kay.mapper;

import com.kay.entity.PermissionEntity;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface PermissionMapper {

    @Select(" select * from sys_permission ")
    List<PermissionEntity> findAllPermission();

}
