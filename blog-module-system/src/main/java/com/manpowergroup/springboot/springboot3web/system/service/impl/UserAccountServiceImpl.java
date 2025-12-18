package com.manpowergroup.springboot.springboot3web.system.service.impl;

import com.manpowergroup.springboot.springboot3web.system.entity.UserAccount;
import com.manpowergroup.springboot.springboot3web.system.mapper.UserAccountMapper;
import com.manpowergroup.springboot.springboot3web.system.service.UserAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * ユーザーログインアカウント 服务实现类
 * </p>
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements UserAccountService {

}
