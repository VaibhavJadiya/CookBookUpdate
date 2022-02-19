package com.printoverit.cookbookupdate

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.printoverit.cookbookupdate.adapters.FavouriteRecipeAdapter
import com.printoverit.cookbookupdate.databinding.FavouriteRecipeItemBinding
import com.printoverit.cookbookupdate.databinding.FragmentFavouraiteReciepeBinding
import com.printoverit.cookbookupdate.viewmodel.FoodViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_favouraite_reciepe.view.*
@AndroidEntryPoint
class FavouraiteReciepeFragment : Fragment() {

    private val foodViewModel:FoodViewModel by viewModels()
    private val mAdapter:FavouriteRecipeAdapter by lazy{FavouriteRecipeAdapter(requireActivity(),foodViewModel)}


   private var _binding: FragmentFavouraiteReciepeBinding ? = null
   private val binding get()=_binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentFavouraiteReciepeBinding.inflate(inflater,container,false)
        binding.lifecycleOwner=this
        binding.foodViewModelBind=foodViewModel //Binding Data with the Data of the Layout
        binding.mAdapter=mAdapter   //Binding Data with the Data of the Layout
           setupRecyclerView(binding.favouriteRecipeRecyclerView)
        setHasOptionsMenu(true)
    //  foodViewModel.readFavouriteRecipes.observe(viewLifecycleOwner,{favouriteEntity->
    //      mAdapter.setData(favouriteEntity)
    //  })
        return binding.root
    }

    fun setupRecyclerView(recyclerView: RecyclerView){
        recyclerView.adapter=mAdapter
        recyclerView.layoutManager=LinearLayoutManager(requireContext())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
        mAdapter.clearContextualActionMode()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fav_recipe_contextual_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.favourite_clear_all){
            foodViewModel.deleteAllFavouriteRecipe()
            showSnackBar("All Recipes Deleted")
        }
        return super.onOptionsItemSelected(item)
    }
    private fun showSnackBar(messsgae:String){
        Snackbar.make(binding.root,messsgae, Snackbar.LENGTH_SHORT).setAction("Done"){}.show()
    }
}