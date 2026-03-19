package com.manpowergroup.springboot.springboot3web.admin;

import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;
import com.manpowergroup.springboot.springboot3web.blog.common.exception.BizException;
import com.manpowergroup.springboot.springboot3web.framework.handler.GlobalExceptionHandler;
import com.manpowergroup.springboot.springboot3web.system.application.service.MenuAppService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class MenuControllerTest {

    private static final String BASE_URL = "/api/system/menu";

    private static final String VALID_CREATE_REQUEST = """
            {
              "parentId": 0,
              "name": "メニュー管理",
              "path": "/system/menu",
              "component": "system/menu/index",
              "permission": "sys:menu:list",
              "type": 2,
              "sort": 1,
              "icon": "menu",
              "status": 1
            }
            """;

    private static final String INVALID_CREATE_REQUEST = """
            {
              "parentId": 0,
              "path": "/system/menu",
              "component": "system/menu/index",
              "permission": "sys:menu:list",
              "type": 2,
              "sort": 1,
              "icon": "menu"
            }
            """;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuAppService menuAppService;

    @MockBean
    private MessageSource messageSource;

    @MockBean
    private Environment environment;

    @BeforeEach
    void setUp() {
        when(environment.getActiveProfiles()).thenReturn(new String[0]);
        when(messageSource.getMessage(any(String.class), ArgumentMatchers.<Object[]>any(), any(Locale.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void create_ShouldReturnSuccessResult_WhenRequestIsValid() throws Exception {
        when(menuAppService.createMenu(any())).thenReturn(100L);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_CREATE_REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success.ok"))
                .andExpect(jsonPath("$.data").value(100L));
    }

    @Test
    void create_ShouldReturnValidationError_WhenRequestIsInvalid() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(INVALID_CREATE_REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(422))
                .andExpect(jsonPath("$.message").value("error.validation"))
                .andExpect(jsonPath("$.data.errors[?(@.field=='name')]").exists())
                .andExpect(jsonPath("$.data.errors[?(@.field=='status')]").exists());
    }

    @Test
    void create_ShouldReturnBusinessError_WhenServiceThrowsBizException() throws Exception {
        doThrow(BizException.withDetail(ErrorCode.SERVER_ERROR, "メニュー作成に失敗しました"))
                .when(menuAppService)
                .createMenu(any());

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_CREATE_REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("error.server"))
                .andExpect(jsonPath("$.detail").value("メニュー作成に失敗しました"));
    }
}
