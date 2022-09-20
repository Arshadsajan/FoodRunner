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
import kotlinx.android.synthetic.main.activity_password.*
import org.json.JSONObject

class PasswordActivity : AppCompatActivity() {
    lateinit var sh: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)
        sh = getSharedPreferences("My File", Context.MODE_PRIVATE)

        btnNext.setOnClickListener {
            var number = etMobile.text.toString()
            var email = etEmail.text.toString()
            if (number.length == 10 && email != "") {
                val intent = Intent(this, ResetPasswordActivity::class.java)
                val queue = Volley.newRequestQueue(this)
                val url = "http://13.235.250.119/v2/forgot_password/fetch_result/"
                val params = JSONObject()
                params.put("mobile_number", number)
                params.put("email", email)

                val jsonObjectRequest =
                    object : JsonObjectRequest(Method.POST, url, params, Response.Listener {
                        val dataObject = it.getJSONObject("data")
                        val success = dataObject.getBoolean("success")
                        if (success) {
                            Toast.makeText(
                                this,
                                "OTP has sent to registered EmailID",
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