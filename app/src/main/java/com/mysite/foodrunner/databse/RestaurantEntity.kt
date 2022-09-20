package com.mysite.foodrunner.databse

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurants")
data class RestaurantEntity(
    @PrimaryKey val restaurant_id: Int?,
    @ColumnInfo(name = "restaurant_name") val restaurantName: String,
    @ColumnInfo(name = "restaurant_rating") val restaurantRating: String,
    @ColumnInfo(name = "restaurant_cost") val restaurantCost: String,
    @ColumnInfo(name = "restaurant_image_url") val restaurantImageUrl: String
)

@Entity(tableName = "restaurantRecipe")
data class RecipeEntity(
    @PrimaryKey val recipe_id: Int?,
    @ColumnInfo(name = "recipe_name") val recipeName: String,
    @ColumnInfo(name = "recipe_price") val recipePrice: String
)