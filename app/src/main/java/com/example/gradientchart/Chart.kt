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
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paintGridLine = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintBottomLine = Paint(Paint.ANTI_ALIAS_FLAG)

    private val paintTextX = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintTextY = Paint(Paint.ANTI_ALIAS_FLAG)

    private val paintBackgroundLeft = Paint(Paint.ANTI_ALIAS_FLAG)

    private val paintLineChart = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintLineChartCircle = Paint(Paint.ANTI_ALIAS_FLAG)

    private val chartColumns = 7f
    private val chartRows = 5f
    private val labelTextSize = 30f
    private val marginTop = 80f
    private val widthBackgroundLeft = 210f
    private val maxValueY = 10f

    private var dataPoints = listOf<DataPoint>()

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

        paintTextX.apply {
            color = Color.parseColor("#ffffff")
            textSize = labelTextSize
        }

        paintTextY.apply {
            color = Color.parseColor("#7E7E7E")
            textSize = labelTextSize
            textAlign = Paint.Align.RIGHT
        }

        paintBackgroundLeft.apply {
            color = Color.parseColor("#222222")
            style = Paint.Style.FILL
        }

        paintLineChart.apply {
            color = Color.parseColor("#FE556F")
            strokeWidth = 3f
        }

        paintLineChartCircle.apply {
            color = Color.parseColor("#FE556F")
            strokeWidth = 3f
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackground(canvas)
        drawBottomLine(canvas)
        drawGridLine(canvas)
        drawLabelX(canvas)
        drawLabelY(canvas)
        drawDataChart(canvas)
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
            val x = columnWidth * i
            canvas.drawLine(x + marginVertical + widthBackgroundLeft, marginTop, x + marginVertical + widthBackgroundLeft, height.toFloat(), paintGridLine)
        }

        for (i in 0..4) {
            val y = (i * rowHeight) + marginTop
            canvas.drawLine(widthBackgroundLeft, y, width.toFloat(), y, paintGridLine)
        }
    }

    private fun drawLabelX(canvas: Canvas) {
        val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        val columnWidth = (width - widthBackgroundLeft) / chartColumns
        val marginVertical = columnWidth / 2
        for (i in 0..6) {
            val label = daysOfWeek[i]
            val xPosition = columnWidth * i + marginVertical - labelTextSize + widthBackgroundLeft
            canvas.drawText(label, xPosition, (marginTop + labelTextSize) / 2, paintTextX)
        }
    }

    private fun drawLabelY(canvas: Canvas) {
        val labels = listOf("Extreme", "Very High", "High", "Moderate", "Low")
        val rowHeight = (height - marginTop) / chartRows

        for (i in 0..4) {
            val y = (i * rowHeight) + marginTop + (labelTextSize / 3)
            canvas.drawText(labels[i], widthBackgroundLeft - 20f, y, paintTextY)
        }
    }

    private fun drawDataChart(canvas: Canvas) {
        if (dataPoints.isEmpty()) return

        val columnWidth = (width - widthBackgroundLeft) / chartColumns
        var point = PointF(0f, 0f)

        for (i in dataPoints.indices) {
            val x = widthBackgroundLeft + (columnWidth * i) + (columnWidth / 2)
            val y = height - ((dataPoints[i].yValue / maxValueY) * (height - marginTop))

            if (i > 0) {
                val gradient = LinearGradient(point.x, point.y, x, y, getColor(dataPoints[i - 1].yValue),
                    getColor(dataPoints[i].yValue),
                    Shader.TileMode.CLAMP
                )
                paintLineChart.shader = gradient

                val gradientTrans = LinearGradient(point.x, point.y, x, y,
                    com.example.gradientchart.Color.generateTransparentColor(
                        getColor(dataPoints[i - 1].yValue), 0.5
                    ),
                    com.example.gradientchart.Color.generateTransparentColor(
                        getColor(dataPoints[i].yValue), 0.5
                    ),
                    Shader.TileMode.CLAMP
                )
                paintLineChartCircle.shader = gradientTrans
                canvas.drawCircle(x, y, 16f, paintLineChartCircle)

                canvas.drawLine(point.x, point.y, x, y, paintLineChart)
            } else {
                val gradient = LinearGradient(point.x, point.y, x, y,
                    getColor(dataPoints[i].yValue),
                    getColor(dataPoints[i].yValue),
                    Shader.TileMode.CLAMP
                )
                paintLineChart.shader = gradient

                val gradientTrans = LinearGradient(point.x, point.y, x, y,
                    com.example.gradientchart.Color.generateTransparentColor(
                        getColor(dataPoints[i].yValue), 0.5
                    ),
                    com.example.gradientchart.Color.generateTransparentColor(
                        getColor(dataPoints[i].yValue), 0.5
                    ),
                    Shader.TileMode.CLAMP
                )
                paintLineChartCircle.shader = gradientTrans
                canvas.drawCircle(x, y, 16f, paintLineChartCircle)
            }

            canvas.drawCircle(x, y, 12f, paintLineChart)

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

    fun submitDataPoints(dataPoints: List<DataPoint>) {
        this.dataPoints = dataPoints
        invalidate()
    }
}