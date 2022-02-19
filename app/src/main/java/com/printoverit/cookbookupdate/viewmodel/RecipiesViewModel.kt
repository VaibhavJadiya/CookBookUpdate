package com.printoverit.cookbookupdate.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.printoverit.cookbookupdate.repository.DataStoreRepository
import com.printoverit.cookbookupdate.utils.Constaints.Companion.API_KEY
import com.printoverit.cookbookupdate.utils.Constaints.Companion.DEFAULT_DIET
import com.printoverit.cookbookupdate.utils.Constaints.Companion.DEFAULT_MAIN_COURSE
import com.printoverit.cookbookupdate.utils.Constaints.Companion.DEFAULT_RECIPE_NUMBER
import com.printoverit.cookbookupdate.utils.Constaints.Companion.QUERY_ADD_RECIPE_INFO
import com.printoverit.cookbookupdate.utils.Constaints.Companion.QUERY_API_KEY
import com.printoverit.cookbookupdate.utils.Constaints.Companion.QUERY_DIET
import com.printoverit.cookbookupdate.utils.Constaints.Companion.QUERY_FILL_INGREDIENTS
import com.printoverit.cookbookupdate.utils.Constaints.Companion.QUERY_NUMBER
import com.printoverit.cookbookupdate.utils.Constaints.Companion.QUERY_SEARCH
import com.printoverit.cookbookupdate.utils.Constaints.Companion.QUERY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipiesViewModel @Inject constructor (
    private val dataStoreRepository: DataStoreRepository,
    application: Application) : AndroidViewModel(application) {

    private var mealType= DEFAULT_MAIN_COURSE
    private var dietType= DEFAULT_DIET
    var networkStatus= false
    var backOnline = false

    val readMealandDietType = dataStoreRepository.readSelectedMealandDietType
    val readBackOnline= dataStoreRepository.readBackOnline.asLiveData()
    fun saveMealandDietType(mealType:String,mealtypeId:Int,dietType:String,dietTypeId:Int)=viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveMealAndDietType(mealType,mealtypeId,dietType,dietTypeId)
    }
    fun saveBackOnline(backOnline:Boolean)=viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveBackOnline(backOnline)
    }
    fun applyQuries() : HashMap<String,String>{
        val queries : HashMap<String,String> = HashMap()

        viewModelScope.launch {
            readMealandDietType.collect { value ->
                mealType=value.selectedMealType
                dietType=value.selectedDietType
            }
        }

        queries[QUERY_NUMBER] = DEFAULT_RECIPE_NUMBER
        queries[QUERY_API_KEY]= API_KEY
        queries[QUERY_TYPE]= mealType
        queries[QUERY_DIET]= dietType
        queries[QUERY_ADD_RECIPE_INFO]= "true"
        queries[QUERY_FILL_INGREDIENTS]= "true"
        return queries
    }
    fun searchApplyQueries(searched:String): HashMap<String,String>{
        val searchedQueries:HashMap<String,String> = HashMap()
        searchedQueries[QUERY_SEARCH]=searched
        searchedQueries[QUERY_NUMBER] = DEFAULT_RECIPE_NUMBER
        searchedQueries[QUERY_API_KEY]= API_KEY
        searchedQueries[QUERY_ADD_RECIPE_INFO]= "true"
        searchedQueries[QUERY_FILL_INGREDIENTS]= "true"
        return searchedQueries
    }
    fun showNetworkStatus(){
        if(!networkStatus){
            Toast.makeText(getApplication(),"No Internet Connection :(",Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        }
        else{
            Toast.makeText(getApplication(),"Internet Access :)",Toast.LENGTH_SHORT).show()
            saveBackOnline(false)
        }
    }
}