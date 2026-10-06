# LoveSync (あなたの恋愛カルテ) 開発・保守ガイドライン

本ドキュメントは、本プロジェクト（`lovesync`）における今後の保守、機能追加、改修時に遵守すべき開発ルールおよびコード構成のガイドラインです。

---

## 1. 最重要原則（基本ルール）

### 1.1 既存コードの大幅な変更時の事前確認ルール
- **確認必須:** 以下の変更を行う場合は、コードを変更する前に必ず**変更の目的・影響範囲・変更案**を整理し、ユーザー（プロジェクト所有者）へ事前に確認・承認を得てください。
  - ViewModel や主要データ構造（`ShindanData`, `ShindanResult`, `MatchingQuestion` 等）の破壊的変更・インターフェース変更
  - 画面遷移フローや主要 UI コンポーネント（`MainContainer`, `SoloShindanScreen`, `MatchingShindanScreen` 等）の全体的な再構築・大幅なレイアウト変更
  - 広告処理 (`AdManager`) や 音響処理 (`SoundManager`, `SettingsManager`, BGM制御) の連携ロジックの変更
  - 広告ユニット ID やパッケージ名、アプリ設定ファイル (`build.gradle.kts`, `AndroidManifest.xml`) の改変
- **小規模修正:** バグ修正、文言修正、軽微なデザイン調整などは、修正内容と理由を明記した上で実施可能です。

### 1.2 新規機能追加時の説明ルール
- 新規機能や新しい ViewModel / Data クラス、画面を追加した際は、作業完了時に以下の内容を説明してください。
  1. **追加機能の概要**（何をする機能か）
  2. **追加・変更したファイルとコードの位置**
  3. **既存機能への影響および連携部分**（例：既存の State や AdManager への依存関係など）
  4. **動作確認手順および検証結果**

---

## 2. アプリ構成と責務ルール

| モジュール / パッケージ / ファイル | 主な役割・責務 | 注意点・開発ルール |
| --- | --- | --- |
| `MainActivity.kt` | メインエントリーポイント、BGM管理、`MainContainer` 画面遷移ナビゲーション | 軽量エントリーポイント（約120行）。画面描画は `ui/` 下の個別コンポーネントへ委譲する。 |
| `ShindanLogic.kt` | 一人用診断のデータモデル & `ShindanViewModel` | 質問のランダム抽出・シャッフル、選択肢の計算ロジック。UI状態管理は Compose `mutableStateOf` を使用。 |
| `MatchingViewModel.kt` | 二人用相性チェックの ViewModel | 2人の交互ターン制御、回答ログ記録、100点満点スコア計算およびカテゴリ分解。 |
| `AdManager.kt` | AdMob (インタースティシャル・リワード) 管理 | シングルトンオブジェクト。広告表示時には必ず `MainActivity` の BGM (`pauseBgm` / `resumeBgm`) と連動させる。 |
| `SoundManager.kt` | 効果音 (`SoundPool`) 管理 | `SettingsManager` の SE 設定を参照し、設定 OFF 時は再生をスキップする。 |
| `SettingsManager.kt` | 設定値 (`SharedPreferences`) 管理 | BGM / SE の ON/OFF 状態を保持・永続化する。 |
| `data/` パッケージ | 診断問題・結果データの静的定義 | データ追加・修正時は型定義および ID 参照の整合性を維持する。 |
| `ui/components/` パッケージ | 再利用可能な共通 UI コンポーネント (`BannerAdView`, `OptionCard`, `AnimatedTextButton`, `LoveRadarChart`, `RefinedProgressBar`) | 各コンポーネントは独立し、不要な画面固有ステートを持たない純粋な Composable とする。 |
| `ui/home/` パッケージ | ホーム画面 (`ModeSelectionScreen`, `ModeCard`) | `BoxWithConstraints` による動的スケール。音響切り替えボタンはステータスバー下に安全配置。 |
| `ui/solo/` パッケージ | 一人用診断画面群 (`SoloShindanScreen`, `SoloQuestionScreen`, `SoloResultScreen`) | 通常端末は100%オリジナルのカードサイズ、小型端末 (`<560dp`) のみコンパクトに動的スケール。 |
| `ui/matching/` パッケージ | 二人用相性チェック画面群 (`MatchingShindanScreen`, `MatchingQuestionScreen`, `MatchingResultScreen`, `MatchingCategoryCard`) | プレイヤー交互ターン制御（指定カラー `#8A98A5` / `#B3A3AA`）と分析結果カード。 |

---

## 3. テーマ・アセット・品質管理規約

1. **デザインテーマ共通カラー規約**
   - **背景色**: アプリ全画面統一 `#F8F5F0` (`SoloBackground`)
   - **テキスト色**: アプリ全画面統一 `#3A3533` (`SoloTextColor`)
   - **ホームボタン背景**: コーラル〜クリームグラデーション (`#F4A090` ➔ `#FFF3ED`) ＆ ドロップシャドウ (`rgba(220,150,140,0.25)`)
2. **フォントアセット管理規約 (`res/font/`)**
   - 日本語フォントは Google Play Services のオンラインダウンロードに依存せず、**`res/font/` 配下にローカル TTF ファイルとして同梱**する。
   - フォントファイル名は AAPT 規則に従い、必ず**小文字スネークケース (`a-z0-9_`)** で指定する。（例: `m_plus_rounded_1c_bold.ttf`, `kyoka.ttf`, `mintyo.ttf`）
3. **XML リソース管理規約**
   - Image Asset 等でアイコン XML を自動生成・編集した後は、必ず **`<?xml version="1.0" encoding="utf-8"?>` 宣言が XML ファイルの第 1 行目にあること**を確認する（コメント行を 1 行目におかない）。
4. **ビルド検証規約**
   - コード変更後は必ず Gradle ビルド（`assembleDebug` 等）を実行し、コンパイルエラーがないことを確認する。
