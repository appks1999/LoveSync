package com.akito.lovesync

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * 診断データの基盤モデル
 */
data class ShindanOption(
    val text: String,
    val scores: Map<String, Int> // カテゴリID -> 加算ポイント (+2, +1, 0, -1など)
)

data class ShindanQuestion(
    val id: Int,
    val text: String,
    val options: List<ShindanOption>
)

data class ShindanResult(
    val categoryId: String,
    val title: String,
    val subtitle: String, // リボン部分のテキスト
    val englishName: String,
    val description: String,
    val lovePurity: Int, // 0-100
    val chartScores: Map<String, Int> // レーダーチャート用: 愛情深さ, 一途さ, 安心感, 積極性, 嫉妬深さ
)

data class ShindanData(
    val title: String,
    val questions: List<ShindanQuestion>,
    val results: List<ShindanResult>
)

/**
 * 診断アプリの共通ロジックを担当するViewModel
 */
class ShindanViewModel : ViewModel() {
    private var originalData: ShindanData? = null
    private var lastLimit: Int = 0
    
    // シャッフルされた質問リストをStateとして管理
    private var shuffledQuestions by mutableStateOf<List<ShindanQuestion>>(emptyList())

    // 現在の状態
    var currentQuestionIndex by mutableStateOf(0)
        private set

    var isFinished by mutableStateOf(false)
        private set

    // 各カテゴリの合計スコア
    private val scores = mutableMapOf<String, Int>()

    var finalResult by mutableStateOf<ShindanResult?>(null)
        private set

    /**
     * 初期セットアップ（質問と選択肢をランダム化）
     * @param data 診断データ
     * @param limit 抽出する質問数（0以下の場合は全件）
     */
    fun setup(data: ShindanData, limit: Int = 0) {
        this.originalData = data
        this.lastLimit = limit
        
        // 質問をシャッフルし、さらに各質問の選択肢もシャッフルする
        val shuffled = data.questions.shuffled().map { question ->
            question.copy(options = question.options.shuffled())
        }
        
        this.shuffledQuestions = if (limit > 0) {
            shuffled.take(limit)
        } else {
            shuffled
        }
        
        this.currentQuestionIndex = 0
        this.isFinished = false
        this.scores.clear()
        this.finalResult = null
    }

    val currentQuestion: ShindanQuestion?
        get() = shuffledQuestions.getOrNull(currentQuestionIndex)

    val totalQuestions: Int
        get() = shuffledQuestions.size

    /**
     * 選択肢が選ばれた時の処理
     */
    fun selectOption(option: ShindanOption) {
        // スコアを加算
        option.scores.forEach { (categoryId, point) ->
            scores[categoryId] = (scores[categoryId] ?: 0) + point
        }

        // 次の質問へ進むか、終了判定
        if (currentQuestionIndex < shuffledQuestions.size - 1) {
            currentQuestionIndex++
        } else {
            finishShindan()
        }
    }

    private fun finishShindan() {
        val data = originalData ?: return
        
        // 最もスコアが高いカテゴリを判定
        val winnerCategoryId = scores.maxByOrNull { it.value }?.key
        
        // 結果データを検索
        finalResult = data.results.find { it.categoryId == winnerCategoryId } 
            ?: data.results.firstOrNull()
        
        isFinished = true
    }
    
    fun reset() {
        originalData?.let { setup(it, lastLimit) }
    }
}
