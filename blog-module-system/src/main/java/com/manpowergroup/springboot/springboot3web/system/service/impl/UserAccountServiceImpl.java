package com.manpowergroup.springboot.springboot3web.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.AccountType;
import com.manpowergroup.springboot.springboot3web.system.entity.Role;
import com.manpowergroup.springboot.springboot3web.system.entity.UserAccount;
import com.manpowergroup.springboot.springboot3web.system.entity.UserRole;
import com.manpowergroup.springboot.springboot3web.system.mapper.RoleMapper;
import com.manpowergroup.springboot.springboot3web.system.mapper.UserAccountMapper;
import com.manpowergroup.springboot.springboot3web.system.mapper.UserRoleMapper;
import com.manpowergroup.springboot.springboot3web.system.service.UserAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * ユーザーログインアカウント 服务实现类
 * </p>
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
@Service
@AllArgsConstructor
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements UserAccountService {
    private final UserRoleMapper userRoleMapper;
    private final RoleMapper roleMapper;


    @Override
    public Optional<UserAccount> findActiveAccount(AccountType type, String accountValue) {

        LambdaQueryWrapper<UserAccount> queryWrapper = new LambdaQueryWrapper<UserAccount>()
                .eq(UserAccount::getAccountType, type)
                .eq(UserAccount::getAccountValue, accountValue)
                .eq(UserAccount::getStatus, 1);

        return Optional.ofNullable(this.getOne(queryWrapper, false));
    }

    @Override
    public List<String> findRoleNamesByUserId(Long userId) {
        List<UserRole> userRoles= userRoleMapper.selectList(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId));
        if (userRoles.isEmpty()) {
            return List.of();
        }
        List<Long> roleIds = userRoles.stream()
                .map(UserRole::getRoleId)
                .toList();

        return roleMapper.selectBatchIds(roleIds)
                .stream()
                .map(Role::getName)
                .toList();
    }
}
