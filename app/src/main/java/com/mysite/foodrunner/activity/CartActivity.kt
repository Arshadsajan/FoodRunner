package com.mysite.foodrunner.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.mysite.foodrunner.R
import com.mysite.foodrunner.adapter.CartAdapter
import com.mysite.foodrunner.databse.RecipeDatabase
import com.mysite.foodrunner.databse.RecipeEntity
import kotlinx.android.synthetic.main.activity_cart.*
import org.json.JSONArray
import org.json.JSONObject

class CartActivity : AppCompatActivity() {
    private lateinit var sh: SharedPreferences

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        var restName: String? = ""
        var restId: String? = ""
        sh = getSharedPreferences(getString(R.string.myFile), MODE_PRIVATE)
        val userId = sh.getString("user_id", "")
        setSupportActionBar(cartToolbar)
        supportActionBar?.apply {
            this.title = "Your Cart"
            this.setHomeButtonEnabled(true)
            this.setDisplayHomeAsUpEnabled(true)
        }
        if (intent != null) {
            restName = intent.getStringExtra("restName")
            restId = intent.getStringExtra("restId")
        }
        cartRestName.text = restName
        val recipeList = FetchAllRecipe(this).execute().get()
        var total = 0
        val food = JSONArray()
        val queue = Volley.newRequestQueue(this)
        val url = "http://13.235.250.119/v2/place_order/fetch_result/"
        for (i in recipeList.indices) {
            val recipe = recipeList[i]
            val jsonObject = JSONObject()
            total += recipe.recipePrice.toInt()
            jsonObject.put("food_item_id", recipe.recipe_id)
            food.put(i, jsonObject)
        }
        val params = JSONObject()
        params.put("user_id", userId)
        params.put("restaurant_id", restId)
        params.put("total_cost", total)
        params.put("food", food)    //food array in params JSONObject()
        orderBtn.text = "Place Order(Total Rs.${total})"

//        Log.d("food", food.toString())
//        Log.d("params", params.toString())
        val cartRecycler: RecyclerView = findViewById(R.id.cartRecycleView)
        cartRecycler.adapter = CartAdapter(recipeList)
        cartRecycler.layoutManager = LinearLayoutManager(this)
        cartRecycler.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
        orderBtn.setOnClickListener {
            val jsonObjectRequest =
                object : JsonObjectRequest(Method.POST, url, params, Response.Listener {
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    if (success) {
                        Toast.makeText(this, "Order is Placed", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, OrderSplashActivity::class.java))
                    } else
                        Toast.makeText(this, "Order isn't Placed", Toast.LENGTH_SHORT).show()
                }, Response.ErrorListener {
                    Toast.makeText(this, "error occurred", Toast.LENGTH_SHORT).show()

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
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }

    class FetchAllRecipe(context: Context) : AsyncTask<Void, Void, List<RecipeEntity>>() {
        private val db =
            Room.databaseBuilder(context, RecipeDatabase::class.java, "db-recipe").build()

        override fun doInBackground(vararg params: Void?): List<RecipeEntity> {
            val recipeList = db.recipeDao().getAllRecipe()
            db.close()
            return recipeList
        }
    }
}