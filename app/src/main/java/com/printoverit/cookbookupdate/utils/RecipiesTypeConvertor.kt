package com.printoverit.cookbookupdate.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.printoverit.cookbookupdate.models.FoodRecipeJson
import com.printoverit.cookbookupdate.models.Result

class RecipiesTypeConvertor {
    var gson= Gson()
    //For Inserting Data in Room Database [Because we are storing the Entire Model CLass in the Data]
    @TypeConverter
    fun foodRecipeToString(foodRecipeJson: FoodRecipeJson): String{
        return  gson.toJson(foodRecipeJson)
    }
    //For Fetching Data from Room Database [Because we are fetching the whole model class and not any specific data entry]
    @TypeConverter
    fun stringToFoodRecipe(data :String ): FoodRecipeJson
    {
        var listType =object:TypeToken<FoodRecipeJson>(){}.type
        return gson.fromJson(data,listType)
    }

    @TypeConverter
    fun stringToResult(data:String):Result{
        val listTYpe=object:TypeToken<Result>(){}.type
        return gson.fromJson(data,listTYpe)
    }
    @TypeConverter
    fun ResultToString(result: Result):String{
        return  gson.toJson(result)
    }

}