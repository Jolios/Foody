package com.example.foody.data

import com.example.foody.data.network.FoodRecipesApi
import com.example.foody.models.FoodJoke
import com.example.foody.models.FoodRecipe
import com.example.foody.util.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RemoteDataSource() {
    private val OkHttpClient : OkHttpClient = okhttp3.OkHttpClient.Builder()
                                        .readTimeout(15,TimeUnit.SECONDS)
                                        .connectTimeout(15,TimeUnit.SECONDS)
                                        .build()
    private val GsonConverterFactory : GsonConverterFactory = retrofit2.converter.gson.GsonConverterFactory.create()

    private val RetrofitInstance : Retrofit = Retrofit.Builder()
                                                      .baseUrl(BASE_URL)
                                                      .client(OkHttpClient)
                                                      .addConverterFactory(GsonConverterFactory)
                                                      .build()
    private val foodRecipesApi: FoodRecipesApi = RetrofitInstance.create(
        FoodRecipesApi::class.java)

    suspend fun getRecipes(queries: Map<String, String>) : Response<FoodRecipe>{
        return foodRecipesApi.getRecipes(queries)
    }

    suspend fun searchRecipes(searchQuery: Map<String,String>) : Response<FoodRecipe>{
        return  foodRecipesApi.searchRecipes(searchQuery)
    }
    suspend fun getFoodJoke(apiKey: String) : Response<FoodJoke>{
        return foodRecipesApi.getFoodJoke(apiKey)
    }
}