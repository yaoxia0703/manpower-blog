package com.manpowergroup.springboot.springboot3web.system.application.service.impl;

import com.manpowergroup.springboot.springboot3web.system.domain.model.menu.Menu;
import com.manpowergroup.springboot.springboot3web.system.infrastructure.mapper.menu.MenuMapper;
import com.manpowergroup.springboot.springboot3web.system.application.service.MenuAppService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * システムメニュー管理テーブル 服务实现类
 * </p>
 *
 * @author YAOXIA
 * @since 2026-03-01
 */
@Service
public class MenuAppServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuAppService {

}
