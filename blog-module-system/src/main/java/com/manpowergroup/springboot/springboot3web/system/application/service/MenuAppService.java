package com.manpowergroup.springboot.springboot3web.system.application.service;

import com.manpowergroup.springboot.springboot3web.system.application.dto.menu.MenuSaveOrUpdateRequest;
import com.manpowergroup.springboot.springboot3web.system.application.dto.menu.MenuStatusUpdateRequest;
import com.manpowergroup.springboot.springboot3web.system.application.vo.MenuTreeVo;
import com.manpowergroup.springboot.springboot3web.system.domain.model.menu.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * システムメニュー管理テーブル 服务类
 * </p>
 *
 * @author YAOXIA
 * @since 2026-03-01
 */
public interface MenuAppService extends IService<Menu> {

    /**
     * メニューをTree構造で取得する
     *
     * @return メニューのTree構造のリスト
     */
    List<MenuTreeVo> selectAllMenus();

    /**
     * ユーザIDに基づいてメニューをTree構造で取得する
     *
     * @param userId ユーザID
     * @return メニューのTree構造のリスト
     */
    List<MenuTreeVo> selectMenusByUserId(Long userId);

    /**
     * メニューを新規作成する
     *
     * @param request メニュー情報
     * @return 作成されたメニューID
     */
    Long createMenu(MenuSaveOrUpdateRequest request);

    /**
     * メニュー情報を更新する
     *
     * @param id      メニューID
     * @param request 更新内容
     */
    void updateMenu(Long id, MenuSaveOrUpdateRequest request);

    /**
     * メニューを削除する
     *
     * @param id メニューID
     */
    void deleteMenu(Long id);

    /**
     * メニューの状態を変更する
     *
     * @param id     メニューID
     * @param status 新しい状態
     */
    void changeMenuStatus(Long id, MenuStatusUpdateRequest status);


}
