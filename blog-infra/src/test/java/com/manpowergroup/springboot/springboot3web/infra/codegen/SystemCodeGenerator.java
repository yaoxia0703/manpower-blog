package com.manpowergroup.springboot.springboot3web.infra.codegen;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class SystemCodeGenerator {

    private static String envOr(String k, String d) {
        String v = System.getenv(k);
        return (v == null || v.isBlank()) ? d : v;
    }

    public static void main(String[] args) throws Exception {
        // ① DB：可用环境变量覆盖（DB_URL/DB_USER/DB_PWD）
        String url  = envOr("DB_URL",  "jdbc:mysql://localhost:3306/blog_db?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Tokyo");
        String user = envOr("DB_USER", "root");
        String pwd  = envOr("DB_PWD",  "Yx19900703.");

        // ② 输出：固定到 blog-module-system
        Path infraRoot = Paths.get(System.getProperty("user.dir"));        // 当前是 blog-infra
        Path target    = infraRoot.getParent().resolve("blog-module-system");
        String javaOut = target.resolve("src/main/java").toString();
        String xmlOut  = target.resolve("src/main/resources/mapper").toString();

        // 兜底创建目录
        Files.createDirectories(Paths.get(javaOut));
        Files.createDirectories(Paths.get(xmlOut));

        // ③ 包前缀 & 表（运行时也可通过 Program arguments 传表名：t_sys_user t_sys_role）
        String basePkg = "com.manpowergroup.springboot.springboot3web.system";
        List<String> tables = (args != null && args.length > 0)
                ? Arrays.asList(args)
                : List.of("t_sys_user");

        // 黑洞目录：即使模板误开，也不会把 Controller 丢到工程里
        Map<OutputFile, String> paths = new HashMap<>();
        paths.put(OutputFile.xml, xmlOut);
        paths.put(OutputFile.controller, infraRoot.resolve("target/generated-ignore").toString());

        FastAutoGenerator.create(url, user, pwd)
                .globalConfig(b -> b
                        .author("YAOXIA")
                        .outputDir(javaOut)
                        .dateType(DateType.TIME_PACK)
                        .disableOpenDir()
                )
                .packageConfig(b -> b
                        .parent(basePkg)
                        .pathInfo(paths)
                )
                .strategyConfig(b -> b
                                .addInclude(tables)
                                .addTablePrefix("t_sys_")
                                .entityBuilder().enableLombok()
                                .mapperBuilder().enableBaseResultMap().enableBaseColumnList()
                                .serviceBuilder()
                                .formatServiceFileName("%sService")
                                .formatServiceImplFileName("%sServiceImpl")
                        // 需要覆盖时再打开：
                        // .entityBuilder().enableFileOverride()
                        // .mapperBuilder().enableFileOverride()
                        // .serviceBuilder().enableFileOverride()
                )
                // 不生成 Controller（要的话删掉这行）
                .templateConfig(t -> t.disable(TemplateType.CONTROLLER))
                // 使用 Freemarker 引擎
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
