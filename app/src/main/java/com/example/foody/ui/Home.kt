package com.example.foody.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.foody.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class Home : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        navController = findNavController(R.id.navHostFragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.recipesFragment,
                R.id.favoriteRecipesFragment,
                R.id.foodJokeFragment
            )
        )
        //connect nav graph to bottom nav view
        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setupWithNavController(navController)
        // connect nav graph to action bar to change titles accordingly
        setupActionBarWithNavController(navController,appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun Logout(view: View) {
        var sharedPreferences = getSharedPreferences("infouser", Context.MODE_PRIVATE)
        var editor = sharedPreferences.edit()
        editor.putBoolean("isConnected", false)
        editor.apply()
        val myIntent = Intent(this@Home, SignIn::class.java)
        this@Home.startActivity(myIntent)
    }
}