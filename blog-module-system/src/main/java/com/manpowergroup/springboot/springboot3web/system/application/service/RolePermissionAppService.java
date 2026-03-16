package com.manpowergroup.springboot.springboot3web.system.application.service;

import com.manpowergroup.springboot.springboot3web.system.domain.model.role.RolePermission;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * ロール・権限紐付け 服务类
 * </p>
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
public interface RolePermissionAppService extends IService<RolePermission> {

    /**
     * ロールに紐づく権限情報を保存または更新する
     *
     * <p>
     * 指定されたロールIDに対して既存の権限関連を更新し、
     * 新しい権限IDリストをもとにロール権限関連を再登録する。
     * </p>
     *
     * @param roleId ロールID
     * @param permissionIds 権限ID配列
     */
    void saveOrUpdate(Long roleId, Long[] permissionIds);

}
