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

## 7. 設計思想

### 第一段階：現在の構成（Why / 現在の構成）
初期段階では、業務境界がまだ十分に固まっていない状態で分散アーキテクチャを先行導入すると、通信・運用・データ整合性の複雑性が先に立ち、開発効率と全体の推進速度を落としやすくなります。そのため本プロジェクトでは、まず **Modular Monolith** を採用し、複雑度を抑えながら構造を安定させる方針を取っています。

現在は業務ドメインごとにモジュールを分割し、`system` と `content` をそれぞれ独立した責務として扱い、API 層もそれに対応して管理系と公開系に分離しています。これにより、初期段階から業務単位で境界を明確にし、将来の拡張に備えた整理された構成を維持します。

また、セキュリティ、ログ、永続化設定などの基盤機能は `framework` に集約し、共通レスポンス、列挙体、バリデーションなどの共通能力は `common` に分離しています。業務モジュールと横断関心事、さらに再利用可能な共通要素を切り分けることで、責務の混在を避け、モジュール境界を明確にしています。

業務設計においては、DDD の考え方を取り入れ、業務ルールと処理フローを技術実装から切り離して整理します。これにより、コアとなる業務ロジックを技術都合に引きずられにくい形で保持し、保守性と拡張性を確保します。

第一段階の主目的は以下の 3 点です。
- モジュール境界を明確にする
- システム構造を安定させる
- 一連の基本機能を一通り成立させる

この段階では、機能を増やすこと以上に、**複雑度を制御しながら、明確な境界を持つ土台を作ること** を重視します。これが第二段階への前提条件になります。

### 第二段階：拡張と進化（Future / Evolution）
第二段階では、第一段階で整理したモジュール境界を前提に、性能と拡張性を段階的に高めていきます。まず、データアクセス負荷の増加に対しては Redis などのキャッシュを導入し、データベースへの集中を緩和して応答性能を改善します。

その上で、業務モジュールの境界が十分に安定し、独立性が確認できた単位から、分散アーキテクチャへの移行を進めます。ここで重要なのは、一括分割ではなく、必要性と成熟度に応じた **段階的な進化** を行うことです。これにより、改修リスクを抑えながら、業務の継続性を維持できます。

サービス分割と複数インスタンス運用が進んだ段階では、負荷分散を導入し、リクエストを適切に振り分けることで可用性と同時処理能力を高めます。さらに、データ量が増加して単一データベースの限界が近づいた場合には、分庫分表のような水平分割も検討対象とします。

すなわち第二段階は、第一段階で作った Modular Monolith の明確な境界を起点として、キャッシュ導入、分散化、負荷分散、データ分割へと無理なく発展させるフェーズです。設計の一貫した考え方は、常に **複雑度の制御、境界の明確化、段階的な進化** にあります。



## 8. 将来拡張予定
以下は企業システムとしての拡張候補です。

1. Redis 本格活用
   - セッション補助、キャッシュ戦略、レート制御
2. メッセージング基盤（MQ）
   - 非同期処理、イベント駆動連携、バッチ連携
3. 分散化・高可用化
   - アプリケーション水平分割
   - ロードバランシング（負荷分散）の導入
   - API Gateway / 認証基盤統合
   - 分散トレーシング
4. データ基盤強化
   - 読み書き分離
   - 監査ログ・変更履歴の強化
5. CI/CD・品質統制
   - 静的解析、テスト自動化、デプロイ標準化
6. 高負荷領域の最適化
   - コメント、いいね、閲覧数などの高頻度アクセスが発生する機能については、将来的に Redis を活用したキャッシュ戦略および Go 言語によるサービス分離を検討
   - 高並列処理に適した構成を導入することで、パフォーマンスおよびスケーラビリティの向上を図る

以上を段階的に導入し、業務要件の拡大に追従可能なプラットフォームへ発展させる方針です。
