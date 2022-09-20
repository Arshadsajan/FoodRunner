package com.mysite.foodrunner.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.mysite.foodrunner.R
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    lateinit var sh: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sh = getSharedPreferences(getString(R.string.myFile), Context.MODE_PRIVATE)
        val edit = sh.edit()
        val intentLogin = Intent(this@LoginActivity, MainActivity::class.java)

        btnLogin.setOnClickListener {
            val mobileNumber: String = etMobile.text.toString()
            val password: String = etPassword.text.toString()

            val queue = Volley.newRequestQueue(this)
            val url = "http://13.235.250.119/v2/login/fetch_result/"
            val params = JSONObject()
            params.put("mobile_number", mobileNumber)
            params.put("password", password)

            val jsonObjectRequest =
                object : JsonObjectRequest(Method.POST, url, params, Response.Listener {
                    try {
                        val resObject = it.getJSONObject("data")
                        val success = resObject.getBoolean("success")
                        if (success) {
                            val dataObject = resObject.getJSONObject("data")
                            edit.putBoolean("isLoggedIn", true).apply()
                            edit.putString("user_id", dataObject.getString("user_id")).apply()
                            edit.putString("name", dataObject.getString("name")).apply()
                            edit.putString("email", dataObject.getString("email")).apply()
                            edit.putString("mobile_number", dataObject.getString("mobile_number")).apply()
                            edit.putString("address", dataObject.getString("address")).apply()
                            startActivity(intentLogin)
                        } else
                            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {

                    }
                }, Response.ErrorListener {

                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "9bf534118365f1"
                        return headers
                    }
                }
            queue.add(jsonObjectRequest)

        }

        txtForgPassword.setOnClickListener {
            val intentPassword = Intent(this@LoginActivity, PasswordActivity::class.java)
            startActivity(intentPassword)
        }
        txtSignUp.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

    }

    override fun onBackPressed() {
        finishAffinity()
    }
}