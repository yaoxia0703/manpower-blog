package com.manpowergroup.springboot.springboot3web.system.application.service;

import com.manpowergroup.springboot.springboot3web.system.domain.model.role.RoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * ロールメニュー関連テーブル 服务类
 * </p>
 *
 * @author YAOXIA
 * @since 2026-03-01
 */
public interface RoleAppMenuService extends IService<RoleMenu> {
    void  saveRoleMenu(Long roleId, Long[] menuIds);

}
