// MainActivity.kt
package com.example.crous_project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.bottomnavigation.BottomNavigationView

import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val SERVER_BASE_URL = "http://10.0.2.2:3000"

class MainActivity : AppCompatActivity(), ListFragment.OnCrousSelectedListener {

    private val crousRepository = CrousRepository

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(SERVER_BASE_URL)
        .build()

    private val crousService = retrofit.create(CrousService::class.java)

    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Load data from API
        loadCrousDataFromApi()

        // Setup TabLayout
        tabLayout = findViewById(R.id.tab_layout)

        // Set default tab and fragment
        if (savedInstanceState == null) {
            tabLayout.getTabAt(0)?.select()
            displayListFragment()
        }

        // Handle tab selection
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> displayListFragment()
                    1 -> displayMapFragment()
                    2 -> displayInfoFragment()
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        // Setup BottomNavigationView
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    // Return to main activity or refresh
                    tabLayout.getTabAt(0)?.select()
                    true
                }
                R.id.navigation_add -> {
                    // Start AddActivity
                    val intent = Intent(this, CrousAddActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_favorite -> {
                    // Display only favorite Crous in the list
                    displayFavoriteCrous()
                    true
                }
                else -> false
            }
        }
    }

    private fun loadCrousDataFromApi() {
        crousService.getAllCrous().enqueue(object : Callback<List<Crous>> {
            override fun onResponse(call: Call<List<Crous>>, response: Response<List<Crous>>) {
                val crousList = response.body()
                if (crousList != null) {
                    crousRepository.clear()
                    crousList.forEach { crousRepository.addCrous(it) }
                    crousRepository.loadFavorites(this@MainActivity)
                    updateCurrentFragment()
                } else {
                    Log.w("CrousAPI", "Response body is null.")
                }
            }

            override fun onFailure(call: Call<List<Crous>>, t: Throwable) {
                Log.e("CrousAPI", "API call failed. Error: ${t.message}", t)
                Toast.makeText(
                    this@MainActivity,
                    "Failed to load data: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun updateCurrentFragment() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment is ListFragment) {
            currentFragment.updateCrousList(crousRepository.getAllCrous())
        } else if (currentFragment is MapFragment) {
            currentFragment.updateMap()
        }
    }


    private fun displayListFragment() {
        val fragment = ListFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun displayMapFragment() {
        val fragment = MapFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun displayInfoFragment() {
        val fragment = InfoFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun displayFavoriteCrous() {
        val fragment = ListFragment()
        val bundle = Bundle()
        bundle.putBoolean("showFavoritesOnly", true)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
        // Update tab selection
        tabLayout.getTabAt(0)?.select()
    }

    override fun onCrousSelected(crous: Crous) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("crous", crous)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        updateCurrentFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {

                // Load data from API
                loadCrousDataFromApi()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
