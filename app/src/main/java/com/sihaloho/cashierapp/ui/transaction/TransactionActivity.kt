package com.sihaloho.cashierapp.ui.transaction

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.RadioButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.sihaloho.cashierapp.R
import com.sihaloho.cashierapp.databinding.ActivityTransactionBinding
import com.sihaloho.cashierapp.preference.PrefManager
import com.sihaloho.cashierapp.retrofit.ApiConfig
import com.sihaloho.cashierapp.retrofit.response.ExportResponse
import com.sihaloho.cashierapp.retrofit.response.cashier.CashierResponse
import com.sihaloho.cashierapp.retrofit.response.transaction.TransactionResponse
import com.sihaloho.cashierapp.ui.chart.ChartActivity
import com.sihaloho.cashierapp.ui.login.LoginActivity
import com.sihaloho.cashierapp.utils.CalendarUtils
import com.sihaloho.cashierapp.utils.convertTanggal
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val transactionByDate = 0
private const val transactionByCashier = 1

class TransactionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransactionBinding
    private lateinit var adapterTransaction: AdapterTransaction
    private lateinit var adapterCashier: AdapterCashier
    private val api by lazy { ApiConfig.getApi() }
    private var progressBar: Dialog? = null
    private val prefManager by lazy { PrefManager(this) }
    private var currentTransaction: Int = 0
    private var dateStart: String = ""
    private var dateEnd: String = ""
    private var usernameCashier: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
        setUpListener()
        isLoading()

        adapterTransaction = AdapterTransaction()
        if (adapterTransaction.itemCount == 0) {
            binding.ivNoData.visibility = View.VISIBLE
            binding.tvNoData.visibility = View.VISIBLE
        }

        setUpRecyclerView()
        binding.etDateStart.setOnClickListener {
            setDateStart()
        }
        binding.etDateEnd.setOnClickListener {
            setDateEnd()
        }

    }

    private fun setDateEnd() {
        val calender = CalendarUtils
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { dataPicker, year, monthOfYear, dayOfMonth ->
                dateEnd = "$year-${(monthOfYear + 1)}-$dayOfMonth"
                binding.etDateEnd.setText(convertTanggal(dateEnd, "dd MMMM yyyy", "yyyy-M-d"))
                listTransaction(transactionByDate)
            },
            calender.year,
            calender.month,
            calender.day
        )
        datePickerDialog.show()
    }

    private fun setDateStart() {
        val calender = CalendarUtils
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { dataPicker, year, monthOfYear, dayOfMonth ->
                dateStart = "$year-${(monthOfYear + 1)}-$dayOfMonth"
                binding.etDateStart.setText(convertTanggal(dateStart, "dd MMMM yyyy", "yyyy-M-d"))
                listTransaction(transactionByDate)
            },
            calender.year,
            calender.month,
            calender.day
        )
        datePickerDialog.show()
    }

    private fun setUpView() {
        supportActionBar?.title = "Laporan Transaksi"
        transactionBy(transactionByDate)
    }

    private fun setUpListener() {
        binding.radioFilterBy.setOnCheckedChangeListener { radioGroup, checkedId ->
            val radioButton: RadioButton = radioGroup.findViewById(checkedId)
            when (radioButton.id) {
                R.id.radio_date -> {
                    transactionBy(transactionByDate)
                }
                R.id.radio_kasir -> {
                    transactionBy(transactionByCashier)
                }
            }
            binding.etNoTransaction.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    listTransaction(currentTransaction)
                    true
                } else {
                    false
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        listCashier()
    }


    private fun transactionBy(transactionBy: Int) {
        currentTransaction = transactionBy
        when (transactionBy) {
            transactionByDate -> {
                binding.radioDate.isChecked = true
                binding.etDateStart.visibility = View.VISIBLE
                binding.etDateEnd.visibility = View.VISIBLE
                binding.inputTanggalAkhir.visibility = View.VISIBLE
                binding.inputTanggalAwal.visibility = View.VISIBLE
                binding.rvCashier.visibility = View.GONE
                binding.labelListCashier.visibility = View.GONE
            }
            transactionByCashier -> {
                binding.radioKasir.isChecked = true
                binding.etDateStart.visibility = View.GONE
                binding.etDateEnd.visibility = View.GONE
                binding.rvCashier.visibility = View.VISIBLE
                binding.inputTanggalAkhir.visibility = View.GONE
                binding.inputTanggalAwal.visibility = View.GONE
                binding.labelListCashier.visibility = View.VISIBLE

            }
        }
    }

    private fun isLoading() {
        progressBar = Dialog(this)
        val layout = layoutInflater.inflate(R.layout.dialog_loading, null)

        progressBar?.setContentView(layout)
        progressBar?.setCancelable(false)
        progressBar?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun setUpRecyclerView() {

        with(binding.rvTransaction) {
            adapter = adapterTransaction
            setHasFixedSize(true)
        }
        adapterTransaction.onClick = {
            val bundle = Bundle()
            bundle.putSerializable("arg_transaction", it)
            val transactionDetailFragment = TransactionDetailFragment()
            transactionDetailFragment.arguments = bundle
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_transaction, transactionDetailFragment)
                .addToBackStack(null)
                .commit()
        }

        adapterCashier = AdapterCashier()
        with(binding.rvCashier) {
            layoutManager =
                LinearLayoutManager(this@TransactionActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = adapterCashier
            setHasFixedSize(true)
        }
        adapterCashier.onClick = {
            usernameCashier = it.username
            listTransaction(transactionByCashier)
        }
    }

    private fun listCashier() {
        api.kasir().enqueue(object : Callback<CashierResponse> {
            override fun onResponse(
                call: Call<CashierResponse>,
                response: Response<CashierResponse>
            ) {
                if (response.isSuccessful) {
                    adapterCashier.setData(response.body()?.data)

                }
            }

            override fun onFailure(call: Call<CashierResponse>, t: Throwable) {
                Toast.makeText(this@TransactionActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT)
                    .show()

            }

        })
    }

    private fun listTransaction(transactionBy: Int) {
        var callApi: Call<TransactionResponse>? = null
        when (transactionBy) {
            transactionByDate ->
                if (dateStart.isNotEmpty() && dateEnd.isNotEmpty()) {
                    callApi = api.transaksiDate(
                        dateStart,
                        dateEnd,
                        binding.etNoTransaction.text.toString()
                    )
                } else {
                    Toast.makeText(this, "Tengkapi tanggal pencarian", Toast.LENGTH_SHORT).show()
                }

            transactionByCashier -> {
                callApi =
                    api.transaksiKasir(usernameCashier, binding.etNoTransaction.text.toString())
            }

        }


        callApi?.let {
            progressBar?.show()
            it.enqueue(object : Callback<TransactionResponse> {
                override fun onResponse(
                    call: Call<TransactionResponse>,
                    response: Response<TransactionResponse>
                ) {
                    if (response.isSuccessful) {
                        progressBar?.dismiss()
                        adapterTransaction.setData(response.body()?.data)
                        if (adapterTransaction.itemCount != 0) {
                            binding.ivNoData.visibility = View.GONE
                            binding.tvNoData.visibility = View.GONE
                        }
                        if (adapterTransaction.itemCount == 0) {
                            binding.ivNoData.visibility = View.VISIBLE
                            binding.tvNoData.visibility = View.VISIBLE
                        }

                    }
                }

                override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
                    progressBar?.dismiss()
                    Toast.makeText(
                        this@TransactionActivity,
                        "Terjadi kesalahan",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            })
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_transaction, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_chart -> {
                startActivity(Intent(this, ChartActivity::class.java))
                true
            }
            R.id.action_export_pdf -> {
                if (dateStart.isNotEmpty() && dateEnd.isNotEmpty()) {
                    export("pdf")
                } else {
                    Toast.makeText(this, "Tengkapi tanggal pencarian", Toast.LENGTH_SHORT).show()
                }
                true
            }
            R.id.action_export_excel -> {
                transactionBy(transactionByDate)
                if (dateStart.isNotEmpty() && dateEnd.isNotEmpty()) {
                    export("excel")
                } else {
                    Toast.makeText(this, "Tengkapi tanggal pencarian", Toast.LENGTH_SHORT).show()
                }
                true
            }
            R.id.action_logout -> {
                prefManager.clear()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun export(exportType: String) {
        var calApi: Call<ExportResponse>? = null
        when (exportType) {
            "pdf" -> calApi = api.exportPdf(dateStart, dateEnd)
            "excel" -> calApi = api.exportExcel(dateStart, dateEnd)
        }
        calApi.let {
            progressBar?.show()
            it?.enqueue(object : Callback<ExportResponse> {
                override fun onResponse(
                    call: Call<ExportResponse>,
                    response: Response<ExportResponse>
                ) {
                    if (response.isSuccessful) {
                        progressBar?.dismiss()
                        exportResponse(response.body())

                    }
                }

                override fun onFailure(call: Call<ExportResponse>, t: Throwable) {
                    progressBar?.dismiss()
                    Toast.makeText(
                        this@TransactionActivity,
                        "Terjadi Kesalahan",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }
    }

    private fun exportResponse(data: ExportResponse?) {

        val openUrl = Intent(Intent.ACTION_VIEW)
        openUrl.data = Uri.parse(data?.data)
        startActivity(openUrl)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}