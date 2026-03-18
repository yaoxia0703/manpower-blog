package com.manpowergroup.springboot.springboot3web.system.application.assembler;

import com.manpowergroup.springboot.springboot3web.system.application.dto.menu.MenuSaveOrUpdateRequest;
import com.manpowergroup.springboot.springboot3web.system.application.vo.MenuTreeVo;
import com.manpowergroup.springboot.springboot3web.system.domain.model.menu.Menu;

/**
 * Menu のリクエストDTO ⇔ Entity 変換ユーティリティ
 * 目的：
 * - Service から normalize / default 値埋め を分離して読みやすくする
 * - DTO は「入力 + 検証」に専念し、変換はここで一元化する
 */
public final class MenuAssembler {

    private MenuAssembler() {
    }

    /**
     * 新規作成用：Request -> Entity
     *
     * @param req MenuSaveOrUpdateRequest 変換元のリクエストDTO
     * @return Menu 変換後のEntity
     */
    public static Menu toNewEntity(MenuSaveOrUpdateRequest req) {
        return Menu.builder()
                .parentId(req.parentId())
                .name(req.name())
                .path(req.path())
                .component(req.component())
                .permission(req.permission())
                .icon(req.icon())
                .type(req.type())
                .sort(defaultSort(req.sort()))
                .status(req.status())
                .build();
    }

    /**
     * 更新用：Request -> 既存Entityへ反映
     */
    public static void applyToExisting(MenuSaveOrUpdateRequest req, Menu existing) {
        existing.setParentId(req.parentId());
        existing.setName(req.name());
        existing.setPath(req.path());
        existing.setComponent(req.component());
        existing.setPermission(req.permission());
        existing.setIcon(req.icon());
        existing.setType(req.type());
        existing.setSort(defaultSort(req.sort()));
        existing.setStatus(req.status());
    }

    /**
     * 表示順：デフォルト値埋め
     */
    private static Integer defaultSort(Integer sort) {
        return sort != null ? sort : 999;
    }

    /**
     * Entity -> TreeVo
     *
     * @param menu Menu Entity 変換元のEntity
     * @return MenuTreeVo 変換後のVO
     */
    public static MenuTreeVo toTreeVo(Menu menu) {
        MenuTreeVo vo = new MenuTreeVo();
        vo.setId(menu.getId());
        vo.setParentId(menu.getParentId());
        vo.setName(menu.getName());
        vo.setPath(menu.getPath());
        vo.setComponent(menu.getComponent());
        vo.setIcon(menu.getIcon());
        vo.setType(menu.getType());
        vo.setSort(menu.getSort());
        vo.setStatus(menu.getStatus());
        return vo;
    }
}
