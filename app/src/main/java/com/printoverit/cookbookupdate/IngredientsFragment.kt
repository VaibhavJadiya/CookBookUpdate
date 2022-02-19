package com.printoverit.cookbookupdate

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.printoverit.cookbookupdate.R
import com.printoverit.cookbookupdate.adapters.IngredientsAdapter
import com.printoverit.cookbookupdate.models.Result
import kotlinx.android.synthetic.main.fragment_ingredients.view.*

class IngredientsFragment : Fragment() {
    private val mAdapater:IngredientsAdapter by lazy { IngredientsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_ingredients, container, false)
        val args=arguments
        val myBundle = args?.getParcelable<Parcelable>("recipeBundle") as Result?
        setupRecyclerView(view.ingredients_recyclerview)
        myBundle?.extendedIngredients?.let { mAdapater.setData(it) }
        return view
    }
    private fun setupRecyclerView(recyclerView: RecyclerView){
        recyclerView.ingredients_recyclerview.adapter=mAdapater
        recyclerView.ingredients_recyclerview.layoutManager=LinearLayoutManager(requireContext())
    }

}