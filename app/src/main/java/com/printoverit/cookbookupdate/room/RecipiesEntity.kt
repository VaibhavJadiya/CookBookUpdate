package com.printoverit.cookbookupdate.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.printoverit.cookbookupdate.models.FoodRecipeJson
import com.printoverit.cookbookupdate.utils.Constaints.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class RecipiesEntity(var foodRecipeJson: FoodRecipeJson) {
    @PrimaryKey(autoGenerate = false)
    var Id: Int =0
}