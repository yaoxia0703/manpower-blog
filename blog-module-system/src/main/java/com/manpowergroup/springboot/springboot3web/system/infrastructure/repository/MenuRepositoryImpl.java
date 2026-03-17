package com.manpowergroup.springboot.springboot3web.system.infrastructure.repository;

import com.manpowergroup.springboot.springboot3web.system.domain.model.menu.Menu;
import com.manpowergroup.springboot.springboot3web.system.domain.repository.MenuRepository;
import com.manpowergroup.springboot.springboot3web.system.infrastructure.mapper.menu.MenuMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MenuRepositoryImpl  implements MenuRepository {
    private final MenuMapper menuMapper;

    public MenuRepositoryImpl(MenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }

    @Override
    public List<Menu> selectMenusByUserId(Long userId) {
        return menuMapper.selectMenusByUserId(userId);
    }

    @Override
    public int existsByParentIdAndName(Long parentId, String name) {
        return menuMapper.existsByParentIdAndName(parentId, name);
    }

    @Override
    public int countByParentId(Long parentId) {
        return menuMapper.countByParentId(parentId);
    }

    @Override
    public int countByParentIdAndNameExcludeId(Long parentId, String name, Long id) {
        return menuMapper.countByParentIdAndNameExcludeId(parentId, name, id);
    }

    @Override
    public int countByPath(String path) {
        return menuMapper.countByPath(path);
    }

    @Override
    public int countByPathExcludeId(String path, Long id) {
        return menuMapper.countByPathExcludeId(path, id);
    }
}
