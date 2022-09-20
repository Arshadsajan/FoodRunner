package com.mysite.foodrunner.fragments

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.mysite.foodrunner.R
import com.mysite.foodrunner.adapter.FavouriteAdapter
import com.mysite.foodrunner.databse.RestaurantDatabase
import com.mysite.foodrunner.databse.RestaurantEntity

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FavouritesFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    lateinit var favouriteRecyclerView: RecyclerView
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
        val view = inflater.inflate(R.layout.fragment_favourites, container, false)

        val allRestaurants = FetchRestaurants(activity as Context).execute().get()
        favouriteRecyclerView = view.findViewById(R.id.favouriteRecycler)
        favouriteRecyclerView.adapter = FavouriteAdapter(allRestaurants)
        favouriteRecyclerView.layoutManager = LinearLayoutManager(activity)

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavouritesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    class FetchRestaurants(val context: Context) : AsyncTask<Void, Void, List<RestaurantEntity>>() {
        private val db =
            Room.databaseBuilder(context, RestaurantDatabase::class.java, "db-restaurant").build()

        override fun doInBackground(vararg params: Void?): List<RestaurantEntity> {
            val allRestaurants: List<RestaurantEntity> = db.restaurantDao().getRestaurants()
            db.close()
            return allRestaurants
        }
    }
}