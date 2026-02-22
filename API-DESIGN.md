# API 設計書（manpower-blog）

## 1. 文書目的
本書は、`manpower-blog` リポジトリの現行実装コードを根拠として、企業向け API 仕様を整理するものである。記載内容は推測を排し、コード上で確認できる事実のみを対象とする。

## 2. 適用範囲と前提
- 対象: `blog-admin-api`（管理系 API）、`blog-portal-api`（公開系 API）
- 共通基盤: `blog-common`（DTO/例外/列挙）、`blog-framework`（Security/JWT/例外ハンドラ）
- 参照整合: README/ARCHITECTURE に記載された「Admin API と Portal API の分離」「Result<T> 統一レスポンス」「Spring Security + JWT + RBAC」の方針と実装が一致していることを確認済み

## 3. 共通レスポンス仕様（`Result<T>`）

### 3.1 レスポンス JSON 基本構造
全 API は `Result<T>` を返却する実装方針であり、以下の項目を持つ。

| 項目 | 型 | 必須 | 説明 |
|---|---|---:|---|
| code | int | 必須 | アプリケーションコード（HTTP 相当値を採用） |
| message | String | 必須 | メッセージ文字列。i18n キーをそのまま返す場合と翻訳済み文字列を返す場合がある |
| data | T | 任意 | 正常系/一部異常系でのペイロード |
| traceId | String | 任意 | トレース ID（ハンドラ/フィルタで付与する想定） |
| timestamp | Long | 必須（ファクトリ利用時） | エポックミリ秒 |
| detail | String | 任意 | 追加詳細。prod プロファイルでは非表示化される実装 |

### 3.2 生成メソッド仕様
`Result<T>` は以下のファクトリを提供する。
- 正常系: `ok(data)`, `ok(data, msg)`, `okMsg(msg)`, `of(code, msg, data)`
- 異常系: `error(msg)`, `error(code, msg)`, `errorWithDetail(code, msg, detail)`, `fail(code, msg)`
- 拡張: `withDetail(detail)`, `withTraceId(traceId)`

補足:
- `ok(data)` の既定 message は `success.ok`。
- `@JsonInclude(NON_NULL)` のため、null 項目は JSON 出力されない。

## 4. エラーコード・例外設計

### 4.1 `ErrorCode` 設計
`ErrorCode` は `code`（数値）と `messageKey`（i18n キー）を保持する。代表値は以下。
- 200: `SUCCESS`
- 400: `BAD_REQUEST`
- 401: `UNAUTHORIZED`
- 403: `FORBIDDEN`
- 404: `NOT_FOUND`
- 405: `METHOD_NOT_ALLOWED`
- 409: `CONFLICT` / `BIZ_ERROR`
- 415: `UNSUPPORTED_MEDIA_TYPE`
- 422: `VALIDATION_ERROR`
- 500: `SERVER_ERROR`
- 503: `SERVICE_UNAVAILABLE`

※ ファイル処理・インポート処理向けの専用コードも定義されている（本 API 群での利用有無は要確認）。

### 4.2 `BizException` 設計
`BizException` は以下を保持する。
- `ErrorCode code`
- `String messageKey`
- `Object[] args`（i18n プレースホルダ引数）
- `String detail`（開発/検証向け詳細）

特徴:
- 互換コンストラクタ（`ErrorCode` のみ）と推奨コンストラクタ（`messageKey` + args）を併存。
- `withDetail` / `withCause` 等の静的ファクトリを提供。

### 4.3 `GlobalExceptionHandler` 設計
`@RestControllerAdvice` により例外を `Result` に統一する。

#### 4.3.1 ハンドリング分類
- `BizException`
- `MethodArgumentNotValidException`（`@Valid`）
- `ConstraintViolationException`（`@Validated`）
- `MissingServletRequestParameterException`
- `HttpMessageNotReadableException`
- `HttpRequestMethodNotSupportedException`
- `HttpMediaTypeNotSupportedException`
- `AccessDeniedException`
- `NoResourceFoundException`
- `MaxUploadSizeExceededException`
- `Exception`（その他）

#### 4.3.2 i18n / detail 制御
- `messageSource` で messageKey をロケール解決。
- `prod` プロファイル時は `safeDetail` により detail 非表示。
- `MDC` から `traceId` をログ出力。

#### 4.3.3 注意点（実装ベース）
- Security の `authenticationEntryPoint` / `accessDeniedHandler` は `Result` ではなく固定 JSON（`{"code":401|403,"message":"..."}`）を直接返却する実装。
- 上記経路では `GlobalExceptionHandler` を経由しない。

## 5. 認証・認可仕様（Spring Security / JWT / RBAC）

## 5.1 SecurityFilterChain
- CSRF 無効化。
- セッション管理は `STATELESS`。
- 認証エントリポイント: 401 JSON を直接返却。
- アクセス拒否時: 403 JSON を直接返却。
- URL 制御:
  - `permitAll`: `/api/system/auth/login`, `/error/**`, `/favicon.ico`
  - `authenticated`: `/api/system/auth/**`, `/api/system/**`
  - その他: `permitAll`
- `JwtAuthenticationFilter` を `UsernamePasswordAuthenticationFilter` 前段に追加。
- `@EnableMethodSecurity` 有効（`@PreAuthorize` 使用可）。

## 5.2 JWT 発行・検証
### 5.2.1 トークン発行
ログイン成功時、`JwtTokenProvider.generateToken(LoginUser)` により HS256 JWT を生成。
- issuer: `security.jwt.issuer`（既定 `springboot3web`）
- subject: `userId`
- 有効期限: `security.jwt.expire-seconds`（既定 7200 秒、最低 60 秒）
- claims:
  - `roles`: ロール名カンマ連結
  - `nickName`: ニックネーム

### 5.2.2 トークン検証
`JwtAuthenticationFilter` にて `Authorization: Bearer <token>` を解釈し、`JwtTokenProvider.validate()` で署名・issuer・期限を検証。

### 5.2.3 認証コンテキスト設定
検証成功時:
1. `userId` を token の subject から取得
2. `UserAuthorityProvider.loadPermissionCodes(userId)` で権限コード取得
3. 権限コードを `SimpleGrantedAuthority` 化（null/空文字除外、trim、distinct）
4. `LoginPrincipal(userId)` を principal として `SecurityContext` に設定

### 5.2.4 フィルタ除外パス
`JwtAuthenticationFilter.shouldNotFilter` で以下を除外:
- `/api/system/auth/`
- `/error/`
- `/favicon.ico`

要確認:
- `/api/system/auth/**` は SecurityConfig で認証必須だが、同時に JWT フィルタ除外対象。`/api/system/auth/me` 等の認証成立経路（別フィルタ/別実装の有無）は追加確認が必要。

## 5.3 RBAC 仕様
- 抽象 IF: `UserAuthorityProvider`（framework）
- 実装: `SystemUserAuthorityProvider`（system module）
- 権限取得元: `PermissionService.selectPermissionCodesByUserId(userId)`
- Spring Security 連携: 取得した permission code を `GrantedAuthority` として設定
- メソッド認可実装例:
  - `RoleController#page` に `@PreAuthorize("hasAuthority('sys:role:pageList')")`

## 6. ページング仕様（実装ベース）

## 6.1 `PageRequest`
- 項目: `pageNum`, `pageSize`（Long）
- 用途: Controller 引数および検索 DTO の基底クラス

## 6.2 `PageUtil` 補正ロジック
`PageRequest` から MyBatis-Plus `Page<T>` に変換する際、以下を適用。
- `pageNum` が null または 0 以下: `defaultPageNum` を採用
- `pageSize` が null または 0 以下: `defaultPageSize` を採用
- `pageSize` は `maxPageSize` を上限に丸める

`PageProperties` 既定値:
- `defaultPageNum=1`
- `defaultPageSize=10`
- `maxPageSize=100`

## 6.3 `JoinPageResult<T>`
複数テーブル結合検索向けページ DTO。
- 項目: `records`, `total`, `pageNum`, `pageSize`, `pages`
- `pages` は `ceil(total / pageSize)` で算出
- `pageSize <= 0` 時は `pages=0`

## 7. API 一覧（Controller 実装抽出）

以下は `@RequestMapping` + 各 `@*Mapping` から抽出した実在 API 一覧である。

## 7.1 Admin API

### 7.1.1 認証 API（`/api/system/auth`）

| メソッド | パス | Controller#Method | リクエスト | レスポンス |
|---|---|---|---|---|
| POST | `/api/system/auth/login` | `LoginController#login` | Body: `LoginRequest`（`@Valid`） | `Result<LoginResponse<LoginUser>>` |
| POST | `/api/system/auth/logout` | `LoginController#logout` | なし | `Result<Void>` |
| GET | `/api/system/auth/me` | `LoginController#me` | なし | `Result<LoginUser>` |

仕様補足:
- `login` はレスポンスヘッダ `Authorization: Bearer <token>` を付与し、body にも `accessToken` を返却。
- `me` は `SecurityContext` の principal から userId を解決できない場合 `BizException(UNAUTHORIZED)` を送出。

### 7.1.2 ロール管理 API（`/api/system/role`）

| メソッド | パス | Controller#Method | リクエスト | レスポンス | 認可 |
|---|---|---|---|---|---|
| GET | `/api/system/role/pageList` | `RoleController#page` | Query: `PageRequest`, `RoleQueryRequest` | `Result<JoinPageResult<Role>>` | `hasAuthority('sys:role:pageList')` |
| GET | `/api/system/role/{id}` | `RoleController#detail` | Path: `id` | `Result<Role>` | URL 認証必須 |
| POST | `/api/system/role` | `RoleController#create` | Body: `RoleSaveOrUpdateRequest` | `Result<Long>` | URL 認証必須 |
| PUT | `/api/system/role/{id}` | `RoleController#update` | Path: `id`, Body: `RoleSaveOrUpdateRequest` | `Result<Void>` | URL 認証必須 |
| DELETE | `/api/system/role/{id}` | `RoleController#delete` | Path: `id` | `Result<Void>` | URL 認証必須 |
| PATCH | `/api/system/role/{id}/status` | `RoleController#changeStatus` | Path: `id`, Body: `RoleSaveOrUpdateRequest`（`status` 使用） | `Result<Void>` | URL 認証必須 |

### 7.1.3 ユーザー参照 API

| メソッド | パス | Controller#Method | リクエスト | レスポンス | 認可 |
|---|---|---|---|---|---|
| GET | `/api/users/{id}` | `UserQueryController#get` | Path: `id` | `Result<User>` | SecurityConfig 上は `anyRequest().permitAll()` に該当 |

要確認:
- `UserQueryController` の URL は `/api/system/**` 配下ではないため、現状 SecurityConfig では認証必須になっていない。運用要件上の意図確認が必要。

## 7.2 Portal API

### 7.2.1 ヘルス確認 API

| メソッド | パス | Controller#Method | リクエスト | レスポンス |
|---|---|---|---|---|
| GET | `/api/portal/ping` | `PingController#ping` | なし | `Result<String>`（`"pong"`） |

### 7.2.2 記事 API（`/api/articles`）

| メソッド | パス | Controller#Method | リクエスト | レスポンス |
|---|---|---|---|---|
| POST | `/api/articles/add` | `ArticleCreateController#add` | Body: `ArticleCreateReq`（`@Valid`） | `Result<Long>` |
| GET | `/api/articles/pageList` | `ArticleCreateController#pageList` | Body: `ArticleQueryRequest` | `Result<JoinPageResult<ArticleVo>>` |
| PUT | `/api/articles/update` | `ArticleCreateController#update` | Body: `ArticleUpdateReq`（`@Valid`） | `Result<Boolean>` |
| DELETE | `/api/articles/{id}` | `ArticleCreateController#delete` | Path: `id` | `Result<Boolean>` |

要確認:
- `GET /api/articles/pageList` が `@RequestBody` を受ける実装。HTTP クライアント/プロキシによっては GET body 非対応の場合があるため、運用互換性確認が必要。

## 8. 主要 DTO 仕様（実装参照）

## 8.1 認証系
- `LoginRequest`
  - `accountType`（`AccountType`、必須）
  - `accountValue`（8〜100 文字、必須）
  - `password`（8〜16 文字、必須）
- `LoginResponse<LoginUser>`
  - `accessToken`
  - `user`
- `LoginUser`
  - `userId`, `accountId`, `nickName`, `accountType`, `accountValue`, `roleNames`

## 8.2 ロール系
- `RoleQueryRequest`: `keyword`, `status`
- `RoleSaveOrUpdateRequest`: `code`, `name`, `sort`, `status`

## 8.3 記事系
- `ArticleCreateReq`: `title`, `summary`, `content`, `categoryId`, `authorId`, `status`
- `ArticleQueryRequest`（`PageRequest` 継承）: `pageNum`, `pageSize`, `title`, `status`, `categoryId`
- `ArticleUpdateReq`: `id`, `title`, `summary`, `content`, `categoryId`, `status`
- `ArticleVo`: `id`, `title`, `summary`, `content`, `authorName`, `categoryName`, `createdAt`, `updatedAt`

## 9. 実装整合チェック結果
- README 記載の「Admin API（`/admin` 系）/Portal API（`/portal` 系）」は概念説明であり、実装上の URL は `/api/system/**`, `/api/users/**`, `/api/portal/**`, `/api/articles/**`。
- ARCHITECTURE 記載の代表エンドポイント（`/api/system/auth/**`, `/api/system/role/**`, `/api/articles/**`, `/api/portal/ping`）は実装に存在する。
- 例外・認証の横断仕様は `blog-framework` 実装と整合。

## 10. 未確定事項（要確認）
1. `/api/system/auth/**` を JWT フィルタ除外しつつ認証必須としている設計意図（特に `/me`）。
2. `/api/users/{id}` を匿名アクセス可能にしている現行 Security 設計の妥当性。
3. `GET /api/articles/pageList` の Request Body 運用可否（クライアント/ゲートウェイ互換）。
4. ErrorCode のうちファイル/インポート関連コードの実運用 API での利用範囲。
