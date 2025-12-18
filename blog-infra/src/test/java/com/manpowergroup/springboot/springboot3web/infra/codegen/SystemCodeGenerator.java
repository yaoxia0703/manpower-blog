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
    private static String envOr(String key, String def) {
        String val = System.getenv(key);
        return (val == null || val.isBlank()) ? def : val;
    }

    public static void main(String[] args) throws Exception {

        /* ========= DB 接続情報 ========= */
        String url = envOr(
                "DB_URL",
                "jdbc:mysql://localhost:3306/blog_db" +
                        "?useUnicode=true&characterEncoding=utf8" +
                        "&serverTimezone=Asia/Tokyo"
        );
        String user = envOr("DB_USER", "root");
        String pwd  = envOr("DB_PWD", "Yx19900703.");

        /* ========= プロジェクトルート判定 ========= */
        Path workDir = Paths.get(System.getProperty("user.dir")).toAbsolutePath();

        // pom.xml があればそこをプロジェクトルートとみなす
        Path projectRoot = Files.exists(workDir.resolve("pom.xml"))
                ? workDir
                : workDir.getParent();

        /* ========= 出力先（blog-module-system） ========= */
        Path target = projectRoot.resolve("blog-module-system");

        String javaOut = target.resolve("src/main/java").toString();
        String xmlOut  = target.resolve("src/main/resources/mapper").toString();

        Files.createDirectories(Paths.get(javaOut));
        Files.createDirectories(Paths.get(xmlOut));

        /* ========= パッケージ & 対象テーブル ========= */
        String basePkg = "com.manpowergroup.springboot.springboot3web.system";

        List<String> tables = (args != null && args.length > 0)
                ? Arrays.asList(args)
                : List.of("t_sys_user");

        /* ========= 出力ファイル設定 ========= */
        Map<OutputFile, String> pathInfo = new HashMap<>();
        pathInfo.put(OutputFile.xml, xmlOut);

        // Controller は生成しない（念のため捨て先を指定）
        /*pathInfo.put(
                OutputFile.controller,
                projectRoot.resolve("target/generated-ignore").toString()
        );*/

        /* ========= Generator 実行 ========= */
        FastAutoGenerator.create(url, user, pwd)
                .globalConfig(builder -> builder
                        .author("YAOXIA")
                        .outputDir(javaOut)
                        .dateType(DateType.TIME_PACK)
                        .disableOpenDir()
                )
                .packageConfig(builder -> builder
                        .parent(basePkg)
                        .entity("entity")
                        .pathInfo(pathInfo)
                )
                .strategyConfig(builder -> builder
                        .addInclude(tables)
                        .addTablePrefix("t_sys_")

                        // ===== Entity =====
                        .entityBuilder()
                        .enableLombok()
                        .disableSerialVersionUID()

                        // ===== Mapper =====
                        .mapperBuilder()
                        .enableBaseResultMap()
                        .enableBaseColumnList()

                        // ===== Service =====
                        .serviceBuilder()
                        .formatServiceFileName("%sService")
                        .formatServiceImplFileName("%sServiceImpl")
                )
                // Controller テンプレート無効化
                .templateConfig(t -> t.disable(TemplateType.CONTROLLER))
                // Freemarker 使用
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
