package com.example.esemkavote

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.concurrent.thread
import org.json.JSONObject
import kotlin.math.log

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val editEmail = findViewById<EditText>(R.id.editEmail)
        val editPassword = findViewById<EditText>(R.id.editPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val textStatus = findViewById<TextView>(R.id.textStatus)

        buttonLogin.setOnClickListener {
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()

            thread {
                try {
                    val response = Api.post("http://10.0.2.2:5001/api/auth", mapOf(
                        "email" to editEmail.text.toString(),
                        "password" to editPassword.text.toString()
                    ))

                    val json = JSONObject(response)

                    runOnUiThread {
                        if (json.has("id")) {
                            textStatus.text = "Login berhasil! Selamat datang, ${json.getString("name")}"

                            // Simpan data user ke intent, SharedPreferences, atau langsung ke MainActivity
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("user_id", json.getInt("id"))
                            intent.putExtra("user_name", json.getString("name"))
                            startActivity(intent)
                            finish()
                        } else {
                            textStatus.text = "Login gagal! Data tidak ditemukan."
                        }
                    }

                } catch (e: Exception) {
                    runOnUiThread {
                        textStatus.text = "Terjadi kesalahan: ${e.message}"
                        Log.e("API_ERROR", "Exception saat login", e)
                    }
                }
            }
        }
    }
}