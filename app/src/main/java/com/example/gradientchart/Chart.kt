package com.example.gradientchart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View

class Chart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paintGridLine = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintBottomLine = Paint(Paint.ANTI_ALIAS_FLAG)

    private val paintXText = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintYText = Paint(Paint.ANTI_ALIAS_FLAG)

    private val paintBackgroundLeft = Paint(Paint.ANTI_ALIAS_FLAG)

    private val paintChartLine = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintChartCircleTransparent = Paint(Paint.ANTI_ALIAS_FLAG)

    private val chartColumns = 7f
    private val chartRows = 5f
    private val xLabelTextSize = 30f
    private val yLabelTextSize = 27f
    private val marginTop = 80f
    private val widthBackgroundLeft = 210f
    private val maxYValue = 10f
    private val circleRadius = 12f
    private val circleTransparentRadius = 16f

    private val xLabels = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    private val yLabels = listOf("Extreme", "Very High", "High", "Moderate", "Low")
    private var chartEntries = listOf<ChartEntry>()

    init {
        paintGridLine.apply {
            color = Color.parseColor("#3D3D3D")
            strokeWidth = 2f
            pathEffect = DashPathEffect(floatArrayOf(8f, 6f), 0f)
        }

        paintBottomLine.apply {
            color = Color.parseColor("#939393")
            strokeWidth = 2.5f
        }

        paintXText.apply {
            color = Color.parseColor("#ffffff")
            textSize = xLabelTextSize
            textAlign = Paint.Align.CENTER
        }

        paintYText.apply {
            color = Color.parseColor("#7E7E7E")
            textSize = yLabelTextSize
            textAlign = Paint.Align.RIGHT
        }

        paintBackgroundLeft.apply {
            color = Color.parseColor("#222222")
            style = Paint.Style.FILL
        }

        paintChartLine.apply {
            color = Color.parseColor("#FE556F")
            strokeWidth = 3f
        }

        paintChartCircleTransparent.apply {
            color = Color.parseColor("#FE556F")
            strokeWidth = 3f
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackground(canvas)
        drawBottomLine(canvas)
        drawGridLine(canvas)
        drawXLabels(canvas)
        drawYLabels(canvas)
        drawChartEntries(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawRect(0f, 0f, widthBackgroundLeft, height.toFloat(), paintBackgroundLeft)
    }

    private fun drawBottomLine(canvas: Canvas) {
        canvas.drawLine(0f, height.toFloat(), width.toFloat(), height.toFloat(), paintBottomLine)
    }

    private fun drawGridLine(canvas: Canvas) {
        val columnWidth = (width - widthBackgroundLeft) / chartColumns
        val rowHeight = (height - marginTop) / chartRows
        val marginVertical = columnWidth / 2

        for (i in 0..6) {
            val xPosition = columnWidth * i + marginVertical + widthBackgroundLeft
            val startYPosition = marginTop
            val endYPosition = height.toFloat()
            canvas.drawLine(xPosition, startYPosition, xPosition, endYPosition, paintGridLine)
        }

        for (i in 0..4) {
            val startXPosition = widthBackgroundLeft
            val endXPosition = width.toFloat()
            val yPosition = (i * rowHeight) + marginTop
            canvas.drawLine(startXPosition, yPosition, endXPosition, yPosition, paintGridLine)
        }
    }

    private fun drawXLabels(canvas: Canvas) {
        val columnWidth = (width - widthBackgroundLeft) / chartColumns
        val marginVertical = columnWidth / 2

        for (i in 0..6) {
            val xPosition = columnWidth * i + marginVertical + widthBackgroundLeft
            val yPosition = marginTop - xLabelTextSize
            canvas.drawText(xLabels[i], xPosition, yPosition, paintXText)
        }
    }

    private fun drawYLabels(canvas: Canvas) {
        val rowHeight = (height - marginTop) / chartRows

        for (i in 0..4) {
            val xPosition = widthBackgroundLeft - 20f
            val yPosition = (i * rowHeight) + marginTop + yLabelTextSize / 3
            canvas.drawText(yLabels[i], xPosition, yPosition, paintYText)
        }
    }

    private fun drawChartEntries(canvas: Canvas) {
        if (chartEntries.isEmpty()) return

        val columnWidth = (width - widthBackgroundLeft) / chartColumns
        val marginVertical = columnWidth / 2
        var point = PointF(0f, 0f)

        for (i in chartEntries.indices) {
            val x = widthBackgroundLeft + (columnWidth * i) + marginVertical
            val y = height - ((chartEntries[i].yValue / maxYValue) * (height - marginTop))

            if (i > 0) {
                val gradient = LinearGradient(point.x, point.y, x, y, getColor(chartEntries[i - 1].yValue),
                    getColor(chartEntries[i].yValue),
                    Shader.TileMode.CLAMP
                )
                paintChartLine.shader = gradient

                val gradientTrans = LinearGradient(point.x, point.y, x, y,
                    com.example.gradientchart.Color.generateTransparentColor(
                        getColor(chartEntries[i - 1].yValue), 0.5
                    ),
                    com.example.gradientchart.Color.generateTransparentColor(
                        getColor(chartEntries[i].yValue), 0.5
                    ),
                    Shader.TileMode.CLAMP
                )
                paintChartCircleTransparent.shader = gradientTrans
                canvas.drawCircle(x, y, circleTransparentRadius, paintChartCircleTransparent)

                canvas.drawLine(point.x, point.y, x, y, paintChartLine)
            } else {
                val gradient = LinearGradient(point.x, point.y, x, y,
                    getColor(chartEntries[i].yValue),
                    getColor(chartEntries[i].yValue),
                    Shader.TileMode.CLAMP
                )
                paintChartLine.shader = gradient

                val gradientTrans = LinearGradient(point.x, point.y, x, y,
                    com.example.gradientchart.Color.generateTransparentColor(
                        getColor(chartEntries[i].yValue), 0.5
                    ),
                    com.example.gradientchart.Color.generateTransparentColor(
                        getColor(chartEntries[i].yValue), 0.5
                    ),
                    Shader.TileMode.CLAMP
                )
                paintChartCircleTransparent.shader = gradientTrans
                canvas.drawCircle(x, y, circleTransparentRadius, paintChartCircleTransparent)
            }

            canvas.drawCircle(x, y, circleRadius, paintChartLine)

            point = PointF(x, y)
        }
    }

    private fun getColor(yValue: Float): Int {
        val colors = intArrayOf(
            Color.parseColor("#43E34F"),
            Color.parseColor("#F5DC4E"),
            Color.parseColor("#FE7650"),
            Color.parseColor("#F63838"),
            Color.parseColor("#8953F1")
        )

        return when (yValue) {
            2f -> colors[0]
            4f -> colors[1]
            6f -> colors[2]
            8f -> colors[3]
            10f -> colors[4]
            else -> Color.BLACK
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    fun submitChartEntries(chartEntries: List<ChartEntry>) {
        this.chartEntries = chartEntries
        invalidate()
    }
}