package com.example.customseekbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar

class CustomSeekBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatSeekBar(context, attrs, defStyle) {

    private val tickCount = max
    private val tickTextArray = arrayOf("1", "2", "3", "4", "5", "6")

    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 25f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // SeekBar의 배경 그리기
        if (background != null) {
            background.setBounds(
                paddingLeft,
                paddingTop,
                width - paddingRight,
                height - paddingBottom
            )
            background.draw(canvas)
        }

        // progress bar의 전체 길이와 각 틱의 길이 계산
        val progressBarLength = width - paddingLeft - paddingRight
        val tickLength = progressBarLength / (tickCount - 1).toFloat()

        // 각 틱 아래에 텍스트 그리기
        for (i in 0 until tickCount) {
            val tickX = i * tickLength + paddingLeft
            val textX = tickX - textPaint.measureText(tickTextArray[i]) / 2
            val textY = height - paddingBottom + textPaint.textSize + 10f
            canvas.drawText(tickTextArray[i], textX, textY, textPaint)
        }



        // tick 그리기
        for (i in 0 until tickCount) {
            val tickX = i * tickLength + paddingLeft
            canvas.drawLine(
                tickX.toFloat(),
                (height - paddingBottom).toFloat(),
                tickX.toFloat(),
                (height - paddingBottom - 30).toFloat(),
                textPaint
            )
        }
    }
}