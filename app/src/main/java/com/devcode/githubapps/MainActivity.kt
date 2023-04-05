package com.devcode.githubapps

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.devcode.githubapps.databinding.ActivityMainBinding
import com.devcode.githubapps.models.SettingViewModel
import com.devcode.githubapps.utils.SettingPreferences
import com.devcode.githubapps.utils.ViewModelFactory
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var topAppBar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment)
        AppBarConfiguration.Builder(
            R.id.navigation_home, R.id.navigation_favourite, R.id.navigation_profile, R.id.navigation_info
        ).build()
        navView.setupWithNavController(navController)

        topAppBar = binding.topAppBar
        topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.setting ->{
                    val intent = Intent(this, SettingActivity::class.java)
                    startActivity(intent)
                    true
                }
                else ->{
                    false
                }
            }
        }
    }
}