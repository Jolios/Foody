package com.example.foody.data

import android.content.Context
import javax.inject.Inject

class Repository constructor(context: Context){
    val remote = RemoteDataSource()
    val local =  LocalDataSource(context)
}