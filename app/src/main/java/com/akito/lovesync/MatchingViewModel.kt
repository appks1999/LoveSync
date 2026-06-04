package com.akito.lovesync

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.akito.lovesync.data.MatchingQuestion
import com.akito.lovesync.data.MatchingShindanContent
import kotlin.math.abs

class MatchingViewModel : ViewModel() {
    // 質問リスト
    var questions by mutableStateOf<List<MatchingQuestion>>(emptyList())
    
    // 現在の状態
    var currentQuestionIndex by mutableStateOf(0)
    var isPlayer2Turn by mutableStateOf(false)
    var isFinished by mutableStateOf(false)
    
    // 名前
    var player1Name by mutableStateOf("あなた")
    var player2Name by mutableStateOf("相手")
    
    // 回答記録 (QuestionID -> (Player1の回答, Player2の回答))
    private val player1Answers = mutableMapOf<Int, String>()
    private val player2Answers = mutableMapOf<Int, String>()
    
    fun setup() {
        // ランダムに10問抽出
        questions = MatchingShindanContent.matchingQuestions.shuffled().take(10)
        currentQuestionIndex = 0
        isPlayer2Turn = false
        isFinished = false
        player1Answers.clear()
        player2Answers.clear()
    }
    
    fun selectOption(option: String) {
        val currentQuestion = questions.getOrNull(currentQuestionIndex) ?: return
        
        if (!isPlayer2Turn) {
            // プレイヤー1の回答
            player1Answers[currentQuestion.id] = option
            isPlayer2Turn = true
        } else {
            // プレイヤー2の回答
            player2Answers[currentQuestion.id] = option
            
            // 次の質問へ
            if (currentQuestionIndex < questions.size - 1) {
                currentQuestionIndex++
                isPlayer2Turn = false
            } else {
                isFinished = true
            }
        }
    }
    
    fun calculateMatchingScore(): Int {
        if (questions.isEmpty()) return 0
        var totalPoints = 0
        questions.forEach { q ->
            val p1Ans = player1Answers[q.id]
            val p2Ans = player2Answers[q.id]
            val p1Idx = q.options.indexOf(p1Ans)
            val p2Idx = q.options.indexOf(p2Ans)
            
            if (p1Idx == p2Idx) {
                totalPoints += 10
            } else if (abs(p1Idx - p2Idx) == 1) {
                totalPoints += 5
            }
        }
        // 10問で100点満点計算
        return totalPoints
    }

    fun getMatchedPoints(): List<String> {
        val points = mutableListOf<String>()
        questions.forEach { q ->
            if (player1Answers[q.id] == player2Answers[q.id]) {
                points.add(q.text)
            }
        }
        return points
    }

    fun getNearMatchedPoints(): List<String> {
        val points = mutableListOf<String>()
        questions.forEach { q ->
            val p1Idx = q.options.indexOf(player1Answers[q.id])
            val p2Idx = q.options.indexOf(player2Answers[q.id])
            if (p1Idx != p2Idx && abs(p1Idx - p2Idx) == 1) {
                points.add(q.text)
            }
        }
        return points
    }

    fun getMismatchPoints(): List<String> {
        val points = mutableListOf<String>()
        questions.forEach { q ->
            val p1Idx = q.options.indexOf(player1Answers[q.id])
            val p2Idx = q.options.indexOf(player2Answers[q.id])
            if (abs(p1Idx - p2Idx) > 1) {
                points.add(q.text)
            }
        }
        return points
    }
}
