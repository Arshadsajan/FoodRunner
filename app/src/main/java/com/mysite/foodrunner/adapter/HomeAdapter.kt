package com.mysite.foodrunner.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mysite.foodrunner.R
import com.mysite.foodrunner.databse.RestaurantEntity
import com.mysite.foodrunner.fragments.HomeFragment
import com.mysite.foodrunner.model.Restaurant
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home_fragment_row.view.*

interface HomeInterface {
    fun onItemClicked(item: Restaurant)
    fun onFavButtonClicked(item: Restaurant, view: View)
}
class HomeAdapter(
    private val mContext: Context,
    private val restaurantList: ArrayList<Restaurant>,
    private val listener: HomeInterface
) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.home_fragment_row, parent, false)
        val viewHolder = HomeViewHolder(view)
        view.setOnClickListener {
            listener.onItemClicked(restaurantList[viewHolder.adapterPosition])
        }
        view.restaurantFavBtn.setOnClickListener {
            listener.onFavButtonClicked(restaurantList[viewHolder.adapterPosition], view)
        }
        return viewHolder
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val restaurant = restaurantList[position]
        holder.name.text = restaurant.restaurantName
        holder.price.text = "\u20B9${restaurant.restaurantCost} /person"
        holder.rating.text = restaurant.restaurantRating
        Picasso.get().load(restaurant.restaurantImageUrl).error(R.drawable.default_food_image)
            .into(holder.image)
        val restaurantEntity = RestaurantEntity(
            restaurant.restaurantId.toInt(),
            restaurant.restaurantName,
            restaurant.restaurantRating,
            restaurant.restaurantCost,
            restaurant.restaurantImageUrl
        )
        if (HomeFragment.DBSyncTask(mContext, restaurantEntity, 3).execute().get()) {
            holder.itemView.restaurantFavBtn.setImageResource(R.drawable.ic_favorite_fill)
        } else
            holder.itemView.restaurantFavBtn.setImageResource(R.drawable.ic_favorite_border_24)
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.restaurantName)
        val price: TextView = view.findViewById(R.id.restaurantPrice)
        val rating: TextView = view.findViewById(R.id.restaurantRating)
        val image: ImageView = view.findViewById(R.id.restaurantImage)
    }
}