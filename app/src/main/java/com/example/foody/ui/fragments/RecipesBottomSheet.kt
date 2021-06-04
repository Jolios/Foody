package com.example.foody.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.foody.RecipesViewModel
import com.example.foody.databinding.RecipesBottomSheetBinding
import com.example.foody.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.foody.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.lang.Exception
import java.util.*

class RecipesBottomSheet : BottomSheetDialogFragment() {

    private lateinit var recipesViewModel: RecipesViewModel

    private var _binding: RecipesBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel = RecipesViewModel(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = RecipesBottomSheetBinding.inflate(inflater, container, false)

        Log.d("RecipesBottomSheet","test")
        recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner,{value ->
            mealTypeChip = value.selectedMealType
            dietTypeChip = value.selectedDietType
            Log.d("RecipesBottomSheet",value.selectedMealTypeId.toString())
            updateChip(value.selectedMealTypeId,binding.mealTypeChipGroup)
            updateChip(value.selectedDietTypeId,binding.dietTypeChipGroup)

        })
        binding.mealTypeChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedMealType = chip.text.toString().toLowerCase(Locale.ROOT)
            Log.d("RecipesBottomSheet",selectedMealType)
            mealTypeChip = selectedMealType
            mealTypeChipId = selectedChipId
        }
        binding.dietTypeChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedDietType = chip.text.toString().toLowerCase(Locale.ROOT)
            dietTypeChip = selectedDietType
            dietTypeChipId = selectedChipId
        }

        binding.applyBtn.setOnClickListener {
            Log.d("RecipesFragment",dietTypeChip)
            Log.d("RecipesFragment","testOnClick")
            recipesViewModel.saveMealAndDietType(mealTypeChip,mealTypeChipId,dietTypeChip,dietTypeChipId)
            val action = RecipesBottomSheetDirections.actionRecipesBottomSheetToRecipesFragment(true,mealTypeChip,dietTypeChip)
            findNavController().navigate(action)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {


        if(chipId != 0 ){
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            } catch (e:Exception){
                Log.d("RecipesBottomSheet",e.message.toString())
            }
        }

    }


}