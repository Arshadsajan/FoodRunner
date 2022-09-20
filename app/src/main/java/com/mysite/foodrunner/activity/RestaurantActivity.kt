package com.mysite.foodrunner.activity

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
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
import com.mysite.foodrunner.adapter.RestaurantAdapter
import com.mysite.foodrunner.adapter.RestaurantItemClicked
import com.mysite.foodrunner.databse.RecipeDatabase
import com.mysite.foodrunner.databse.RecipeEntity
import com.mysite.foodrunner.model.RecipeItems
import kotlinx.android.synthetic.main.activity_restaurant.*
import kotlinx.android.synthetic.main.restaurant_menu_row.view.*

class RestaurantActivity : AppCompatActivity(), RestaurantItemClicked {
    private var restId: String? = "100"
    var restaurantName: String? = "Restaurant"
    val recipeList = arrayListOf<RecipeItems>()
    lateinit var restaurantRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant)

        if (intent != null) {
            restId = intent.getStringExtra("id12")
            restaurantName = intent.getStringExtra("restName")
        }
        setSupportActionBar(toolbar)
        supportActionBar.apply {
            this?.setDisplayHomeAsUpEnabled(true)
            this?.setHomeButtonEnabled(true)
        }
        cartBtn.visibility = View.GONE
        cartBtn.setOnClickListener {
            val intent2 = Intent(this, CartActivity::class.java)
            intent2.putExtra("restName", restaurantName)
            intent2.putExtra("restId", restId)
            startActivity(intent2)
        }
        val queue = Volley.newRequestQueue(this)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/${restId}"
        val jsonObjectRequest =
            object : JsonObjectRequest(Method.GET, url, null, Response.Listener {
                try {
                    val dataObject = it.getJSONObject("data")
                    val success = dataObject.getBoolean("success")
                    if (success) {
                        val dataArray = dataObject.getJSONArray("data")
                        for (i in 0 until dataArray.length()) {
                            val foodObject = dataArray.getJSONObject(i)
                            val recipeItem = RecipeItems(
                                foodObject.getString("id"),
                                foodObject.getString("name"),
                                foodObject.getString("cost_for_one")
                            )
                            recipeList.add(recipeItem)
                        }
                        supportActionBar?.title = restaurantName
                        restaurantRecyclerView = findViewById(R.id.restaurantRecycler)
                        restaurantRecyclerView.adapter = RestaurantAdapter(this, recipeList, this)
                        restaurantRecyclerView.layoutManager = LinearLayoutManager(this)

                        restaurantRecyclerView.addItemDecoration(
                            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
                        )

                    } else {
                        Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Error $e", Toast.LENGTH_SHORT).show()

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

    override fun onAddBtnClicked(item: RecipeItems, view: View) {
        val recipeEntity = RecipeEntity(
            item.recipeId.toInt(),
            item.recipeName,
            item.recipePrice
        )
        if (DbSyncRecipe(this, recipeEntity, 3).execute().get()) {
            DbSyncRecipe(this, recipeEntity, 2).execute()
            view.menuButton.setBackgroundColor(getColor(R.color.colorPrimary))
            Toast.makeText(this, "Removed From Cart", Toast.LENGTH_SHORT).show()
            view.menuButton.text = "Add"
            if (!DbSyncRecipe(this, RecipeEntity(0, "", ""), 5).execute().get())
                cartBtn.visibility = View.GONE

        } else {
            DbSyncRecipe(this, recipeEntity, 1).execute()
            view.menuButton.setBackgroundColor(getColor(R.color.colorRemove))
            Toast.makeText(this, "Added to Cart", Toast.LENGTH_SHORT).show()
            view.menuButton.text = "Remove"
            cartBtn.visibility = View.VISIBLE

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }

    override fun onBackPressed() {
        DbSyncRecipe(this, RecipeEntity(0, "", ""), 4).execute()
        super.onBackPressed()
    }

    class DbSyncRecipe(
        val context: Context,
        private val recipeEntity: RecipeEntity,
        private val mode: Int
    ) : AsyncTask<Void, Void, Boolean>() {

        private val db =
            Room.databaseBuilder(context, RecipeDatabase::class.java, "db-recipe").build()

        override fun doInBackground(vararg params: Void?): Boolean {
            when (mode) {
                1 -> {
                    db.recipeDao().insertRecipe(recipeEntity)
                    db.close()
                    return true
                }
                2 -> {
                    db.recipeDao().deleteRecipe(recipeEntity)
                    db.close()
                    return true
                }
                3 -> {
                    val recipe: RecipeEntity = db.recipeDao().getRecipeById(recipeEntity.recipe_id)
                    db.close()
                    return recipe != null
                }
                4 -> {
                    db.recipeDao().removeAll()
                    db.close()
                    return true
                }
                5 -> {
                    val n: Int? = db.recipeDao().totalRows()
                    return n != 0
                }
            }
            return false
        }
    }

}