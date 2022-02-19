package com.printoverit.cookbookupdate.models


import com.google.gson.annotations.SerializedName

data class FoodRecipeJson(
    @SerializedName("results")
    val results: List<Result>

    )