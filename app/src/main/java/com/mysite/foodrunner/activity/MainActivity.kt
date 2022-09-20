package com.mysite.foodrunner.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.mysite.foodrunner.R
import com.mysite.foodrunner.fragments.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var sh: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sh = getSharedPreferences(getString(R.string.myFile), Context.MODE_PRIVATE)
        if (!sh.getBoolean("isLoggedIn", false)) {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }
        setUpToolBar()
        val actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.close_drawer, R.string.open_drawer)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        setHomeFragment()
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homeDashBoard -> setHomeFragment()
                R.id.favourites -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, FavouritesFragment())
                        .commit()
                    supportActionBar?.title = "Favourites"
                    drawerLayout.closeDrawers()
                }
                R.id.history -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout,HistoryFragment())
                        .commit()
                    supportActionBar?.title = "Order History"
                    drawerLayout.closeDrawers()
                }
                R.id.faqs -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, FaqsFragment())
                        .commit()
                    supportActionBar?.title = "FAQ's"
                    drawerLayout.closeDrawers()
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, AboutFragment()).commit()
                    supportActionBar?.title = "Your Profile"
                    drawerLayout.closeDrawers()
                }
                R.id.logout -> {
                    sh.edit().putBoolean("isLoggedIn", false).apply()
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }
            return@setNavigationItemSelectedListener true
        }

    }

    private fun setUpToolBar() {
        setSupportActionBar(toolbar)
        supportActionBar.apply {
            this?.title = "toolbar"
            this?.setHomeButtonEnabled(true)
            this?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frameLayout)
        when (currentFragment) {
            !is HomeFragment -> setHomeFragment()
            else -> super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home)
            drawerLayout.openDrawer(GravityCompat.START)
        return super.onOptionsItemSelected(item)
    }

    private fun setHomeFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, HomeFragment()).commit()
        supportActionBar?.title = "Home"
        navigationView.setCheckedItem(R.id.homeDashBoard)
        drawerLayout.closeDrawers()
    }
}