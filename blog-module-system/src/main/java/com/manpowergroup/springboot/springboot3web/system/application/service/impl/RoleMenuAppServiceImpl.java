package com.manpowergroup.springboot.springboot3web.system.application.service.impl;

import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;
import com.manpowergroup.springboot.springboot3web.blog.common.exception.BizException;
import com.manpowergroup.springboot.springboot3web.system.domain.model.role.RoleMenu;
import com.manpowergroup.springboot.springboot3web.system.infrastructure.mapper.role.RoleMenuMapper;
import com.manpowergroup.springboot.springboot3web.system.application.service.RoleAppMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * ロールメニュー関連テーブル 服务实现类
 * </p>
 *
 * @author YAOXIA
 * @since 2026-03-01
 */
@Service
public class RoleMenuAppServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleAppMenuService {

    @Override
    public void saveRoleMenu(Long roleId, Long[] menuIds) {
        if(roleId==null){
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "ロールIDが指定されていません。");
        }
    }
}
