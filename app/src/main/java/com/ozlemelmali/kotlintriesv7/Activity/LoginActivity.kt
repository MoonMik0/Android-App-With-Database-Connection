package com.ozlemelmali.kotlintriesv7.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.ozlemelmali.kotlintriesv7.Activity.MainActivity
import com.ozlemelmali.kotlintriesv7.ApiService
import com.ozlemelmali.kotlintriesv7.Data.LoginResponse
import com.ozlemelmali.kotlintriesv7.Data.UserLogin
import com.ozlemelmali.kotlintriesv7.R
import com.ozlemelmali.kotlintriesv7.RetrofitClient
import org.json.JSONStringer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class LoginActivity : AppCompatActivity() {
    private val apiService = RetrofitClient.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameEditText = findViewById<EditText>(R.id.etUsername)
        val passwordEditText = findViewById<EditText>(R.id.etPassword)
        val loginButton = findViewById<Button>(R.id.btnLogin)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Kullanıcı adı ve şifre boş olamaz!", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(username, password)
            }
        }
    }

    private fun loginUser(username: String, password: String) {
        val request = UserLogin(username, password)
        val jsonBody = Gson().toJson(request)
        Log.d("JSON_DEBUG", "Gönderilen JSON: $jsonBody") //readable on logcat
        val call = apiService.login(request)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    Log.d("API_SUCCESS", "Login successful: ${response.body()}")
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("API_ERROR", "Login failed: ${response.errorBody()?.string()}")
                    Toast.makeText(this@LoginActivity, "Hatalı giriş!", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("API_ERROR", "Network error: ${t.message}")
                Toast.makeText(this@LoginActivity, "Bağlantı hatası!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}