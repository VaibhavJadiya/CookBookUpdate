package com.printoverit.cookbookupdate.repository

import android.content.Context
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.printoverit.cookbookupdate.utils.Constaints.Companion.DEFAULT_DIET
import com.printoverit.cookbookupdate.utils.Constaints.Companion.DEFAULT_MAIN_COURSE
import com.printoverit.cookbookupdate.utils.Constaints.Companion.PREFERENCES_BACK_ONLINE
import com.printoverit.cookbookupdate.utils.Constaints.Companion.PREFERENCES_DIET_TYPE
import com.printoverit.cookbookupdate.utils.Constaints.Companion.PREFERENCES_DIET_TYPE_ID
import com.printoverit.cookbookupdate.utils.Constaints.Companion.PREFERENCES_MEAL_TYPE
import com.printoverit.cookbookupdate.utils.Constaints.Companion.PREFERENCES_MEAL_TYPE_ID
import com.printoverit.cookbookupdate.utils.Constaints.Companion.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context:Context) {

    private var appContext = context

    private object PreferenceKeys{
        val selectedMealType= stringPreferencesKey(PREFERENCES_MEAL_TYPE)
        val selectedMealTypeId= intPreferencesKey(PREFERENCES_MEAL_TYPE_ID)
        val selectedDietType= stringPreferencesKey(PREFERENCES_DIET_TYPE)
        val selectedDietTypeId= intPreferencesKey(PREFERENCES_DIET_TYPE_ID)
        val backOnline= booleanPreferencesKey(PREFERENCES_BACK_ONLINE)
    }
    private val Context.dataStore by preferencesDataStore(PREFERENCES_NAME)
    suspend fun saveMealAndDietType(mealType:String,mealtypeId:Int,dietType:String,dietTypeId:Int){
        appContext.dataStore.edit { preferences ->
            preferences[PreferenceKeys.selectedMealType]=mealType
            preferences[PreferenceKeys.selectedMealTypeId]=mealtypeId
            preferences[PreferenceKeys.selectedDietType]= dietType
            preferences[PreferenceKeys.selectedDietTypeId]=dietTypeId
        }
    }
    suspend fun saveBackOnline(backOnline:Boolean){
        appContext.dataStore.edit { preferences ->
            preferences[PreferenceKeys.backOnline]=backOnline
        }
    }
    val readSelectedMealandDietType: Flow<MealandDietType> = appContext.dataStore.data.catch { exception->
        if (exception is IOException){
            emit(emptyPreferences())
        }else{
            throw exception
        }
    }.map{ preferences->
        val selectedMealType= preferences[PreferenceKeys.selectedMealType]?: DEFAULT_MAIN_COURSE
        val selectedMealTypeID= preferences[PreferenceKeys.selectedMealTypeId]?: 0
        val selectedDealType= preferences[PreferenceKeys.selectedDietType]?: DEFAULT_DIET
        val selectedDealTypeID= preferences[PreferenceKeys.selectedDietTypeId]?: 0
        MealandDietType(
            selectedMealType,
            selectedMealTypeID,
            selectedDealType,
            selectedDealTypeID
        )
    }
    val readBackOnline : Flow<Boolean> = appContext.dataStore.data.catch{ exception->
        if (exception is IOException){
            emit(emptyPreferences())
        }else{
            throw exception
        }
    }.map { preferences->
        val backOnline=preferences[PreferenceKeys.backOnline]?: false
        backOnline
    }
}
data class MealandDietType(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int
)