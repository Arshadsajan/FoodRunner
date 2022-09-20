package com.mysite.foodrunner.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.mysite.foodrunner.R
import com.mysite.foodrunner.adapter.HistoryAdapter
import com.mysite.foodrunner.model.OrderHistory
import kotlinx.android.synthetic.main.fragment_history.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HistoryFragment : Fragment() {
    lateinit var sh: SharedPreferences
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_history, container, false)
        sh = activity?.getSharedPreferences(getString(R.string.myFile), Context.MODE_PRIVATE)!!
        val userId = sh.getString("user_id", "")
        val orderedItems = arrayListOf<OrderHistory>()
        val queue = Volley.newRequestQueue(context)
        val url = "http://13.235.250.119/v2/orders/fetch_result/${userId}"
        val jsonObjectRequest =
            object : JsonObjectRequest(Method.GET, url, null, Response.Listener {
                val dataObject = it.getJSONObject("data")
                val success = dataObject.getBoolean("success")
                if (success) {
                    val dataArray = dataObject.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        val data = dataArray.getJSONObject(i)
                        val foodItems = data.getJSONArray("food_items")
                        val foodArray = arrayListOf<String>()
                        for (j in 0 until foodItems.length()) {
                            val food = foodItems.getJSONObject(j)
                            foodArray.add(food.getString("name"))
                        }
                        val item = OrderHistory(
                            data.getString("restaurant_name"),
                            data.getString("total_cost"),
                            data.getString("order_placed_at"),
                            foodArray
                        )
                        orderedItems.add(item)
                    }
                    historyRecycler.adapter = HistoryAdapter(activity!!, orderedItems)
                    historyRecycler.layoutManager = LinearLayoutManager(context)
                } else
                    Toast.makeText(activity, "Some Error occurred", Toast.LENGTH_SHORT).show()
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
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}