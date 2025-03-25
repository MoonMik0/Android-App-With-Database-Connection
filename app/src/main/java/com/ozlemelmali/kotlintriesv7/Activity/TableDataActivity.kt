package com.ozlemelmali.kotlintriesv7.Activity

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ozlemelmali.kotlintriesv7.R
import com.ozlemelmali.kotlintriesv7.Data.TableDataResponse
import com.ozlemelmali.kotlintriesv7.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TableDataActivity : AppCompatActivity() {
    private lateinit var tableName: String
    private lateinit var textView: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table_data)

        textView = findViewById(R.id.tvTableData)
        progressBar = findViewById(R.id.progressBar)
        tableName = intent.getStringExtra("table_name") ?: ""

        fetchTableData()
    }

    private fun fetchTableData() {
        progressBar.visibility = View.VISIBLE
        val call = RetrofitClient.instance.getTableData(tableName)

        call.enqueue(object : Callback<TableDataResponse> {
            override fun onResponse(call: Call<TableDataResponse>, response: Response<TableDataResponse>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val data = response.body()?.data ?: emptyList()
                    textView.text = data.joinToString("\n") { it.toString() }
                } else {
                    textView.text = "Veri alınamadı!"
                }
            }

            override fun onFailure(call: Call<TableDataResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                textView.text = "Bağlantı hatası!"
            }
        })
    }
}
