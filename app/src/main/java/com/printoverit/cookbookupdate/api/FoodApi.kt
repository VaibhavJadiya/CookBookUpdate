package com.printoverit.cookbookupdate.api

import com.printoverit.cookbookupdate.models.FoodRecipeJson
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface FoodApi {
    @GET("/recipes/complexSearch")
    //suspend = Kotlin Coroutines (Running in background)
   suspend fun getRecipes(
        @QueryMap quries: Map<String ,String>
    ):Response<FoodRecipeJson>

    @GET("/recipes/complexSearch")
   suspend fun getSearchRecipe(
        @QueryMap searchQueries: Map<String, String>
   ):Response<FoodRecipeJson>

}