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
import kotlinx.android.synthetic.main.activity_reset_password.*
import org.json.JSONObject

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var sh: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        sh = getSharedPreferences("My File", Context.MODE_PRIVATE)

        btnSubmit.setOnClickListener {
            var password = etNewPwd.text.toString()
            var confPassword = etConfPwd.text.toString()
            val otp = etOtp.text.toString()

            if (password.length >= 4 && password == confPassword && otp != "") {
                val intent = Intent(this, LoginActivity::class.java)
                val queue = Volley.newRequestQueue(this)
                val url = "http://13.235.250.119/v2/reset_password/fetch_result/"
                val params = JSONObject()
                params.put("mobile_number", sh.getString("mobile_number", ""))
                params.put("password", password)
                params.put("otp", otp)

                val jsonObjectRequest =
                    object : JsonObjectRequest(Method.POST, url, params, Response.Listener {
                        val dataObject = it.getJSONObject("data")
                        val success = dataObject.getBoolean("success")
                        if (success) {
                            Toast.makeText(
                                this,
                                dataObject.getString("successMessage"),
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(intent)
                        } else
                            Toast.makeText(this, dataObject.getString("errorMessage"), Toast.LENGTH_SHORT).show()

                    }, Response.ErrorListener {
                        Toast.makeText(this, "Some error has occurred", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()

        }
    }
}