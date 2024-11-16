package com.example.gradientchart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gradientchart.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = listOf(
            DataPoint(0f, 6f),
            DataPoint(1f, 6f),
            DataPoint(2f, 2f),
            DataPoint(3f, 2f),
            DataPoint(4f, 2f),
            DataPoint(5f, 2f),
            DataPoint(6f, 8f),
        )
        binding.chart.submitDataPoints(data)
    }
}