package com.marijannovak.autismhelper.data.models

import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.LineData

data class ChartData(var lineData: LineData, var barData: BarData, var dates: List<String>)
