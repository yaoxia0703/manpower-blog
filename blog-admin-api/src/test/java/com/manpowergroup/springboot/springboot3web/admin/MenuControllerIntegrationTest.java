package com.manpowergroup.springboot.springboot3web.admin;

import com.manpowergroup.springboot.springboot3web.framework.security.jwt.JwtTokenProvider;
import com.manpowergroup.springboot.springboot3web.system.application.service.LoginAppService;
import com.manpowergroup.springboot.springboot3web.system.application.service.MenuAppService;
import com.manpowergroup.springboot.springboot3web.system.application.service.PermissionAppService;
import com.manpowergroup.springboot.springboot3web.system.application.service.RoleAppService;
import com.manpowergroup.springboot.springboot3web.system.application.service.UserAppService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TestApplication.class)
@AutoConfigureMockMvc(addFilters = false)
class MenuControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuAppService menuAppService;

    @MockBean
    private LoginAppService loginAppService;

    @MockBean
    private UserAppService userAppService;

    @MockBean
    private RoleAppService roleAppService;

    @MockBean
    private PermissionAppService permissionAppService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @WithMockUser(authorities = "sys:menu:create")
    void shouldCreateMenuSuccessfully() throws Exception {
        when(menuAppService.createMenu(any())).thenReturn(100L);

        mockMvc.perform(post("/api/system/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "parentId": 0,
                                  "name": "系统管理",
                                  "path": "/system",
                                  "component": "Layout",
                                  "permission": "sys:menu:list",
                                  "type": 1,
                                  "sort": 1,
                                  "icon": "system",
                                  "status": 1
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(100L));

        verify(menuAppService).createMenu(any());
    }
}
