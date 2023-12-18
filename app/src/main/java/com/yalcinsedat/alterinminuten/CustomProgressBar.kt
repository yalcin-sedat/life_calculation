package com.yalcinsedat.alterinminuten

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.ProgressBar

class CustomProgressBar: ProgressBar {
    private val progressPaint =Paint()

    constructor(context: Context) :super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    init {
        // İlerleme çubuğunun rengini ve diğer özelliklerini burada özelleştirebilirsiniz
        progressPaint.color = Color.RED
        progressPaint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas?.let {
            // Özelleştirilmiş ilerleme çubuğunu burada çizebilirsiniz
            val progressBarWidth = width - paddingLeft - paddingRight
            val progressBarHeight = height - paddingTop - paddingBottom
            val progress = progressBarWidth * progress / max

            canvas.drawRect(
                paddingLeft.toFloat(),
                paddingTop.toFloat(),
                (paddingLeft + progress).toFloat(),
                (paddingTop + progressBarHeight).toFloat(),
                progressPaint
            )


            val barRect = RectF(
                paddingLeft.toFloat(),
                paddingTop.toFloat(),
                (paddingLeft + progress).toFloat(),
                (paddingTop + progressBarHeight).toFloat()
            )
            canvas.drawRoundRect(barRect, 10f, 10f, progressPaint) // Yuvarlatılmış köşeleri olan bir dikdörtgen çizer

        }
    }

}