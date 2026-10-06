# LoveSync 診断アプリ プロジェクト流用・複製ガイドライン (REUSE_GUIDE.md)

本ドキュメントは、本プロジェクト（LoveSync）のモジュール化されたソースコードアーキテクチャおよび UI コンポーネントをベースにして、新規の診断アプリ・相性チェックアプリを複製・新規作成する際の手順書です。

---

## 1. 流用（そのまま再利用）できるコンポーネント・モジュール一覧

本プロジェクトは高再利用性・保守性を考慮してパッケージ分割されています。以下のコード・モジュールは構造を変えずにそのまま流用可能です。

| パッケージ / ファイル | 流用可能な機能・役割 |
| --- | --- |
| **`ui/components/RefinedProgressBar.kt`** | 動的アニメーション付き進捗バー。質問進行状況（`01 / 10`）および進捗率バッジ（`10%`）の自動描画。 |
| **`ui/components/OptionCard.kt`** | A / B / C / D の選択肢回答カード。タップアニメーション、複数行テキスト自動拡張機能。 |
| **`ui/components/AnimatedTextButton.kt`** | 「やめる」ボタンなどの押し込みアニメーション付きボタン。 |
| **`ui/components/BannerAdView.kt`** | 画面最下部に常時表示される AdMob バナー広告 View。 |
| **`ui/components/LoveRadarChart.kt`** | 結果画面用の5軸ペンタゴン・レーダーチャート Canvas。 |
| **`ui/home/ModeSelectionScreen.kt`** | レスポンシブ対応ホーム画面レイアウト、BGM/SE 切り替えボタン。 |
| **`ui/home/ModeCard.kt`** | グラデーション・ドロップシャドウ・タップアニメーション付きモード選択カード。 |
| **`ui/solo/SoloShindanScreen.kt`** | 一人用診断専用 Scaffold コンテナ & プログレスバー連動制御。 |
| **`ui/solo/SoloQuestionScreen.kt`** | 設問カード、選択肢並列表示、小型スマホ (`<560dp`) 自動縮小レスポンシブ制御。 |
| **`ui/matching/MatchingShindanScreen.kt`** | 二人用相性チェック Scaffold コンテナ。 |
| **`ui/matching/MatchingQuestionScreen.kt`** | プレイヤー1・プレイヤー2のターン交代インジケーター & 交互回答記録制御。 |
| **`ShindanLogic.kt` (`ShindanViewModel`)** | 一人用診断のランダム抽出、回答記録、スコア集計ロジック。 |
| **`MatchingViewModel.kt`** | 二人用ターンスイッチ、一致点数（100点満点）算出、カテゴリ分解ロジック。 |
| **`AdManager.kt`** | AdMob インタースティシャル・リワード広告読み込み＆BGM一時停止・再開制御。 |
| **`SoundManager.kt` / `SettingsManager.kt`** | SoundPool による効果音再生および SharedPreferences 永続化。 |

---

## 2. リセット・変更が必要な箇所一覧

新アプリを作成する場合は、以下の項目を**必ずリセットまたは新アプリ用に差し替えて**ください。

### 2.1 パッケージ名 & Application ID の変更
- **`app/build.gradle.kts`**:
  ```kotlin
  android {
      namespace = "com.yourcompany.newapp" // 新アプリのパッケージ名
      defaultConfig {
          applicationId = "com.yourcompany.newapp" // 新アプリの Application ID
      }
  }
  ```
- AndroidManifest.xml およびすべての `.kt` ファイル先頭の `package com.akito.lovesync` を新パッケージ名へ一括リファクタリング。

### 2.2 バージョン管理情報のリセット
新規アプリとしてリリースするため、ビルド番号とバージョン名をリセットします。
- **`app/build.gradle.kts`**:
  ```kotlin
  versionCode = 1       // 1 にリセット
  versionName = "1.0"   // "1.0" にリセット
  ```

### 2.3 診断問題・結果データ (`data/` パッケージ) の差し替え
- **`data/LoveShindanContent.kt`**:
  - 新アプリのテーマに合わせた質問文（30問等）および選択肢・スコア割り当ての差し替え。
  - 診断結果タイプ（10種類等）のタイトル・説明文・英語名・純粋度パラメータの差し替え。
- **`data/MatchingShindanContent.kt`**:
  - 二人用相性チェック質問文（20問等）および選択肢の差し替え。

### 2.4 アセット（画像・音声・フォント）の差し替え
- **画像 (`app/src/main/res/drawable/`)**:
  - キャラクター画像（動物等）、メインビジュアル (`main_visual.png`)、アイコン画像 (`heart1.png`, `bouquet1.png`, `love_letter.png`, `solo.png`, `pair.png`) の差し替え。
- **アプリアイコン (`app/src/main/res/mipmap/` & `drawable/`)**:
  - Image Asset で新アプリアイコンを生成。（※生成後、XMLファイルの1行目が `<?xml version="1.0" encoding="utf-8"?>` になっているか要確認）
- **音声 (`app/src/main/res/raw/`)**:
  - BGM (`bgm_main.mp3`) および効果音 (`click.wav`, `result.wav` 等) の差し替え。
- **フォント (`app/src/main/res/font/`)**:
  - 新アプリ用の `.ttf` フォントファイルの配置。（※ファイル名は必ず小文字スネークケース `a-z0-9_` に指定）
  - `ui/theme/Type.kt` 内の `Font(R.font.your_font_name)` 参照の差し替え。

### 2.5 AdMob 広告 ID (`AdManager.kt` & `AndroidManifest.xml`)
- **`AndroidManifest.xml`**:
  ```xml
  <meta-data
      android:name="com.google.android.gms.ads.APPLICATION_ID"
      android:value="ca-app-pub-YOUR_ADMOB_APP_ID"/>
  ```
- **`AdManager.kt`**:
  - インタースティシャル広告ユニット ID およびリワード広告ユニット ID の差し替え。

### 2.6 アプリ表示名 (`app/src/main/res/values/strings.xml`)
- ```xml
  <resources>
      <string name="app_name">新アプリ名</string>
  </resources>
  ```

---

## 3. アプリ複製時のチェックリスト (Checklist)

- [ ] パッケージ名 (`applicationId`) を新規設定したか
- [ ] `versionCode = 1`, `versionName = "1.0"` にリセットしたか
- [ ] `data/` 下の設問・診断結果テキストを差し替えたか
- [ ] アプリアイコン、画像、音声、フォントファイルを新素材に更新したか
- [ ] AdMob ID を新アプリ用に差し替えたか
- [ ] `./gradlew assembleDebug` でエラーなくビルドが成功するか確認したか
