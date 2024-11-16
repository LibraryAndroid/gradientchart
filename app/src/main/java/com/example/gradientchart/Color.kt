package com.example.gradientchart

import androidx.core.graphics.ColorUtils
import kotlin.math.roundToInt

object Color {
    fun generateTransparentColor(color: Int, alpha: Double?): Int {
        val defaultAlpha = 255
        val colorAlpha = alpha?.times(defaultAlpha)?.roundToInt() ?: defaultAlpha
        return ColorUtils.setAlphaComponent(color, colorAlpha)
    }
}