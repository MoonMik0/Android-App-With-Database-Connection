package com.ozlemelmali.kotlintriesv7.Activity

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.ozlemelmali.kotlintriesv7.Adapter.TableDataAdapter
import com.ozlemelmali.kotlintriesv7.Adapter.TableHeaderAdapter
import com.ozlemelmali.kotlintriesv7.Data.ColumnResponse
import com.ozlemelmali.kotlintriesv7.R
import com.ozlemelmali.kotlintriesv7.Data.TableDataResponse
import com.ozlemelmali.kotlintriesv7.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TableDataActivity : AppCompatActivity() {

    private lateinit var recyclerViewHeader: RecyclerView
    private lateinit var recyclerViewData: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tableHeader: TableLayout
    private lateinit var tableName: String
    private lateinit var tableHeaderAdapter: TableHeaderAdapter
    private lateinit var tableDataAdapter: TableDataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table_data)

        recyclerViewHeader = findViewById(R.id.recyclerViewHeader)
        recyclerViewData = findViewById(R.id.recyclerViewData)
        progressBar = findViewById(R.id.progressBar)
        tableHeader = findViewById(R.id.tableHeader)
        // LayoutManager ve Adapter'ları ayarlıyoruz
        recyclerViewHeader.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewData.layoutManager = LinearLayoutManager(this)

        tableHeaderAdapter = TableHeaderAdapter(emptyList()) // Başlangıçta boş bir liste
        tableDataAdapter = TableDataAdapter(emptyList(), emptyList())

        recyclerViewHeader.adapter = tableHeaderAdapter
        recyclerViewData.adapter = tableDataAdapter

        // Intent ile gelen tablo ismini al
        tableName = intent.getStringExtra("table_name") ?: return

        // API'den sütun başlıklarını çek

        fetchTableColumns()

    }

    private fun fetchTableColumns() {
        progressBar.visibility = View.VISIBLE

        // Retrofit ile API çağrısı
        val call = RetrofitClient.instance.getTableColumns(tableName)

        call.enqueue(object : Callback<ColumnResponse> {
            override fun onResponse(call: Call<ColumnResponse>, response: Response<ColumnResponse>) {
                if (response.isSuccessful) {
                    val columns = response.body()?.columns ?: emptyList()

                    Log.d("API_RESPONSE", "Gelen sütun isimleri: $columns") // Log ekleyelim

                    if (columns.isNotEmpty()) {
                        fetchTableData(columns)
                        setupTableHeader(columns)
                    } else {
                        Log.e("TABLE_ERROR", "API sütunları boş döndü!")
                        Toast.makeText(this@TableDataActivity, "Sütun bilgileri boş!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@TableDataActivity, "Sütun bilgileri alınamadı!", Toast.LENGTH_SHORT).show()
                }
            }


            override fun onFailure(call: Call<ColumnResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@TableDataActivity, "Bağlantı hatası!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchTableData(columns: List<String>) {
        progressBar.visibility = View.VISIBLE

        // Verileri çekmek için API çağrısı
        val call = RetrofitClient.instance.getTableData(tableName)

        call.enqueue(object : Callback<TableDataResponse> {
            override fun onResponse(call: Call<TableDataResponse>, response: Response<TableDataResponse>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val data = response.body()?.data ?: emptyList()
                    tableHeaderAdapter.updateColumns(columns)
                    tableDataAdapter.updateData(columns, data)
                } else {
                    Toast.makeText(this@TableDataActivity, "Tablo verileri alınamadı!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TableDataResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@TableDataActivity, "Bağlantı hatası!", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun setupTableHeader(columns: List<String>) {
        tableHeader.removeAllViews() // Önce temizleyelim
        val tableRow = TableRow(this)
        tableRow.setBackgroundColor(Color.DKGRAY) // Arka plan rengi koyu gri yapıldı.

        for (column in columns) {
            val textView = TextView(this).apply {
                text = column.uppercase()  // Küçükse büyük harfe çevir.
                setPadding(16, 16, 16, 16)
                setTextColor(Color.WHITE)  // Beyaz yazı rengi.
                setTypeface(null, Typeface.BOLD)
                textSize = 16f  // Yazıyı büyüttüm.
                setBackgroundResource(R.drawable.table_header_bg) // Kenarlık ekledim.
                gravity = Gravity.CENTER
                layoutParams = TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f) // Esnek genişlik
            }
            tableRow.addView(textView)
        }

        tableHeader.addView(tableRow) // En üste ekleyelim
    }

}



