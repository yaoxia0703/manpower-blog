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
    /**
     * ロールに紐づくメニュー権限を保存する。
     *
     * <p>
     * 処理内容：
     * <ul>
     *     <li>指定されたロールIDに紐づく既存のロールメニュー関連を削除する</li>
     *     <li>新しいメニューIDリストをもとにロールメニュー関連を再登録する</li>
     * </ul>
     *
     * <p>
     * 注意事項：
     * <ul>
     *     <li>本処理はトランザクション管理下で実行される</li>
     *     <li>menuIds が null または空の場合はエラーとする</li>
     * </ul>
     *
     * @param roleId  対象ロールID
     * @param menuIds ロールに関連付けるメニューID配列
     */
    void  saveRoleMenu(Long roleId, Long[] menuIds);

}
