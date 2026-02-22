# manpower-blog 企業向けブログシステム

## 1. プロジェクト概要
本プロジェクトは、企業内利用および対外公開の双方を想定した、マルチモジュール構成のブログシステムです。Spring Boot 3 を中核とし、業務ドメインをモジュール単位で分離することで、保守性・拡張性・責務分離を重視した設計としています。

本リポジトリはバックエンド実装を中心としており、管理画面・ポータル画面から利用される API 群を提供します。フロントエンドは Vue3 + Element Plus を前提とした構成で連携可能な設計です。

補足（English）: This repository currently contains the backend multi-module implementation and is designed to integrate with a Vue3 + Element Plus frontend.

## 2. システム構成（Spring Boot 3 + Vue3 + Element Plus）
システムは以下の 2 層で構成します。

- Backend: Spring Boot 3（本リポジトリに実装）
  - 管理系 API（`/admin` 系）
  - 公開系 API（`/portal` 系）
  - 認証認可（Spring Security + JWT）
  - 共通例外処理、トレース ID、OpenAPI
- Frontend: Vue3 + Element Plus（別途フロントエンドプロジェクトを想定）
  - 管理コンソール（RBAC 前提）
  - 公開ブログ UI

補足（中文）: 当前仓库主要提供后端服务，前端（Vue3 + Element Plus）建议以独立工程方式接入。

## 3. 技術スタック一覧
### バックエンド
- Java 21（親 POM で `java.version=21` を管理）
- Spring Boot 3.4.9
- Spring Web / Spring Security
- MyBatis-Plus 3.5.x
- JWT（jjwt 0.11.5）
- springdoc-openapi（Swagger UI）
- MySQL（ランタイムドライバ）
- Redis（機能有効化時）
- Maven（Multi-Module）
- Lombok / MapStruct

### 開発支援・テスト
- MyBatis-Plus Generator（`blog-infra`）
- Freemarker（コード生成用）
- Testcontainers（`blog-module-content` テスト依存）

## 4. モジュール構成の説明
本プロジェクトの Maven モジュールは以下の責務で分割されています。

### `blog-common`
共通 DTO、共通例外、列挙体、ユーティリティ、ページング関連設定を提供します。全モジュールから参照される基盤ライブラリです。

### `blog-framework`
アプリケーション横断のフレームワーク層です。
- Security 設定（JWT フィルタ、パスワードサービス、権限プロバイダ IF）
- グローバル例外処理
- MyBatis-Plus 設定
- I18n 設定
- Swagger/OpenAPI 設定
- TraceId フィルタ／レスポンス付与
- Redis 設定・ヘルスチェック

### `blog-infra`
開発支援モジュールです。主に MyBatis-Plus Generator を利用したコード生成ユーティリティを test スコープで保持します。

### `blog-module-system`
システム管理ドメイン（ユーザー、ロール、権限、ログイン）を扱う業務モジュールです。RBAC の中核ロジックを保持します。

### `blog-module-content`
コンテンツドメイン（記事）を扱う業務モジュールです。記事エンティティ、Mapper、Service、DTO を提供します。

### `blog-admin-api`
管理系 API モジュールです。`blog-module-system` を利用し、管理画面向けエンドポイントを提供します。

### `blog-portal-api`
公開系 API モジュールです。`blog-module-content` を利用し、ポータル向けエンドポイントを提供します。

### `blog-starter`
起動モジュールです。`ManpowerBlogApplication` をエントリーポイントとして、各モジュールを集約して実行可能な Spring Boot アプリケーションを構成します。

## 5. ディレクトリ構成
```text
manpower-blog/
├── pom.xml
├── blog-common/
├── blog-framework/
├── blog-infra/
├── blog-module-system/
├── blog-module-content/
├── blog-admin-api/
├── blog-portal-api/
└── blog-starter/
```

主要依存関係（概略）:
- `blog-admin-api` → `blog-module-system` → `blog-framework` → `blog-common`
- `blog-portal-api` → `blog-module-content` → `blog-framework` → `blog-common`
- `blog-starter` → `blog-admin-api`, `blog-portal-api`, `blog-module-system`, `blog-module-content`, `blog-framework`, `blog-common`

## 6. 起動手順

### 6.1 Backend（Spring Boot）

#### 前提環境
- JDK 21
- Maven 3.9 以上
- MySQL（利用時）
- Redis（有効化時のみ）

#### 手順
1. ルートディレクトリでビルド
   ```bash
   mvn clean install
   ```
2. 起動
   ```bash
   mvn spring-boot:run -pl blog-starter
   ```
3. 既定のアクセス先
   - アプリケーション: `http://localhost:8080`
   - Swagger UI: `http://localhost:8080/swagger-ui/index.html`

#### 設定補足
- `application.yml` では `spring.profiles.active=dev` が既定です。
- Redis は `infra.redis.enabled=false` が既定で、さらに Redis AutoConfiguration を除外しています。
- 本番向け設定は `application-prod.yml` を利用し、MySQL/Redis 接続情報を環境変数で注入する想定です。

### 6.2 Frontend（Vue3）
本リポジトリには Vue3 実装は同梱されていません。実運用では以下の別プロジェクト構成を推奨します。

- 技術要素（推奨）
  - Vue 3
  - Element Plus
  - Vue Router
  - Pinia
  - Axios
- バックエンド接続先
  - 管理 API: `blog-admin-api` が公開するエンドポイント
  - 公開 API: `blog-portal-api` が公開するエンドポイント

補足（English）: Frontend codebase should be maintained independently and integrated via REST APIs.

## 7. 開発方針・設計思想

### RBAC（Role-Based Access Control）
- `User` / `Role` / `Permission` / `UserRole` / `RolePermission` のドメインモデルを中心に認可を構成します。
- `SystemUserAuthorityProvider` と Security 設定により、業務権限を Spring Security の認可処理へ連携します。

### `Result` による統一レスポンス
- API レスポンスは `blog-common` の `Result` DTO を基準として統一します。
- エラーは共通例外（`BizException`）とグローバル例外ハンドラで一元制御し、呼出元のハンドリング複雑性を抑制します。

### JDK17+ スタイルを踏まえた実装方針
- 実行環境は Java 21 を採用し、現行 LTS 世代（JDK17 以降）を前提とした実装規約で統一します。
- DTO/Service/Mapper の責務分離、設定クラスの明確化、モジュール境界の明示により、長期運用可能なコードベースを維持します。

### 運用性・監査性の考慮
- TraceId の付与により、分散ログ追跡および障害解析を容易化します。
- OpenAPI により API 契約を可視化し、フロントエンド・外部システム連携時の認識齟齬を低減します。

## 8. 将来拡張予定
以下は企業システムとしての拡張候補です。

1. Redis 本格活用
   - セッション補助、キャッシュ戦略、レート制御
2. メッセージング基盤（MQ）
   - 非同期処理、イベント駆動連携、バッチ連携
3. 分散化・高可用化
   - アプリケーション水平分割
   - API Gateway / 認証基盤統合
   - 分散トレーシング
4. データ基盤強化
   - 読み書き分離
   - 監査ログ・変更履歴の強化
5. CI/CD・品質統制
   - 静的解析、テスト自動化、デプロイ標準化

以上を段階的に導入し、業務要件の拡大に追従可能なプラットフォームへ発展させる方針です。
