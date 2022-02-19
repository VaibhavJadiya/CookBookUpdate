package com.printoverit.cookbookupdate.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.printoverit.cookbookupdate.models.Result
import com.printoverit.cookbookupdate.utils.Constaints.Companion.FAVOURITE_TABLE_NAME

@Entity(tableName = FAVOURITE_TABLE_NAME)
data class FavouriteEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val result:Result,
)