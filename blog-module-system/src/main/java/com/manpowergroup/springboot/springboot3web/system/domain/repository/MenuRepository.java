package com.manpowergroup.springboot.springboot3web.system.domain.repository;

import com.manpowergroup.springboot.springboot3web.system.domain.model.menu.Menu;

import java.util.List;

public interface MenuRepository {
    /**
     * ユーザIDに基づいてメニューを選択する
     *
     * @param userId ユーザID
     * @return ユーザIDに基づいて選択されたメニューのリスト
     */
    List<Menu> selectMenusByUserId(Long userId);

    /**
     * 同一階層におけるメニュー名称の存在有無を取得する
     *
     * @param parentId 親メニューID
     * @param name     メニュー名称
     * @return 件数（0: 存在しない / 1以上: 存在する）
     */
    int existsByParentIdAndName(Long parentId, String name);

    /**
     * 指定した親メニューIDに紐づく子メニューの件数を取得する
     *
     * @param parentId 親メニューID
     * @return 子メニュー件数
     */
    int countByParentId(Long parentId);


    /**
     * 同一階層におけるメニュー名称の重複件数を取得する（自身を除く）
     *
     * @param parentId 親メニューID
     * @param name     メニュー名称
     * @param id       除外対象のメニューID（自身）
     * @return 重複件数
     */
    int countByParentIdAndNameExcludeId(Long parentId, String name, Long id);

    /**
     * 指定したパスのメニュー件数を取得する
     *
     * @param path フロントエンドのルートパス
     * @return 件数
     */
    int countByPath(String path);


    /**
     * 指定したパスのメニュー件数を取得する（自身を除く）
     *
     * @param path フロントエンドのルートパス
     * @param id   除外対象のメニューID（自身）
     * @return 件数
     */
    int countByPathExcludeId(String path, Long id);
}
