package com.printoverit.cookbookupdate.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.printoverit.cookbookupdate.IngredientsFragment
import com.printoverit.cookbookupdate.InstructionsFragment
import com.printoverit.cookbookupdate.OverviewFragment
import com.printoverit.cookbookupdate.R
import com.printoverit.cookbookupdate.adapters.PagerAdapter
import com.printoverit.cookbookupdate.room.FavouriteEntity
import com.printoverit.cookbookupdate.viewmodel.FoodViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_details.*
import java.lang.Exception

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    private val args by navArgs<DetailsActivityArgs>()
    private val foodViewModel:FoodViewModel by viewModels()

    private var recipeSaved=false
    private var savedRecipeId=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(appToolbar)
        appToolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(InstructionsFragment())
        fragments.add(IngredientsFragment())

        val titles=ArrayList<String>()
        titles.add("Overview")
        titles.add("Instructions")
        titles.add("Ingredients")
        val resultBundle =Bundle()
        resultBundle.putParcelable("recipeBundle",args.result)

        val pageadapter=PagerAdapter(
            resultBundle,
            fragments,
            this
        )
        viewPager.apply {
            adapter=pageadapter
        }
        TabLayoutMediator(tabLayout,viewPager){tab,position->
            tab.text=titles[position]
        }.attach()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu,menu)
        val menuItem=menu?.findItem(R.id.favourite_menu)
        checkSavedRecipe(menuItem!!)
        return true
    }

    fun checkSavedRecipe(menuItem: MenuItem){
        foodViewModel.readFavouriteRecipes.observe(this,{favouriteEntity->
            //"favouriteEntity" work as a middle man between the Room data and the Usage Class you can take any refference of that class and acces the data
            try {
                for (savedRecipe in favouriteEntity ){
                    if (savedRecipe.result.id==args.result.id){
                        changeItemColor(menuItem,R.color.yellow)
                        savedRecipeId=savedRecipe.id
                        recipeSaved=true
                    }else{
                        changeItemColor(menuItem,R.color.white)
                    }
                }
            }catch (e:Exception){
                Toast.makeText(this,"Exception in Favourite Database",Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==android.R.id.home){
            finish()
        }
        else if (item.itemId==R.id.favourite_menu && !recipeSaved){
            Toast.makeText(applicationContext,"Added to Favourite",Toast.LENGTH_SHORT).show()
            saveToFavorites(item)
            //savedRecipeId=item.itemId
        }else if(item.itemId==R.id.favourite_menu && recipeSaved){
            removeFromFavourites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun removeFromFavourites(item: MenuItem){
        val facoutiteEntity=FavouriteEntity(savedRecipeId,args.result)
        foodViewModel.deleteFavouriteRecipe(facoutiteEntity)
        changeItemColor(item,R.color.white)
        showSnakeBar("Favourite Removed")
        recipeSaved=false
    }
    private fun saveToFavorites(item:MenuItem){
        val favouriteEntity=FavouriteEntity(0,args.result)
        foodViewModel.insertFavouriteRecipe(favouriteEntity)
        changeItemColor(item,R.color.yellow)
        showSnakeBar("Recipe Added Favorite List")
        recipeSaved=true
    }

    private fun showSnakeBar(s: String) {
        Snackbar.make(detailsLayout,s,Snackbar.LENGTH_SHORT).setAction("Okay"){}.show()
    }

    private fun changeItemColor(item: MenuItem, yellow: Int) {
        item.icon.setTint(ContextCompat.getColor(this,yellow))
    }
}