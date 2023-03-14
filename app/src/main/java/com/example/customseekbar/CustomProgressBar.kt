package com.example.customseekbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.ProgressBar
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.core.content.ContextCompat
import kotlin.math.roundToInt

class CustomProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ProgressBar(context, attrs, defStyle) {

    private val tickCount = 6
    private val tickTextArray = arrayOf("OFF", "5초", "10초", "20초", "30초", "60초")

    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 25f
    }

    private var touchDownX = 0f

    private val thumbDrawable = ContextCompat.getDrawable(context, R.drawable.drw_thumb_seekbar)!!
    private val progressDrawable =
        ContextCompat.getDrawable(context, R.drawable.drw_progress_seekbar)!!

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchDownX = event.x
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val touchX = event.x
                val progressBarLength = width - paddingLeft - paddingRight
                val touchProgress =
                    (max.toFloat() * (touchX - paddingLeft) / progressBarLength).toInt()
                progress = touchProgress
                return true
            }
            MotionEvent.ACTION_UP -> {
                val touchX = event.x
                val progressBarLength = width - paddingLeft - paddingRight
                val touchProgress =
                    (max.toFloat() * (touchX - paddingLeft) / progressBarLength).toInt()
                val nearestTick = getNearestTick(touchProgress)
                progress = nearestTick
                return true
            }
            else -> return super.onTouchEvent(event)
        }
    }

    private fun getNearestTick(progress: Int): Int {
        val tickLength = max / (tickCount - 1).toFloat()
        val nearestTick = (progress / tickLength).roundToInt() * tickLength
        return nearestTick.coerceIn(0F, max.toFloat()).roundToInt()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // ProgressBar의 배경 그리기
        if (background != null) {
            background.setBounds(0, paddingTop, width - paddingRight / 2, height - paddingBottom)
            background.draw(canvas)
        }

        // progress bar의 전체 길이와 각 틱의 길이 계산
        val progressBarLength = width - paddingLeft - paddingRight
        val tickLength = progressBarLength / (tickCount - 1).toFloat()

        // thumb 그리기
        val thumbPos = (progress.toFloat() / max) * progressBarLength + paddingLeft
        val thumbHalfWidth = thumbDrawable.intrinsicWidth / 2
        val thumbTop =
            paddingTop + (height - paddingTop - paddingBottom) / 2 - thumbDrawable.intrinsicHeight / 2
        thumbDrawable.setBounds(
            thumbPos.toInt() - thumbHalfWidth,
            thumbTop,
            thumbPos.toInt() + thumbHalfWidth,
            thumbTop + thumbDrawable.intrinsicHeight
        )

        // tick 그리기
        for (i in 0 until tickCount) {
            val tickX = i * tickLength + paddingLeft
            canvas.drawLine(
                tickX,
                (height - paddingBottom + 8).toFloat(),
                tickX,
                (height - paddingBottom + thumbHalfWidth).toFloat(), textPaint
            )
            val textX = tickX - textPaint.measureText(tickTextArray[i]) / 2
            val textY = (height - paddingBottom + thumbHalfWidth).toFloat() + textPaint.textSize
            canvas.drawText(tickTextArray[i], textX, textY, textPaint)
        }

        // progress 그리기
        val progressPos = (progress.toFloat() / max) * progressBarLength + paddingLeft
        val progressTop =
            paddingTop + (height - paddingTop - paddingBottom) / 2
        Log.d("TEST","pos -> $progressPos // top -> $top")
        progressDrawable.setBounds(0, progressTop, progressPos.toInt(), height - paddingBottom)
        progressDrawable.draw(canvas)
        thumbDrawable.draw(canvas)
    }
}
