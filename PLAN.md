# LoveSync (あなたの恋愛カルテ) アプリ設計・構成計画書 (PLAN.md)

本ドキュメントは、LoveSync アプリの全体構造、パッケージ設計、各画面・コンポーネントの役割およびデータフローをまとめた設計計画書です。

---

## 1. 全体パッケージ・ディレクトリ構造

```
com.akito.lovesync/
├── MainActivity.kt                # メインエントリーポイント（Edge-to-Edge・BGM・AdMob初期化・画面遷移ルート）
├── ShindanLogic.kt                # 一人用診断データモデル & ShindanViewModel
├── MatchingViewModel.kt           # 二人用相性チェック ViewModel
├── AdManager.kt                   # AdMob (インタースティシャル・リワード広告) 管理
├── SoundManager.kt                # 効果音 (SoundPool) 管理
├── SettingsManager.kt             # 設定値 (SharedPreferences) 管理
│
├── data/
│   ├── LoveShindanContent.kt      # 一人用診断の問題（30問）・結果（10タイプ）定義
│   └── MatchingShindanContent.kt  # 二人用相性チェックの問題（20問）定義
│
└── ui/
    ├── theme/
    │   ├── Color.kt               # アプリ共通カラー定義
    │   ├── Theme.kt               # Compose LoveSyncTheme 定義
    │   └── Type.kt                # フォント（Zen Maru Gothic）& Typography 定義
    │
    ├── components/                # 複数画面で再利用される共通 UI コンポーネント
    │   ├── BannerAdView.kt        # 最下部常時表示の AdMob バナー広告 View
    │   ├── AnimatedTextButton.kt  # 押し込みアニメーション付きテキストボタン（「やめる」ボタン等）
    │   ├── OptionCard.kt          # 診断回答用選択肢カード（A, B, C, D）
    │   └── LoveRadarChart.kt      # 結果画面用ペンタゴン・レーダーチャート Canvas
    │
    ├── home/                      # ホーム画面パッケージ
    │   ├── ModeSelectionScreen.kt # ホーム画面レイアウト（タイトル、イラスト、音響ボタン）
    │   └── ModeCard.kt            # モード選択カード（「一人でじっくり」「二人で相性」）
    │
    ├── solo/                      # 一人用診断パッケージ
    │   ├── SoloShindanScreen.kt   # Scaffold コンテナ & プログレスバー
    │   ├── SoloQuestionScreen.kt  # 一人用設問カード & 選択肢表示画面
    │   └── SoloResultScreen.kt    # 一人用診断結果画面 (キャラ表示・純粋度・レーダーチャート・シェア)
    │
    └── matching/                  # 二人用相性チェックパッケージ
        ├── MatchingShindanScreen.kt  # Scaffold コンテナ & プログレスバー
        ├── MatchingQuestionScreen.kt # プレイヤー交互ターン & 設問画面
        ├── MatchingResultScreen.kt   # 相性スコア (%) & フィードバック表示
        └── MatchingCategoryCard.kt   # 結果カテゴリ分解表示カード（ぴったり・近い・伸びしろ）
```

---

## 2. 各モジュール・画面の役割とデータフロー

### 2.1 エントリーポイント & インフラ層
- **`MainActivity.kt`**: アプリ起動時の Edge-to-Edge 設定、BGM `MediaPlayer` のループ再生制御、AdMob 初期化。`MainContainer` を通じて `currentMode` ("SOLO" / "MATCHING" / `null`) による画面切替を統括。
- **`AdManager.kt`**: インタースティシャル広告およびリワード広告の読み込み・表示と、広告再生中の BGM 自動一時停止・再開連携。
- **`SoundManager.kt` / `SettingsManager.kt`**: 効果音（クリック音、結果表示音）の `SoundPool` 再生と SharedPreferences 永続化。

### 2.2 ホーム画面 (`ui/home/`)
- **`ModeSelectionScreen.kt`**: `BoxWithConstraints` を活用し、`screenHeight` に応じた動的タイポグラフィ（40sp〜56sp）とイラスト高さを適用。BGM/SE 切り替えボタンをステータスバー下に安全に配置。
- **`ModeCard.kt`**: スケーリングアニメーション付きの「一人でじっくり診断」「二人で相性チェック」カード。

### 2.3 一人用診断 (`ui/solo/`)
- **`SoloShindanScreen.kt`**: Scaffold 上部バー（やめるボタン、プログレスバー）と `viewModel.isFinished` に応じた画面切替（設問 ↔ 結果）。
- **`SoloQuestionScreen.kt`**: ランダム抽出された 10 問の質問を表示。レスポンシブなカード余白 (`20dp`〜`48dp`) と選択肢高さ (`58dp`〜`80dp`) で描画。
- **`SoloResultScreen.kt`**: 動物キャラクター画像、純粋度プログレス、リワード広告解放型レーダーチャート (`LoveRadarChart`)、SNS シェア機能。

### 2.4 二人用相性チェック (`ui/matching/`)
- **`MatchingShindanScreen.kt`**: 相性チェック専用 Scaffold コンテナ。
- **`MatchingQuestionScreen.kt`**: プレイヤー1とプレイヤー2の交互ターンインジケーター（「あなたの番」「相手の番」）を表示し、非公開で回答を記録。
- **`MatchingResultScreen.kt`**: 2人の回答一致度から 100 点満点の相性スコアを算出。`MatchingCategoryCard` で価値観の一致・不一致をポイント分析。

---

## 3. レスポンシブ & 表示設計方針
1. **通常スマホ (Medium / Large Phone)**: 100% オリジナルのリッチでゆったりとしたカードサイズ（質問カード内余白 `48dp`、選択肢カード `80dp`、フォント `21sp`）、マージンを使用し、自然なスクロール形式で美しく表示。
2. **小型スマホ (Small Phone `<560dp`)**: 上部バーのタイトル・ボタン被りを防ぐ flex `Row` 構造を適用し、質問カード・選択肢カードを「ややコンパクト」な設定値（質問カード内余白 `20dp`、選択肢カード `58dp`）に自動切り替え。
