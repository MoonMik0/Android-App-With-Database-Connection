package com.ozlemelmali.kotlintriesv7.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ozlemelmali.kotlintriesv7.R
import com.ozlemelmali.kotlintriesv7.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.ozlemelmali.kotlintriesv7.Activity.TableDataActivity

class MainActivity : AppCompatActivity() {
    private lateinit var layout: LinearLayout
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layout = findViewById(R.id.tableButtonLayout)
        progressBar = findViewById(R.id.progressBar)

        fetchTableNames()
    }

    private fun fetchTableNames() {
        progressBar.visibility = View.VISIBLE
        val call = RetrofitClient.instance.getTableNames()

        call.enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val tableNames = response.body() ?: emptyList()
                    createTableButtons(tableNames)
                } else {
                    Toast.makeText(this@MainActivity, "Tablo isimleri alınamadı!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@MainActivity, "Bağlantı hatası!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun createTableButtons(tableNames: List<String>) {
        layout.removeAllViews()
        for (table in tableNames) {
            val button = Button(this)
            button.text = table
            button.setOnClickListener {
                val intent = Intent(this, TableDataActivity::class.java)
                intent.putExtra("table_name", table)
                startActivity(intent)
            }
            layout.addView(button)
        }
    }
}
