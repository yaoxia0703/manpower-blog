package com.manpowergroup.springboot.springboot3web.system.infrastructure.mapper.menu;

import com.manpowergroup.springboot.springboot3web.system.domain.model.menu.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * システムメニュー管理テーブル Mapper 接口
 * </p>
 *
 * @author YAOXIA
 * @since 2026-03-01
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

}
