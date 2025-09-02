package com.manpowergroup.springboot.springboot3web.system.user.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_sys_user")
public class User {
    @TableId(type = IdType.AUTO) // 如果不是自增，改成 ASSIGN_ID
    private Long id;

    private String username;
    private String nickName;
    private String email;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic // 配合你框架层的逻辑删除配置：0未删 1已删
    private Integer isDeleted;
}
