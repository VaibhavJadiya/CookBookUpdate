package com.printoverit.cookbookupdate.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.*
import com.printoverit.cookbookupdate.models.FoodRecipeJson
import com.printoverit.cookbookupdate.repository.FoodRepository
import com.printoverit.cookbookupdate.room.FavouriteEntity
import com.printoverit.cookbookupdate.room.RecipiesEntity
import com.printoverit.cookbookupdate.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class FoodViewModel @Inject constructor(
    private val  repository: FoodRepository,
    application: Application) : AndroidViewModel(application)
{
    /*Room Database */
    val readRecipes : LiveData<List<RecipiesEntity>> = repository.local.readRecipes().asLiveData()
    val readFavouriteRecipes:LiveData<List<FavouriteEntity>> = repository.local.readFavouriteData().asLiveData()

    private fun insertRecipe(recipiesEntity: RecipiesEntity)=viewModelScope.launch(Dispatchers.IO) {
        repository.local.insertRecipe(recipiesEntity)
    }
     fun insertFavouriteRecipe(favouriteEntity: FavouriteEntity)=viewModelScope.launch(Dispatchers.IO){
        repository.local.insertFavouriteData(favouriteEntity)
    }
     fun deleteFavouriteRecipe(favouriteEntity: FavouriteEntity)=viewModelScope.launch(Dispatchers.IO){
        repository.local.deleteFavouriteData(favouriteEntity)
    }
     fun deleteAllFavouriteRecipe()=viewModelScope.launch(Dispatchers.IO) {
        repository.local.deleteAllFavouriteData()
    }

    /*Retrofit*/
    var recipeResponse:MutableLiveData<NetworkResult<FoodRecipeJson>> = MutableLiveData()
    var searchedRecipeResponse:MutableLiveData<NetworkResult<FoodRecipeJson>> = MutableLiveData()
    fun getRecipies(quries:Map<String, String>)=viewModelScope.launch {
            getRecipiesSafeCall(quries)
        }
    fun getSearchedRecipes(searchedQueries:Map<String, String>)=viewModelScope.launch {
        getSearchedRecipesSafeCall(searchedQueries)
        }

    private suspend fun getRecipiesSafeCall(quries: Map<String, String>) {
        recipeResponse.value=NetworkResult.Loading()
        if (hasInternConnection()){
            try {
                val response =repository.remote.getRecipies(quries)
                recipeResponse.value=handleFoodRecipeResponse(response)

                //Caching Data Immideatly in Room Database
                val foodRecipe=recipeResponse.value!!.data
                Log.d("TAG", "insertRecipe: ")
                if (foodRecipe != null) {
                    Log.d("TAG", "insertRecipe: ")
                        offlineCache(foodRecipe)
                    }

            }catch (e: Exception){
                recipeResponse.value= NetworkResult.Error("Recipies Not Found")
            }
        }else{
            recipeResponse.value=NetworkResult.Error("No Internet Connections :)")
        }
    }
    private suspend fun getSearchedRecipesSafeCall(searchedQueries:Map<String,String>){
        searchedRecipeResponse.value=NetworkResult.Loading()
        if (hasInternConnection()){
            try {
                val searchResponse=repository.remote.getSearchRecipes(searchedQueries)
                searchedRecipeResponse.value=handleFoodRecipeResponse(searchResponse)
                //No Caching in Searched Response As We don't Need that response Again
            }catch (e:Exception){
                searchedRecipeResponse.value=NetworkResult.Error("Recipes Not Found")
            }
        }else{
            searchedRecipeResponse.value=NetworkResult.Error("No Internet Connections :)")
        }
    }
    private fun offlineCache(foodRecipe: FoodRecipeJson) {
        Log.d("TAG", "insertRecipe: ")
        val recipiesEntity=RecipiesEntity(foodRecipe)
        insertRecipe(recipiesEntity)
    }

    private fun handleFoodRecipeResponse(response: Response<FoodRecipeJson>): NetworkResult<FoodRecipeJson>? {
        when{
            response.message().toString().contains("timeout")->{
                return NetworkResult.Error("Timeout")
            }
            response.code()==402->{
                return NetworkResult.Error("Api Key Limited.")
            }
            response.body()!!.results.isNullOrEmpty()->{
                return  NetworkResult.Error("Recipes Not Found")
            }
            response.isSuccessful->{
                val foodRecipies= response.body()
                return  NetworkResult.Success(foodRecipies!!)
            }
            else->{
                return  NetworkResult.Error(response.message().toString())
            }
        }
    }

    private  fun hasInternConnection():Boolean {
            val connectivityManager=getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
           val activeNetwork=connectivityManager.activeNetwork ?: return false
           val capabilties=connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when{
                capabilties.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilties.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilties.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
       }

 }