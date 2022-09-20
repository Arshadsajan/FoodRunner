package com.mysite.foodrunner.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mysite.foodrunner.R
import com.mysite.foodrunner.databse.RestaurantEntity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home_fragment_row.view.*

class FavouriteAdapter(private val favRestaurantList: List<RestaurantEntity>) :
    RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.home_fragment_row, parent, false)
        return FavouriteViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val restaurant = favRestaurantList[position]
        holder.name.text = restaurant.restaurantName
        holder.price.text = "\u20B9${restaurant.restaurantCost} /person"
        holder.rating.text = restaurant.restaurantRating
        Picasso.get().load(restaurant.restaurantImageUrl).error(R.drawable.default_food_image)
            .into(holder.image)
        holder.itemView.restaurantFavBtn.setImageResource(R.drawable.ic_favorite_fill)

    }

    override fun getItemCount(): Int {
        return favRestaurantList.size
    }

    class FavouriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.restaurantName)
        val price: TextView = view.findViewById(R.id.restaurantPrice)
        val rating: TextView = view.findViewById(R.id.restaurantRating)
        val image: ImageView = view.findViewById(R.id.restaurantImage)
    }
}
