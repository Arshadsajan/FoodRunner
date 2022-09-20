package com.mysite.foodrunner.databse

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RestaurantEntity::class], version = 1)
abstract class RestaurantDatabase : RoomDatabase() {
    abstract fun restaurantDao(): RestaurantDao
}

@Database(entities = [RecipeEntity::class], version = 1)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}