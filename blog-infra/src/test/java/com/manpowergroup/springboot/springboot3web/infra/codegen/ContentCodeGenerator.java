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

public class ContentCodeGenerator {

    private static String envOr(String k, String d) {
        String v = System.getenv(k);
        return (v == null || v.isBlank()) ? d : v;
    }

    public static void main(String[] args) throws Exception {
        // 1) DB 连接
        String url  = envOr("DB_URL",  "jdbc:mysql://localhost:3306/blog_db?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Tokyo");
        String user = envOr("DB_USER", "root");
        String pwd  = envOr("DB_PWD",  "Yx19900703.");

        // 2) 以 blog-infra 为工作目录，输出到 blog-module-content
        Path infraRoot = Paths.get(System.getProperty("user.dir"));             // .../manpower-blog/blog-infra
        Path target    = infraRoot.getParent().resolve("blog-module-content");  // .../manpower-blog/blog-module-content
        String javaOut = target.resolve("src/main/java").toString();
        String xmlOut  = target.resolve("src/main/resources/mapper").toString();

        // 兜底创建输出目录
        Files.createDirectories(Paths.get(javaOut));
        Files.createDirectories(Paths.get(xmlOut));

        // 3) 包前缀与表
        String basePkg = "com.manpowergroup.springboot.springboot3web.content";
        List<String> tables = (args != null && args.length > 0)
                ? Arrays.asList(args)
                : List.of("t_content_article");

        // 4) 自定义各文件输出路径 —— 把 Controller 映射到黑洞目录（即使模板没禁用也不会污染工程）
        Map<OutputFile, String> paths = new HashMap<>();
        paths.put(OutputFile.xml, xmlOut);
        paths.put(OutputFile.controller, infraRoot.resolve("target/generated-ignore").toString());

        FastAutoGenerator.create(url, user, pwd)
                .globalConfig(b -> b
                        .author("YAOXIA")
                        .outputDir(javaOut)                 // Java 文件输出到 content 模块
                        .dateType(DateType.TIME_PACK)
                        .disableOpenDir()
                )
                .packageConfig(b -> b
                        .parent(basePkg)
                        .pathInfo(paths)                    // 覆盖 XML 与 Controller 的输出路径
                )
                .strategyConfig(b -> b
                        .addInclude(tables)
                        .addTablePrefix("t_content_")
                        .entityBuilder().enableLombok()
                        .mapperBuilder().enableBaseResultMap().enableBaseColumnList()
                        .serviceBuilder()
                        .formatServiceFileName("%sService")
                        .formatServiceImplFileName("%sServiceImpl")
                )
                // 双保险：禁用 Controller 模板
                .templateConfig(t -> t.disable(TemplateType.CONTROLLER))
                // 使用 Freemarker
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
