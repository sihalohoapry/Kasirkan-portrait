package com.sihaloho.cashierapp.ui.chart

import android.app.Dialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateUtils
import android.widget.Toast
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.sihaloho.cashierapp.R
import com.sihaloho.cashierapp.databinding.ActivityChartBinding
import com.sihaloho.cashierapp.retrofit.ApiConfig
import com.sihaloho.cashierapp.retrofit.response.chart.ChartResponse
import com.sihaloho.cashierapp.utils.CalendarUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChartBinding
    private val api by lazy { ApiConfig.getApi() }
    private var progressBar: Dialog? = null
    private var transactionEntry = ArrayList<BarEntry>()
    private var dateValue = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
        isLoading()
    }

    private fun isLoading() {
        progressBar = Dialog(this)
        val layout = layoutInflater.inflate(R.layout.dialog_loading, null)
        progressBar.let {
            it?.setContentView(layout)
            it?.setCancelable(false)
            it?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    override fun onStart() {
        super.onStart()
        listChart()
    }

    private fun listChart() {
        val calendar = CalendarUtils
        progressBar?.show()
        api.chart(tahun = calendar.year.toString()).enqueue(object : Callback<ChartResponse> {
            override fun onResponse(call: Call<ChartResponse>, response: Response<ChartResponse>) {
                if (response.isSuccessful) {
                    progressBar?.dismiss()
                    var index = 0
                    val data = response.body()?.data
                    if (data != null) {
                        dateValue.add("0")
                        for (chart in data) {
                            index++
                            transactionEntry.add(BarEntry(index.toFloat(), chart.data.toFloat()))
                            dateValue.add(chart.nama_bulan)
                        }
                        barChart(transactionEntry, dateValue)

                    }
                }

            }

            override fun onFailure(call: Call<ChartResponse>, t: Throwable) {
                progressBar?.dismiss()
                Toast.makeText(this@ChartActivity, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun barChart(transactionEntry: ArrayList<BarEntry>, dateValue: ArrayList<String>) {

        val legend = binding.barChart.legend
        legend.isEnabled = true
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)


        val barDataSet = BarDataSet(transactionEntry, "Penjualan ${CalendarUtils.year}")
        barDataSet.color = Color.CYAN

        binding.barChart.description.isEnabled = false
        binding.barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.barChart.data = BarData(barDataSet)
        binding.barChart.animateXY(100, 500)
        binding.barChart.setDrawGridBackground(false)
        binding.barChart.isDragEnabled = true
        binding.barChart.setVisibleXRangeMaximum(5f)
        binding.barChart.xAxis.axisMinimum = 0f
        binding.barChart.axisLeft.axisMinimum = 0f

        val dateArray = AxisDateFormatter(dateValue.toArray(arrayOfNulls<String>(dateValue.size)))

        binding.barChart.data = BarData(barDataSet)
        val xAxis = binding.barChart.xAxis
        xAxis.valueFormatter = dateArray
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true

        binding.barChart.invalidate()

    }

    private fun setUpView() {
        supportActionBar?.title = "Grafik Penjualan"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}