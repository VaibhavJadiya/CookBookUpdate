package com.printoverit.cookbookupdate

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.printoverit.cookbookupdate.utils.Constaints.Companion.DEFAULT_DIET
import com.printoverit.cookbookupdate.utils.Constaints.Companion.DEFAULT_MAIN_COURSE
import com.printoverit.cookbookupdate.utils.Constaints.Companion.TAG
import com.printoverit.cookbookupdate.viewmodel.RecipiesViewModel
import kotlinx.android.synthetic.main.fragment_recipe_bottom_sheet.view.*


class RecipeBottomSheetFragment : BottomSheetDialogFragment() {

    private var mealTypeChip=DEFAULT_MAIN_COURSE
    private var mealTypeChipID=0
    private var dietTypeChip= DEFAULT_DIET
    private var dietTypeChipId=0
    private lateinit var recipiesViewModel: RecipiesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val mView:View= inflater.inflate(R.layout.fragment_recipe_bottom_sheet, container, false)
        recipiesViewModel.readMealandDietType.asLiveData().observe(viewLifecycleOwner, {value->
            mealTypeChip=value.selectedMealType
            dietTypeChip=value.selectedDietType
            updateChip(value.selectedMealTypeId,mView.mealType_ChipGroup)
            updateChip(value.selectedDietTypeId,mView.DietType_ChipGroup)
        })
        mView.mealType_ChipGroup.setOnCheckedChangeListener { group,selectedChipId ->
                val chip= group.findViewById<Chip>(selectedChipId)
                val selectedMealType=chip.text.toString().lowercase()
                mealTypeChip=selectedMealType
                mealTypeChipID=selectedChipId
        }
       mView.DietType_ChipGroup.setOnCheckedChangeListener { group,selectedChipId ->
           val chip= group.findViewById<Chip>(selectedChipId)
           val selectedDietType=chip.text.toString().lowercase()
           dietTypeChip=selectedDietType
           dietTypeChipId=selectedChipId
       }
        mView.bottom_sheet_submit.setOnClickListener {
            recipiesViewModel.saveMealandDietType(
                mealTypeChip,
                mealTypeChipID,
                dietTypeChip,
                dietTypeChipId
            )
            val action=RecipeBottomSheetFragmentDirections.actionRecipeBottomSheetFragmentToReciepeFragment2(true)
            findNavController().navigate(action)
        }

        return mView
    }

    private fun updateChip(chipId:Int, chipGroup: ChipGroup) {
        if (chipId!=0){
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked=true
            }catch (e:Exception){
                Log.d(TAG, e.message.toString())
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipiesViewModel=ViewModelProvider(requireActivity()).get(RecipiesViewModel::class.java)
    }
}