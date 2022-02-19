package com.printoverit.cookbookupdate.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.printoverit.cookbookupdate.utils.RecipiesTypeConvertor

@Database( entities = [RecipiesEntity::class , FavouriteEntity::class], version = 1 , exportSchema = false)
@TypeConverters(RecipiesTypeConvertor::class)
abstract class RecipiesDatabase: RoomDatabase() {

    abstract fun recipiesDao(): RecipiesDao

}