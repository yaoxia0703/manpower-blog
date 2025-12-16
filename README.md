Manpower Blog

Spring Boot 3 をベースとしたマルチモジュール構成のブログバックエンドです。
親 POM にて Java 21、依存関係、Maven プラグインを一元管理し、各サブモジュールは責務単位で分割されています。
本リポジトリは、中〜大規模バックエンド開発を想定した構成・設計のサンプルとして整備されています。

技術スタック
- Java 21
- Spring Boot 3.x
- MyBatis-Plus
- MySQL
- Redis（Optional）
- springdoc OpenAPI
- Maven（Multi-Module）

モジュール構成
blog-common：共通 DTO、例外、ユーティリティ
blog-framework：Security、例外処理、MyBatis-Plus、Swagger
blog-infra：コード生成・開発支援
blog-module-system：ユーザー、権限などのシステム領域
blog-module-content：記事などのコンテンツ領域
blog-admin-api：管理画面向け API
blog-portal-api：公開 API
blog-starter：起動モジュール

起動方法
1. JDK21、Maven、MySQL を準備
2. mvn clean install
3. mvn spring-boot:run -pl blog-starter

アプリケーションのエントリポイントは
blog-starter モジュールの ManpowerBlogApplication クラスです。

オプション設定
- Redis はデフォルトで無効（infra.redis.enabled=false）
  有効化する場合は application.yml にて spring.redis を設定し、
  Redis 自動設定の除外定義を削除してください。
- MyBatis-Plus の Mapper XML パスや論理削除フィールドは
  application-dev.yml にて調整可能です。

API ドキュメント
起動後、springdoc 提供の Swagger UI
(/swagger-ui.html または /swagger-ui/index.html) から
API 仕様を確認できます。
