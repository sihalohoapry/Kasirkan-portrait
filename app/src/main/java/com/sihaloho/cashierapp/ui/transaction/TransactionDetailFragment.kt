package com.sihaloho.cashierapp.ui.transaction

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.sihaloho.cashierapp.R
import com.sihaloho.cashierapp.databinding.FragmentTransactionDetailBinding
import com.sihaloho.cashierapp.retrofit.ApiConfig
import com.sihaloho.cashierapp.retrofit.response.transaction.TransactionData
import com.sihaloho.cashierapp.retrofit.response.transactiondetail.TransactionDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransactionDetailFragment : Fragment() {

    private var _binding : FragmentTransactionDetailBinding ?= null
    val binding get() = _binding
    private val api by lazy { ApiConfig.getApi() }
    private val actionBar by lazy { (requireActivity() as TransactionActivity).supportActionBar }
    private lateinit var transactionDetailAdapter: AdapterTransactionDetail
    private var progressBar: Dialog? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentTransactionDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        argumentsTransaction()
        setUpListener()
        setUpView()
        isLoading()
    }

    private fun setUpView() {
        actionBar?.title = "Detail Transaction"
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun isLoading() {
        progressBar = Dialog(requireContext())
        val layout = layoutInflater.inflate(R.layout.dialog_loading,null)
        progressBar.let {
            it?.setContentView(layout)
            it?.setCancelable(false)
            it?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    private fun setUpListener() {
        transactionDetailAdapter = AdapterTransactionDetail()
        with(binding?.rvDetail){
            this?.adapter = transactionDetailAdapter
            this?.setHasFixedSize(true)
        }
    }
    private fun argumentsTransaction() {
        val data = arguments?.getSerializable("arg_transaction") as TransactionData
        binding?.tvName?.text = data.nama_pelanggan
        binding?.tvTable?.text = data.no_meja
        binding?.tvNote?.text = data.catatan
        detailTransaction(data.id_transaksi)
    }
    private fun detailTransaction(idTransaksi: String) {
        progressBar?.show()
        api.transaksiDetail(idTransaksi).enqueue(object : Callback<TransactionDetailResponse> {
            override fun onResponse(
                call: Call<TransactionDetailResponse>,
                response: Response<TransactionDetailResponse>
            ) {
                if (response.isSuccessful) {
                    progressBar?.dismiss()
                    transactionDetailAdapter.setData(response.body()?.data)
                }
            }

            override fun onFailure(call: Call<TransactionDetailResponse>, t: Throwable) {
                progressBar?.dismiss()
                Toast.makeText(requireContext(), "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        actionBar?.setDisplayHomeAsUpEnabled(false)
        actionBar?.title = "Laporan Transaksi"
    }


}