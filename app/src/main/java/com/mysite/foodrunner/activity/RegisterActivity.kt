package com.mysite.foodrunner.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.mysite.foodrunner.R
import kotlinx.android.synthetic.main.activity_login.etMobile
import kotlinx.android.synthetic.main.activity_login.etPassword
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    private lateinit var sh: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
        sh = getSharedPreferences("My File", Context.MODE_PRIVATE)
        val edit = sh.edit()

        btnReg.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val mobileNumber = etMobile.text.toString()
            val address = etAddress.text.toString()
            val password = etPassword.text.toString()
            val confPassword = etConfPassword.text.toString()

            if (password.length >= 4 && password == confPassword && mobileNumber.length == 10 && name.length >= 3) {

                val queue = Volley.newRequestQueue(this)
                val url = "http://13.235.250.119/v2/register/fetch_result/"
                val params = JSONObject()
                params.put("name", name)
                params.put("mobile_number", mobileNumber)
                params.put("password", password)
                params.put("address", address)
                params.put("email", email)

                val jsonObjectRequest = object : JsonObjectRequest(
                    Method.POST, url, params, Response.Listener {
                        try {
                            val resObject = it.getJSONObject("data")
                            val success = resObject.getBoolean("success")
                            Log.d("registerOr", "Data has passed")
                            if (success) {
                                val dataObject = resObject.getJSONObject("data")
                                edit.putBoolean("isLoggedIn", true).apply()
                                edit.putString("user_id", dataObject.getString("user_id")).apply()
                                edit.putString("name", name).apply()
                                edit.putString("email", email).apply()
                                edit.putString("mobile_number", mobileNumber).apply()
                                edit.putString("address", address).apply()
                                Toast.makeText(this, "Registered successfully!", Toast.LENGTH_SHORT)
                                    .show()
                                startActivity(intent)
                            } else
                                Toast.makeText(
                                    this,
                                    resObject.getString("errorMessage"),
                                    Toast.LENGTH_SHORT
                                ).show()

                        } catch (e: Exception) {
                            Toast.makeText(this, "Some Error has occurred.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }, Response.ErrorListener {
                        Toast.makeText(this, "Volley error has occurred!!", Toast.LENGTH_SHORT)
                            .show()
                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "9bf534118365f1"
                        return headers
                    }
                }

                queue.add(jsonObjectRequest)

            } else
                Toast.makeText(this, "Enter credentials Correctly", Toast.LENGTH_SHORT).show()
        }
    }
}