package com.manpowergroup.springboot.springboot3web.system.application.service;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.JoinPageResult;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.PageRequest;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.Status;
import com.manpowergroup.springboot.springboot3web.system.application.dto.role.RoleQueryRequest;
import com.manpowergroup.springboot.springboot3web.system.application.dto.role.RoleSaveOrUpdateRequest;
import com.manpowergroup.springboot.springboot3web.system.domain.model.role.Role;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * ロールマスタ 服务类
 * </p>
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
public interface RoleAppService extends IService<Role> {
    /**
     * ロール一覧をページングで取得する
     *
     * @param pageRequest ページ情報
     * @param query 検索条件
     * @return ロール一覧（ページング）
     */
    JoinPageResult<Role> pageRoles(PageRequest pageRequest, RoleQueryRequest query);

    /**
     * ロールIDによりロール情報を取得する
     *
     * @param id ロールID
     * @return ロール情報
     */
    Role getRoleById(Long id);

    /**
     * ロールを新規作成する
     *
     * @param request ロール作成リクエスト
     * @return 作成されたロールID
     */
    Long createRole(RoleSaveOrUpdateRequest request);

    /**
     * ロール情報を更新する
     *
     * @param id ロールID
     * @param request 更新内容
     */
    void updateRole(Long id, RoleSaveOrUpdateRequest request);

    /**
     * ロールを削除する
     *
     * @param id ロールID
     */
    void deleteRole(Long id);

    /**
     * ロールの状態を変更する
     *
     * @param id ロールID
     * @param status 状態（有効 / 無効）
     */
    void changeStatus(Long id, Status status);

}
