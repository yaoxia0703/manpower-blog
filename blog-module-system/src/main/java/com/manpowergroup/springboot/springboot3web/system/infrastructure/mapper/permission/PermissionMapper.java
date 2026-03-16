package com.manpowergroup.springboot.springboot3web.system.infrastructure.mapper.permission;

import com.manpowergroup.springboot.springboot3web.system.domain.model.permission.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 権限マスタ（MENU/BUTTON/API） Mapper 接口
 * </p>
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 指定ユーザーに紐づく権限コード一覧を取得する
     *
     * @param userId ユーザーID
     * @return 権限コード一覧
     */
    List<String> selectPermissionCodesByUserId(@Param("userId") Long userId);

}
