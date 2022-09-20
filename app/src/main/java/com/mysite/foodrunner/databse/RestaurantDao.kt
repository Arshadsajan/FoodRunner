package com.mysite.foodrunner.databse

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RestaurantDao {
    @Insert
    fun insertRestaurant(restaurantEntity: RestaurantEntity)

    @Delete
    fun deleteRestaurant(restaurantEntity: RestaurantEntity)

    @Query("select * from restaurants")
    fun getRestaurants(): List<RestaurantEntity>

    @Query("select * from restaurants where restaurant_id= :id")
    fun getRestaurantById(id: Int?): RestaurantEntity
}

@Dao
interface RecipeDao {
    @Insert
    fun insertRecipe(recipeEntity: RecipeEntity)

    @Delete
    fun deleteRecipe(recipeEntity: RecipeEntity)

    @Query("select * from restaurantRecipe")
    fun getAllRecipe(): List<RecipeEntity>

    @Query("select * from restaurantRecipe where recipe_id= :id")
    fun getRecipeById(id: Int?): RecipeEntity

    @Query("delete from restaurantRecipe")
    fun removeAll()

    @Query("select count(*) from restaurantRecipe")
    fun totalRows() : Int?
}