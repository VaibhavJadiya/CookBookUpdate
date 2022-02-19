package com.printoverit.cookbookupdate.room

import androidx.room.*

import kotlinx.coroutines.flow.Flow

@Dao
interface RecipiesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(recipiesEntity: RecipiesEntity)

    @Query("SELECT * FROM recipe_table ORDER BY id ASC")
    fun readRecipe() : Flow<List<RecipiesEntity>>

    @Insert()
    suspend fun insertFavouriteData(favouriteEntity: FavouriteEntity)

    @Delete()
    suspend fun deleteFavouriteData(favouriteEntity: FavouriteEntity)

    @Query("DELETE FROM favourite_table")
    fun deleteAllFavouriteRecipe()

    @Query("SELECT * FROM favourite_table ORDER BY id ASC")
    fun readFavourite() : Flow<List<FavouriteEntity>>
}