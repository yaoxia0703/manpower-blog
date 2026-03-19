package com.manpowergroup.springboot.springboot3web.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manpowergroup.springboot.springboot3web.ManpowerBlogApplication;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.Result;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.MenuType;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.Status;
import com.manpowergroup.springboot.springboot3web.system.application.dto.menu.MenuSaveOrUpdateRequest;
import com.manpowergroup.springboot.springboot3web.system.application.service.MenuAppService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ManpowerBlogApplication.class)
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class MenuControllerIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(MenuControllerIntegrationTest.class);
    private static final String MENU_API_PATH = "/api/system/menu";

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private MenuAppService menuAppService;

    MenuControllerIntegrationTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    @WithMockUser(authorities = "sys:menu:create")
    void shouldCreateMenuSuccessfully() throws Exception {
        MenuSaveOrUpdateRequest request = new MenuSaveOrUpdateRequest(
                0L,
                "系统管理",
                "/system",
                "system/index",
                "sys:menu:list",
                MenuType.MENU,
                1,
                "setting",
                Status.ENABLED
        );
        long expectedMenuId = 1001L;
        when(menuAppService.createMenu(any(MenuSaveOrUpdateRequest.class))).thenReturn(expectedMenuId);

        log.info("[MenuControllerIntegrationTest#shouldCreateMenuSuccessfully] request={}", request);

        MvcResult mvcResult = mockMvc.perform(post(MENU_API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        Result<Long> result = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );

        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo(200);
        assertThat(result.getData()).isEqualTo(expectedMenuId);
        verify(menuAppService, times(1)).createMenu(any(MenuSaveOrUpdateRequest.class));
    }

    @Test
    @WithMockUser(authorities = "sys:menu:create")
    void shouldReturnBadRequestWhenNameIsMissing() throws Exception {
        String invalidRequestBody = """
                {
                  "parentId": 0,
                  "path": "/system",
                  "component": "system/index",
                  "permission": "sys:menu:list",
                  "type": 2,
                  "sort": 1,
                  "icon": "setting",
                  "status": 1
                }
                """;

        log.info("[MenuControllerIntegrationTest#shouldReturnBadRequestWhenNameIsMissing] requestBody={}", invalidRequestBody);

        MvcResult mvcResult = mockMvc.perform(post(MENU_API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestBody))
                .andExpect(status().isBadRequest())
                .andReturn();

        Result<?> result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Result.class);

        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo(400);
        verify(menuAppService, times(0)).createMenu(any(MenuSaveOrUpdateRequest.class));
    }
}
