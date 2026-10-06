package com.akito.lovesync.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akito.lovesync.ui.theme.SubtitleColor
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun LoveRadarChart(scores: Map<String, Int>) {
    val labels = listOf("愛情深さ", "一途さ", "安心感", "積極性", "嫉妬深さ")
    val dataKeys = listOf("love", "loyalty", "security", "proactivity", "jealousy")
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.2f)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.minDimension / 2.5f
            
            // Draw background pentagon lines (5 levels)
            for (i in 1..5) {
                val r = radius * (i / 5f)
                val path = Path()
                for (j in 0 until 5) {
                    val angle = Math.toRadians(j * 72.0 - 90.0)
                    val x = center.x + r * cos(angle).toFloat()
                    val y = center.y + r * sin(angle).toFloat()
                    if (j == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                path.close()
                drawPath(path, Color.LightGray.copy(alpha = 0.3f), style = Stroke(width = 1.dp.toPx()))
            }
            
            // Draw axis lines
            for (j in 0 until 5) {
                val angle = Math.toRadians(j * 72.0 - 90.0)
                val x = center.x + radius * cos(angle).toFloat()
                val y = center.y + radius * sin(angle).toFloat()
                drawLine(Color.LightGray.copy(alpha = 0.3f), center, Offset(x, y), strokeWidth = 1.dp.toPx())
            }
            
            // Draw data polygon
            val dataPath = Path()
            for (j in 0 until 5) {
                val score = scores[dataKeys[j]] ?: 50
                val r = radius * (score / 100f)
                val angle = Math.toRadians(j * 72.0 - 90.0)
                val x = center.x + r * cos(angle).toFloat()
                val y = center.y + r * sin(angle).toFloat()
                if (j == 0) dataPath.moveTo(x, y) else dataPath.lineTo(x, y)
            }
            dataPath.close()
            drawPath(dataPath, Color(0xFFFF80AB).copy(alpha = 0.3f), style = Fill)
            drawPath(dataPath, Color(0xFFFF80AB), style = Stroke(width = 2.dp.toPx()))
        }
        
        // Simplified labels display around the chart
        Box(modifier = Modifier.fillMaxSize()) {
            Text(labels[0], modifier = Modifier.align(Alignment.TopCenter).padding(top = 0.dp), fontSize = 10.sp, color = SubtitleColor)
            Text(labels[1], modifier = Modifier.align(Alignment.CenterEnd).padding(end = 0.dp), fontSize = 10.sp, color = SubtitleColor)
            Text(labels[2], modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 0.dp, end = 20.dp), fontSize = 10.sp, color = SubtitleColor)
            Text(labels[3], modifier = Modifier.align(Alignment.BottomStart).padding(bottom = 0.dp, start = 20.dp), fontSize = 10.sp, color = SubtitleColor)
            Text(labels[4], modifier = Modifier.align(Alignment.CenterStart).padding(start = 0.dp), fontSize = 10.sp, color = SubtitleColor)
        }
    }
}
