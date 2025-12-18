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

    /**
     * 環境変数取得（未設定時はデフォルト値を使用）
     */
    private static String envOr(String k, String d) {
        String v = System.getenv(k);
        return (v == null || v.isBlank()) ? d : v;
    }

    public static void main(String[] args) throws Exception {

        // DB 接続情報
        String url  = envOr(
                "DB_URL",
                "jdbc:mysql://localhost:3306/blog_db?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Tokyo"
        );
        String user = envOr("DB_USER", "root");
        String pwd  = envOr("DB_PWD",  "Yx19900703.");

        // 出力先設定（blog-module-system）
        Path infraRoot = Paths.get(System.getProperty("user.dir"));
        Path target    = infraRoot.getParent().resolve("blog-module-system");
        String javaOut = target.resolve("src/main/java").toString();
        String xmlOut  = target.resolve("src/main/resources/mapper").toString();

        // 出力ディレクトリ作成
        Files.createDirectories(Paths.get(javaOut));
        Files.createDirectories(Paths.get(xmlOut));

        // パッケージプレフィックスおよび対象テーブル
        String basePkg = "com.manpowergroup.springboot.springboot3web.system";
        List<String> tables = (args != null && args.length > 0)
                ? Arrays.asList(args)
                : List.of("t_sys_user");

        // 各ファイルの出力先設定
        Map<OutputFile, String> paths = new HashMap<>();
        paths.put(OutputFile.xml, xmlOut);
        paths.put(OutputFile.controller,
                infraRoot.resolve("target/generated-ignore").toString());

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
                        .mapperBuilder()
                        .enableBaseResultMap()
                        .enableBaseColumnList()
                        .serviceBuilder()
                        .formatServiceFileName("%sService")
                        .formatServiceImplFileName("%sServiceImpl")
                )
                // Controller テンプレートを無効化
                .templateConfig(t -> t.disable(TemplateType.CONTROLLER))
                // Freemarker テンプレートエンジンを使用
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
