package com.printoverit.cookbookupdate

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.printoverit.cookbookupdate.adapters.FoodAdapter
import com.printoverit.cookbookupdate.databinding.FragmentReciepeBinding
import com.printoverit.cookbookupdate.utils.Constaints.Companion.API_KEY
import com.printoverit.cookbookupdate.utils.NetworkListner
import com.printoverit.cookbookupdate.utils.NetworkResult
import com.printoverit.cookbookupdate.utils.observeOnce
import com.printoverit.cookbookupdate.viewmodel.FoodViewModel
import com.printoverit.cookbookupdate.viewmodel.RecipiesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_reciepe.view.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReciepeFragment : Fragment() , SearchView.OnQueryTextListener{
    private val TAG="tag"
    private val mAdapter by lazy { FoodAdapter() }
    //private lateinit var mView: View
    private lateinit var foodviewmodel: FoodViewModel
    private lateinit var reciepeViewModel: RecipiesViewModel
    private var _binding: FragmentReciepeBinding? =null
    private val binding get()= _binding!!

    //Bottom Navigation Agrs
    private val args by navArgs<ReciepeFragmentArgs>()

    //Read NetrworkLisetner
    private lateinit var networkListener: NetworkListner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        foodviewmodel=ViewModelProvider(requireActivity()).get(FoodViewModel::class.java )
        reciepeViewModel=ViewModelProvider(requireActivity()).get(RecipiesViewModel::class.java )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentReciepeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner=this
        binding.mainViewmodel = foodviewmodel
        binding.foodActionBtn.setOnClickListener {
            if(reciepeViewModel.networkStatus){
                findNavController().navigate(R.id.action_reciepeFragment_to_recipeBottomSheetFragment)
            }else{
                reciepeViewModel.showNetworkStatus()
            }

        }

        SetupRecyclerView()
        setHasOptionsMenu(true)
        //requestApiData()
        //readOfflineData()
        reciepeViewModel.readBackOnline.observe(viewLifecycleOwner,{
            reciepeViewModel.backOnline=it
        })
        lifecycleScope.launch {
            networkListener=NetworkListner()
            networkListener.checkNetworkAvailability(requireContext()).collect {status->
                Log.d("NetworkListener", status.toString())
                reciepeViewModel.networkStatus=status
                reciepeViewModel.showNetworkStatus()
                readOfflineData()
            }
        }

        return  binding.root
    }

   override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
       inflater.inflate(R.menu.recipe_menu,menu)
       val search=menu.findItem(R.id.menu_search)
       val searchView=search.actionView as? SearchView
       searchView?.isSubmitButtonEnabled=true
       searchView?.setOnQueryTextListener(this)
   }
    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query!=null){
            searchedRequestApiData(query)
        }
       return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
     return  true
    }

    private fun readOfflineData() {
      lifecycleScope.launch {
          foodviewmodel.readRecipes.observeOnce(viewLifecycleOwner, {
                  database->
              if (database.isNotEmpty() && !args.backFromBottomSheet){
                  Toast.makeText(context,"Loading Offline Data",Toast.LENGTH_SHORT).show()
                  Log.d(TAG, "readOfflineData: Called ")
                  mAdapter.setData(database[0].foodRecipeJson)
                  HideShimmerEffects()
              }else{
                  requestApiData()
              }
          })
      }
    }

    private fun ShowShimmerEffect(){
        binding.shimmerReciepeRv.showShimmer()
    }
    private fun HideShimmerEffects(){
        binding.shimmerReciepeRv.hideShimmer()
    }
    private fun requestApiData(){
        Toast.makeText(context,"Calling Online Data",Toast.LENGTH_SHORT).show()
        foodviewmodel.getRecipies(reciepeViewModel.applyQuries())
        foodviewmodel.recipeResponse.observe(viewLifecycleOwner, { response ->
            when (response) {

                is NetworkResult.Success->{
                    HideShimmerEffects()
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error->{
                    HideShimmerEffects()
                    loadDatafromCache()
                    Toast.makeText(context,response.message.toString(),Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading->{
                    ShowShimmerEffect()
                }
            }

        })
    }
    private fun searchedRequestApiData(searchQuery:String){
        ShowShimmerEffect()
        foodviewmodel.getSearchedRecipes(reciepeViewModel.searchApplyQueries(searchQuery))
        foodviewmodel.searchedRecipeResponse.observe(viewLifecycleOwner,{searchResponce->
            when (searchResponce) {
                is NetworkResult.Success->{
                    HideShimmerEffects()
                    searchResponce.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error->{
                    HideShimmerEffects()
                    loadDatafromCache()
                    Toast.makeText(context,searchResponce.message.toString(),Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading->{
                    ShowShimmerEffect()
                }
            }
        })

    }

    private fun loadDatafromCache(){
        lifecycleScope.launch {
            foodviewmodel.readRecipes.observe(viewLifecycleOwner,{database->
                if (database.isNotEmpty()){
                    mAdapter.setData(database[0].foodRecipeJson)
                }

            })
        }

    }

    private fun SetupRecyclerView(){
        binding.shimmerReciepeRv.adapter=mAdapter
        binding.shimmerReciepeRv.layoutManager=LinearLayoutManager(requireContext())
        ShowShimmerEffect()
    }

    override fun onDestroy() {
        super.onDestroy()
        //prevents Memory Leak
        _binding=null
    }


}