package com.example.foody.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.foody.MainViewModel
import com.example.foody.R
import com.example.foody.adapters.PagerAdapter
import com.example.foody.data.database.FavoritesEntity
import com.example.foody.databinding.ActivityDetailsBinding
import com.example.foody.ui.fragments.IngredientsFragment
import com.example.foody.ui.fragments.InstructionsFragment
import com.example.foody.ui.fragments.OverviewFragment
import com.example.foody.util.Constants
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel: MainViewModel by viewModels()

    private var recipeSaved = false
    private var savedRecipeId = 0
    private var databaseRef : DatabaseReference = Firebase.database("https://foody-449b5-default-rtdb.europe-west1.firebasedatabase.app/").reference
    private val uid = FirebaseAuth.getInstance().uid


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")

        val resultBundle = Bundle()
        resultBundle.putParcelable(Constants.RECIPE_RESULT_KEY,args.result)

        val pagerAdapter = PagerAdapter(
            resultBundle,
            fragments,
            this
        )
        binding.viewPager2.apply {
            adapter = pagerAdapter
        }


        TabLayoutMediator(binding.tabLayout,binding.viewPager2){tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu,menu)
        val menuItem = menu?.findItem(R.id.save_to_favorties_menu)
        checkSavedRecipes(menuItem!!)
        return true
    }

    private fun checkSavedRecipes(menuItem: MenuItem) {
        mainViewModel.readFavoriteRecipes.observe(this,{favoritesEntity ->
            try{
                for (savedRecipe in favoritesEntity){
                    if(savedRecipe.result.id == args.result.id){
                        changeMenuItemColor(menuItem,R.color.yellow)
                        savedRecipeId = savedRecipe.id
                        recipeSaved = true
                    }else{
                        changeMenuItemColor(menuItem,R.color.white)
                    }
                }
            }catch (e:Exception){
                Log.d("DetailsActivity",e.message.toString())
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        } else if (item.itemId == R.id.save_to_favorties_menu && !recipeSaved){
            saveToFavorites(item)
        } else if(item.itemId == R.id.save_to_favorties_menu && recipeSaved){
            removeFromFavorites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveToFavorites(item: MenuItem) {
        val favoritesEntity =
            FavoritesEntity(
                0,
                args.result
            )
        databaseRef.child(uid!!).child("Favorites").child(favoritesEntity.result.id.toString()).setValue(favoritesEntity)
        mainViewModel.insertFavoriteRecipes(favoritesEntity)
        changeMenuItemColor(item,R.color.yellow)
        showSnackBar("Recipe saved.")
        recipeSaved = true
    }

    private fun removeFromFavorites(item: MenuItem){
        val favoritesEntity =
        FavoritesEntity(
            savedRecipeId,
            args.result
        )
        databaseRef.child(uid!!).child("Favorites").child(favoritesEntity.result.id.toString()).removeValue()
        mainViewModel.deleteFavoriteRecipes(favoritesEntity)
        changeMenuItemColor(item,R.color.white)
        showSnackBar("Removed from Favorites")
        recipeSaved = false
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.detailsLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay"){}
            .show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this,color))
    }
}