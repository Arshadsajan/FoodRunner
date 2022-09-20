package com.mysite.foodrunner.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.mysite.foodrunner.R
import com.mysite.foodrunner.activity.RestaurantActivity
import com.mysite.foodrunner.databse.RecipeEntity
import com.mysite.foodrunner.model.RecipeItems
import kotlinx.android.synthetic.main.restaurant_menu_row.view.*

interface RestaurantItemClicked {
    fun onAddBtnClicked(item: RecipeItems, view: View)
}

class RestaurantAdapter(
    private val mContext: Context,
    private val recipeList: ArrayList<RecipeItems>,
    private val listener: RestaurantItemClicked
) : RecyclerView.Adapter<RestaurantAdapter.MenuViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.restaurant_menu_row, parent, false)
        val viewHolder = MenuViewHolder(view)
        view.menuButton.setOnClickListener {
            listener.onAddBtnClicked(recipeList[viewHolder.adapterPosition], view)
        }
        return viewHolder
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val recipeItem = recipeList[position]
        holder.sn.text = "${position + 1}"
        holder.recipeName.text = recipeItem.recipeName
        holder.recipePrice.text = "\u20B9${recipeItem.recipePrice}"

        val recipeEntity = RecipeEntity(
            recipeItem.recipeId.toInt(),
            recipeItem.recipeName,
            recipeItem.recipePrice
        )
        if (RestaurantActivity.DbSyncRecipe(mContext, recipeEntity, 3).execute().get()) {
            holder.itemView.menuButton.setBackgroundColor(getColor(mContext, R.color.colorRemove))
            holder.itemView.menuButton.text = "Remove"
        } else {
            holder.itemView.menuButton.setBackgroundColor(getColor(mContext, R.color.colorPrimary))
            holder.itemView.menuButton.text = "Add"
        }
    }

    override fun getItemCount(): Int = recipeList.size

    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sn: TextView = view.findViewById(R.id.sn)
        val recipeName: TextView = view.findViewById(R.id.menuFood)
        val recipePrice: TextView = view.findViewById(R.id.menuPrice)
    }
}