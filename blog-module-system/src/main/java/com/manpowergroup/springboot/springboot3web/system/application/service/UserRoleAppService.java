package com.manpowergroup.springboot.springboot3web.system.application.service;

import com.manpowergroup.springboot.springboot3web.system.domain.model.user.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * ユーザー・ロール紐付け 服务类
 * </p>
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
public interface UserRoleAppService extends IService<UserRole> {

    /**
     * ユーザーに紐づくロール関連を保存または更新する
     *
     * @param userId 対象ユーザーID
     * @param roleIds ユーザーに割り当てるロールID配列
     */
    void saveOrUpdate(Long userId, Long[] roleIds);

}
