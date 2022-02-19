package com.printoverit.cookbookupdate.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.google.gson.JsonParser
import com.printoverit.cookbookupdate.ReciepeFragmentDirections
import com.printoverit.cookbookupdate.models.FoodRecipeJson
import com.printoverit.cookbookupdate.models.Result
import com.printoverit.cookbookupdate.room.RecipiesEntity
import com.printoverit.cookbookupdate.utils.NetworkResult
import org.jsoup.Jsoup


class RecipiesBinding {

    companion object{

        @BindingAdapter("onRecipeClickListner")
        @JvmStatic
        fun OnRecipeClickListner(recipeRowLayout: ConstraintLayout, result: Result){
            recipeRowLayout.setOnClickListener {
                try {
                    val action=ReciepeFragmentDirections.actionReciepeFragmentToDetailsActivity(result)
                    recipeRowLayout.findNavController().navigate(action)
                }catch (e:Exception){

                }
            }
        }

        @BindingAdapter("readAPiResponse","readDatabase", requireAll = true)
        @JvmStatic
        fun errorImageVisibility(
            imageView: ImageView,
            apiResponse: NetworkResult<FoodRecipeJson>?,
            database: List<RecipiesEntity>?
        ){
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()){
                    imageView.visibility=View.VISIBLE
            }
            else if(apiResponse is NetworkResult.Loading){
                imageView.visibility=View.GONE
            }
            else if (apiResponse is NetworkResult.Success){
                imageView.visibility=View.GONE
            }

        }

        @BindingAdapter("readAPiResponseNO2","readDatabaseNO2", requireAll = true)
        @JvmStatic
        fun errorTextVisibility(
            textView: TextView,
            apiResponse: NetworkResult<FoodRecipeJson>?,
            database: List<RecipiesEntity>?
        ){
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()){
                textView.visibility=View.VISIBLE
                textView.text=apiResponse.message.toString()
            }
            else if(apiResponse is NetworkResult.Loading){
                textView.visibility=View.GONE
            }
            else if (apiResponse is NetworkResult.Success){
                textView.visibility=View.GONE
            }

        }

        @BindingAdapter("htmlParse")
        @JvmStatic
        fun parseHtml(textView: TextView, description:String){
            if(description!= null){
                val deccription=Jsoup.parse(description).text()
                textView.text=deccription
            }
        }
    }

}