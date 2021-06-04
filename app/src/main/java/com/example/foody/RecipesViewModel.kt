package com.example.foody

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.foody.data.DataStoreRepository
import com.example.foody.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RecipesViewModel constructor(application:Application) : AndroidViewModel(application) {

    private val dataStoreRepository: DataStoreRepository = DataStoreRepository(application.applicationContext)
    val readMealAndDietType = dataStoreRepository.readMealAndDietType

    private var mealType = Constants.DEFAULT_MEAL_TYPE
    private var dietType = Constants.DEFAULT_DIET_TYPE

    fun saveMealAndDietType(mealType: String, mealTypeId:Int,dietType:String,dietTypeId:Int){
        viewModelScope.launch(Dispatchers.IO){

            dataStoreRepository.saveMealAndDietType(mealType,mealTypeId,dietType,dietTypeId)
        }
    }
    fun applyQureies(passedMealType: String,passedDietType: String): HashMap<String, String>{
        val queries: HashMap<String, String> = HashMap()

        Log.d("RecipesFragment","insisdeApplyQueries")


        viewModelScope.launch(Dispatchers.Main.immediate) {
            readMealAndDietType.collect { value ->
                mealType = value.selectedMealType
                dietType = value.selectedDietType
                Log.d("RecipesFragment",mealType)
                Log.d("RecipesFragment",dietType)
                Log.d("RecipesFragment","testrecipes")
            }
        }
        Log.d("RecipesFragment",mealType)
        Log.d("RecipesFragment",dietType)
        queries[Constants.QUERY_NUMBER] = Constants.DEFAULT_RECIPES_NUMBER
        queries[Constants.QUERY_API_KEY] = Constants.API_KEY
        queries[Constants.QUERY_TYPE] = passedMealType
        queries[Constants.QUERY_DIET] = passedDietType
        queries[Constants.QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[Constants.QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }
    fun applySearchQuery(searchQuery: String):HashMap<String,String>{
        val queries: HashMap<String,String> = HashMap()
        queries[Constants.QUERY_SEARCH] = searchQuery
        queries[Constants.QUERY_NUMBER] = Constants.DEFAULT_RECIPES_NUMBER
        queries[Constants.QUERY_API_KEY] = Constants.API_KEY
        queries[Constants.QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[Constants.QUERY_FILL_INGREDIENTS] = "true"
        return queries
    }
}