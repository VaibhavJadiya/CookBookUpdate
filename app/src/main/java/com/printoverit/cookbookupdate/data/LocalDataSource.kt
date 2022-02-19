package com.printoverit.cookbookupdate.data

import android.util.Log
import com.printoverit.cookbookupdate.room.FavouriteEntity
import com.printoverit.cookbookupdate.room.RecipiesDao
import com.printoverit.cookbookupdate.room.RecipiesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipiesDao: RecipiesDao
) {
     fun readRecipes(): Flow<List<RecipiesEntity>>{
       return recipiesDao.readRecipe()
    }
    suspend fun insertRecipe(recipiesEntity: RecipiesEntity){
        Log.d("TAG", "insertRecipe: ")
        recipiesDao.insertData(recipiesEntity)
    }
    fun readFavouriteData(): Flow<List<FavouriteEntity>>{
        return  recipiesDao.readFavourite()
    }
    suspend fun insertFavouriteData(favouriteEntity: FavouriteEntity){
        recipiesDao.insertFavouriteData(favouriteEntity)
    }
    suspend fun deleteFavouriteData(favouriteEntity: FavouriteEntity){
        recipiesDao.deleteFavouriteData(favouriteEntity)
    }
    suspend fun deleteAllFavouriteData(){
        recipiesDao.deleteAllFavouriteRecipe()
    }
}