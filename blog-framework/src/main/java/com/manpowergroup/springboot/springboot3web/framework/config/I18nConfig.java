package com.manpowergroup.springboot.springboot3web.framework.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

/**
 * 国際化（i18n）設定クラス
 */
@Configuration
public class I18nConfig {

    /**
     * メッセージリソースの設定
     */
    @Bean
    public MessageSource messageSource() {
        var ms = new ReloadableResourceBundleMessageSource();
        ms.setBasename("classpath:messages");
        ms.setDefaultEncoding("UTF-8");
        ms.setFallbackToSystemLocale(false);
        // 該当するキーが存在しない場合は、キー文字列をそのまま返す
        ms.setUseCodeAsDefaultMessage(true);
        return ms;
    }

    /**
     * ロケール解決の設定
     * Accept-Language ヘッダーをもとにロケールを判定する
     */
    @Bean
    public LocaleResolver localeResolver() {
        var lr = new AcceptHeaderLocaleResolver();
        // デフォルトロケールは日本語
        lr.setDefaultLocale(Locale.JAPAN);
        return lr;
    }
}
