package com.manpowergroup.springboot.springboot3web.system.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.AccountType;
import com.manpowergroup.springboot.springboot3web.blog.common.util.CollectionUtils;
import com.manpowergroup.springboot.springboot3web.blog.common.util.StringUtils;
import com.manpowergroup.springboot.springboot3web.system.application.dto.userAccount.LoginAccountUserDTO;
import com.manpowergroup.springboot.springboot3web.system.domain.model.role.Role;
import com.manpowergroup.springboot.springboot3web.system.domain.model.user.UserAccount;
import com.manpowergroup.springboot.springboot3web.system.domain.model.user.UserRole;
import com.manpowergroup.springboot.springboot3web.system.domain.repository.UserAccountRepository;
import com.manpowergroup.springboot.springboot3web.system.domain.repository.UserRoleRepository;
import com.manpowergroup.springboot.springboot3web.system.infrastructure.mapper.role.RoleMapper;
import com.manpowergroup.springboot.springboot3web.system.infrastructure.mapper.user.UserAccountMapper;
import com.manpowergroup.springboot.springboot3web.system.infrastructure.mapper.user.UserRoleMapper;
import com.manpowergroup.springboot.springboot3web.system.application.service.UserAccountAppService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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
public class UserAccountAppServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements UserAccountAppService {
    private final UserRoleMapper userRoleMapper;
    private final RoleMapper roleMapper;
    private final UserAccountRepository userAccountRepository;




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

        final var userRoles = CollectionUtils.safeList(
                userRoleMapper.selectList(
                        new LambdaQueryWrapper<UserRole>()
                                .eq(UserRole::getUserId, userId)
                )
        );

        if (CollectionUtils.isEmpty(userRoles)) {
            return List.of();
        }

        final var roleIds = userRoles.stream()
                .map(UserRole::getRoleId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (roleIds.isEmpty()) {
            return List.of();
        }

        return CollectionUtils.safeList(roleMapper.selectBatchIds(roleIds))
                .stream()
                .map(Role::getName)
                .filter(StringUtils::hasText)
                .distinct()
                .toList();
    }


    @Override
    public Optional<LoginAccountUserDTO> findLoginUserByAccountTypeAndAccountValue(String accountType, String accountValue) {
        return Optional.ofNullable(userAccountRepository.findLoginUserByAccountTypeAndAccountValue(accountType, accountValue));
    }
}
