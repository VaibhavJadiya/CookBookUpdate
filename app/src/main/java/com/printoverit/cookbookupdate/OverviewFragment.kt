package com.printoverit.cookbookupdate

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import coil.load
import com.printoverit.cookbookupdate.R
import com.printoverit.cookbookupdate.models.Result
import kotlinx.android.synthetic.main.fragment_overview.view.*
import org.jsoup.Jsoup

class OverviewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_overview, container, false)

        val args=arguments
        val myBundle = args?.getParcelable<Parcelable>("recipeBundle") as Result?
        //Receiving the Data from Bundle and then arranging the data again the form of Result Module :)
        view.recipe_image.load(myBundle?.image)
        val desc= Jsoup.parse(myBundle?.summary).text()
        view.recipeDescriptionText.text= desc
        view.title_textView.text=myBundle?.title
        view.likes_text.text=myBundle?.aggregateLikes.toString()
        view.duration_text.text=myBundle?.readyInMinutes.toString()
        if (myBundle?.cheap==true){
         view.itemImage6.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
         view.itemText6.setTextColor(ContextCompat.getColor(requireContext(),R.color.light_green))
        }
        if (myBundle?.veryHealthy==true){
            view.itemImage5.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            view.itemText5.setTextColor(ContextCompat.getColor(requireContext(),R.color.light_green))
        }
        if (myBundle?.dairyFree==true){
            view.itemImage4.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            view.itemText4.setTextColor(ContextCompat.getColor(requireContext(),R.color.light_green))
        }
        if (myBundle?.glutenFree==true){
            view.itemImage3.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            view.itemText3.setTextColor(ContextCompat.getColor(requireContext(),R.color.light_green))
        }
        if (myBundle?.vegan==true){
            view.itemImage2.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            view.itemText2.setTextColor(ContextCompat.getColor(requireContext(),R.color.light_green))
        }
        if (myBundle?.vegetarian==true){
            view.itemImage1.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            view.itemText1.setTextColor(ContextCompat.getColor(requireContext(),R.color.light_green))
        }

        return  view
    }

}