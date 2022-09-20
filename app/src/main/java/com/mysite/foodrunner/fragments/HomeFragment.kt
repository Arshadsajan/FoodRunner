package com.mysite.foodrunner.fragments

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.mysite.foodrunner.R
import com.mysite.foodrunner.activity.RestaurantActivity
import com.mysite.foodrunner.adapter.HomeAdapter
import com.mysite.foodrunner.adapter.HomeInterface
import com.mysite.foodrunner.databse.RestaurantDatabase
import com.mysite.foodrunner.databse.RestaurantEntity
import com.mysite.foodrunner.model.Restaurant
import kotlinx.android.synthetic.main.home_fragment_row.view.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment(), HomeInterface {
    lateinit var homeRecyclerView: RecyclerView
    val lst = arrayListOf<Restaurant>()

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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"
        val jsonObjectRequest =
            object : JsonObjectRequest(Method.GET, url, null, Response.Listener {
                try {
                    val dataObject = it.getJSONObject("data")
                    val success = dataObject.getBoolean("success")
                    if (success) {
                        val dataArray = dataObject.getJSONArray("data")
                        for (i in 0 until dataArray.length()) {
                            val jsonObject = dataArray.getJSONObject(i)
                            val restaurant = Restaurant(
                                jsonObject.getString("id"),
                                jsonObject.getString("name"),
                                jsonObject.getString("rating"),
                                jsonObject.getString("cost_for_one"),
                                jsonObject.getString("image_url")
                            )
                            lst.add(restaurant)
                        }

                        homeRecyclerView = view.findViewById(R.id.homeRecycler)
                        homeRecyclerView.adapter = HomeAdapter(activity as Context,lst, this)
                        homeRecyclerView.layoutManager = LinearLayoutManager(activity)

                    } else {
                        Toast.makeText(
                            activity as Context,
                            "Some Error has occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } catch (e: Exception) {
                    Toast.makeText(
                        activity as Context,
                        "Some Error has occurred",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }, Response.ErrorListener {
                if (activity != null)
                    Toast.makeText(
                        activity as Context,
                        "Volley error has occurred",
                        Toast.LENGTH_SHORT
                    ).show()
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
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    class DBSyncTask    (
        val context: Context,
        private val restaurantEntity: RestaurantEntity,
        private val mode: Int
    ) : AsyncTask<Void, Void, Boolean>() {

        private val db =
            Room.databaseBuilder(context, RestaurantDatabase::class.java, "db-restaurant").build()

        override fun doInBackground(vararg params: Void?): Boolean {
            when (mode) {
                1 -> {
                    db.restaurantDao().insertRestaurant(restaurantEntity)
                    db.close()
                    return true
                }
                2 -> {
                    db.restaurantDao().deleteRestaurant(restaurantEntity)
                    db.close()
                    return true
                }
                3 -> {
                    val restaurant :RestaurantEntity = db.restaurantDao().getRestaurantById(restaurantEntity.restaurant_id)
                    db.close()
                    return restaurant != null
                }
            }
            return false
        }

    }

    override fun onItemClicked(item: Restaurant) {
        val intent = Intent(activity, RestaurantActivity::class.java)
        intent.putExtra("id12",item.restaurantId)
        intent.putExtra("restName",item.restaurantName)
        startActivity(intent)
    }

    override fun onFavButtonClicked(item: Restaurant, view: View) {
        val restaurantEntity = RestaurantEntity(
            item.restaurantId.toInt(),
            item.restaurantName,
            item.restaurantRating,
            item.restaurantCost,
            item.restaurantImageUrl
        )
        if (DBSyncTask(activity as Context, restaurantEntity, 3).execute().get()) {
            DBSyncTask(activity as Context, restaurantEntity, 2).execute()
            Toast.makeText(activity as Context, "Removed from favourite", Toast.LENGTH_SHORT).show()
            view.restaurantFavBtn.setImageResource(R.drawable.ic_favorite_border_24)
        } else {
            DBSyncTask(activity as Context, restaurantEntity, 1).execute()
            Toast.makeText(activity as Context, "Added to favourite", Toast.LENGTH_SHORT).show()
            view.restaurantFavBtn.setImageResource(R.drawable.ic_favorite_fill)
        }
    }
}
