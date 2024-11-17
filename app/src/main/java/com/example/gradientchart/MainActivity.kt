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
            ChartEntry(0f, 6f),
            ChartEntry(1f, 6f),
            ChartEntry(2f, 2f),
            ChartEntry(3f, 2f),
            ChartEntry(4f, 2f),
            ChartEntry(5f, 2f),
            ChartEntry(6f, 8f),
        )
        binding.chart.submitChartEntries(data)
    }
}