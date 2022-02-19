package com.printoverit.cookbookupdate.data

import com.printoverit.cookbookupdate.api.FoodApi
import com.printoverit.cookbookupdate.models.FoodRecipeJson
import retrofit2.Response
import retrofit2.http.Query
import retrofit2.http.QueryMap
import java.time.temporal.TemporalQueries
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodApi: FoodApi
) {
    //Kotlin Coroutines - suspend
    suspend fun getRecipies(queries: Map<String , String>) :Response<FoodRecipeJson>{
        return foodApi.getRecipes(queries)
    }
    suspend fun getSearchRecipes(searchQueries: Map<String, String>):Response<FoodRecipeJson>{
        return foodApi.getSearchRecipe(searchQueries)
    }
}