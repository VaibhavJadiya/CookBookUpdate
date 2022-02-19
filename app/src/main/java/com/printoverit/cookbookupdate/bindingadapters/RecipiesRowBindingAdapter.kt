package com.printoverit.cookbookupdate.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import com.printoverit.cookbookupdate.R

class RecipiesRowBindingAdapter {
    companion object{
        @BindingAdapter("setNumberofLikes")
        @JvmStatic
        fun setNumberofLikes(textView: TextView,likes:Int){
                textView.text=likes.toString()
        }
        @BindingAdapter("setNumberofMinutes")
        @JvmStatic
        fun setNumberofMinutes(textView: TextView,minutes:Int){
            textView.text=minutes.toString()
        }

        @BindingAdapter("applyVeganColor")
        @JvmStatic
        fun applyVeganColor(view: View, vegan: Boolean){
            if (vegan){
                when(view){
                   is TextView->{
                      view.setTextColor(ContextCompat.getColor(view.context, R.color.green ))
                    }
                    is ImageView->{
                        view.setColorFilter(ContextCompat.getColor(view.context,R.color.green ))
                    }
                }
            }
        }
        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView , url :String){
            imageView.load(url){
                    crossfade(600)
                error(R.drawable.ic_no_internet_image)
            }
        }
    }
}