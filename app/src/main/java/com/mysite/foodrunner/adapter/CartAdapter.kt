package com.mysite.foodrunner.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mysite.foodrunner.R
import com.mysite.foodrunner.databse.RecipeEntity

class CartAdapter(private val cartItems: List<RecipeEntity>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cart_item_row, parent, false)

        return CartViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.cartRecipeName.text = cartItem.recipeName
        holder.cartRecipePrice.text = "\u20B9${cartItem.recipePrice}"
    }

    override fun getItemCount(): Int = cartItems.size

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cartRecipeName: TextView = view.findViewById(R.id.cartRecipe)
        val cartRecipePrice: TextView = view.findViewById(R.id.cartRecipePrice)
    }
}