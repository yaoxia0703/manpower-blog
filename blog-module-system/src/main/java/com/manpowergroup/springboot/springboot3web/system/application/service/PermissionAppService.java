package com.manpowergroup.springboot.springboot3web.system.application.service;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.JoinPageResult;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.PageRequest;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.Status;
import com.manpowergroup.springboot.springboot3web.system.application.dto.permission.PermissionQueryRequest;
import com.manpowergroup.springboot.springboot3web.system.application.dto.permission.PermissionSaveOrUpdateRequest;
import com.manpowergroup.springboot.springboot3web.system.domain.model.permission.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 権限マスタ（MENU/BUTTON/API） 服务类
 * </p>
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
public interface PermissionAppService extends IService<Permission> {

    /**
     * ユーザーIDに紐づく権限コード一覧を取得する
     *
     * @param userId ユーザーID
     * @return 権限コード一覧
     */
    List<String> selectPermissionCodesByUserId(Long userId);

    /**
     * 権限一覧をページングで取得する
     *
     * @param queryRequest 検索条件
     * @param pageRequest ページ情報
     * @return 権限一覧（ページング）
     */
    JoinPageResult<Permission> pagePermission(PermissionQueryRequest queryRequest, PageRequest pageRequest);

    /**
     * 権限IDにより権限情報を取得する
     *
     * @param id 権限ID
     * @return 権限情報
     */
    Permission getPermissionById(Long id);

    /**
     * 権限を新規作成する
     *
     * @param request 権限作成リクエスト
     * @return 作成された権限ID
     */
    Long createPermission(PermissionSaveOrUpdateRequest request);

    /**
     * 権限情報を更新する
     *
     * @param id 権限ID
     * @param request 更新内容
     */
    void updatePermission(Long id, PermissionSaveOrUpdateRequest request);

    /**
     * 権限を削除する
     *
     * @param id 権限ID
     */
    void deletePermission(Long id);

    /**
     * 権限の状態を変更する
     *
     * @param id 権限ID
     * @param status 状態（有効 / 無効）
     */
    void changeStatus(Long id, Status status);

}
