package com.sihaloho.cashierapp.ui.print

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mazenrashed.printooth.Printooth
import com.mazenrashed.printooth.data.printable.Printable
import com.mazenrashed.printooth.data.printable.TextPrintable
import com.mazenrashed.printooth.data.printer.DefaultPrinter
import com.mazenrashed.printooth.ui.ScanningActivity
import com.sihaloho.cashierapp.databinding.ActivityPrintBinding
import com.sihaloho.cashierapp.retrofit.response.cart.CartItem
import com.sihaloho.cashierapp.ui.home.MainActivity

class PrintActivity : AppCompatActivity() {


    private val noTable by lazy { intent.getStringExtra("intent_table") }
    private val name by lazy { intent.getStringExtra("intent_name") }
    private val total by lazy { intent.getStringExtra("intent_total") }
    private val carts: ArrayList<CartItem> by lazy { intent.getSerializableExtra("intent_cart") as ArrayList<CartItem> }

    private lateinit var binding: ActivityPrintBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrintBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        Printooth.init(this)
        initView()

    }

    private fun initView() {
        binding.btnCariPerangkat.setOnClickListener {
            startActivityForResult(Intent(this, ScanningActivity::class.java), ScanningActivity.SCANNING_FOR_PRINTER)
        }
        binding.btnPrint.setOnClickListener {
            print()
            Toast.makeText(this, "Berhasil Print", Toast.LENGTH_SHORT).show()
        }
        binding.btnTutup.setOnClickListener {
            finish()
        }
    }

    private fun print() {
        val printables = arrayListOf<Printable>(
                TextPrintable.Builder()
                        .setText("Warung Makan Ibu" )
                        .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
                        .setFontSize(DefaultPrinter.FONT_SIZE_LARGE)
                        .setNewLinesAfter(1)
                        .build(),
                TextPrintable.Builder()
                        .setText("Kedaton, Bandar Lampung")
                        .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
                        .setNewLinesAfter(1)
                        .build(),
                TextPrintable.Builder()
                        .setText("No. Meja $noTable - $name")
                        .setNewLinesAfter(1)
                        .setLineSpacing(DefaultPrinter.LINE_SPACING_60)
                        .build()
        )

        carts.forEach {
            printables.add(
                    TextPrintable.Builder()
                            .setText("${it.nama_produk} x ${it.jumlah} : ${it.harga}")
                            .setNewLinesAfter(1)
                            .build()
            )
        }

        printables.add(
                TextPrintable.Builder()
                        .setText( total!! )
                        .setNewLinesAfter(1)
                        .setEmphasizedMode(DefaultPrinter.EMPHASIZED_MODE_BOLD)
                        .build()
        )

        printables.add(
                TextPrintable.Builder()
                        .setText("Terima kasih")
                        .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
                        .setNewLinesAfter(1)
                        .setLineSpacing(DefaultPrinter.LINE_SPACING_60)
                        .build()
        )

        Printooth.printer().print(printables)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ScanningActivity.SCANNING_FOR_PRINTER && resultCode == Activity.RESULT_OK){
            Toast.makeText(this, "Printer sudah siap", Toast.LENGTH_SHORT).show()
        }

    }

}