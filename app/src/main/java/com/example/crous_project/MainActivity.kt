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
import com.example.crous_project.CrousCreator


//const val SERVER_BASE_URL = "http://10.0.2.2:3000"

const val SERVER_BASE_URL = "https://app-38070179-aa49-47be-b530-6fece93917b4.cleverapps.io/"
class MainActivity : AppCompatActivity(), ListFragment.OnCrousSelectedListener, CrousCreator, CrousCallback {

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
                    displayListFragment() // Reset to default "Home" mode
                    true
                }

                R.id.navigation_add -> {
                    // Start AddActivity
                    displayCrousAddFragment()
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
            val showFavoritesOnly = currentFragment.arguments?.getBoolean("showFavoritesOnly") ?: false
            val updatedList = if (showFavoritesOnly) {
                crousRepository.getAllCrous().filter { it.favorite }
            } else {
                crousRepository.getAllCrous()
            }
            currentFragment.updateCrousList(updatedList)
        } else if (currentFragment is MapFragment) {
            currentFragment.updateMap()
        }
    }



    private fun displayListFragment() {
        val fragment = ListFragment()
        val bundle = Bundle()
        bundle.putBoolean("showFavoritesOnly", false)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()

        tabLayout.getTabAt(0)?.select()
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
        // should delete?
        tabLayout.getTabAt(0)?.select()
    }

    private fun displayCrousAddFragment() {
        val fragment = CrousAddFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null) // Optional: allows back navigation
            .commit()
    }

    override fun onCrousSelected(crous: Crous) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("crous_id", crous.id)
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

    override fun onCrousCreated(crous: Crous) {
        // Call the API
        crousService.createCrous(crous)
            .enqueue(object : Callback<Crous> {
                override fun onResponse(call: Call<Crous>, response: Response<Crous>) {
                    if (response.isSuccessful) {
                        val crousFromServer = response.body()
                        if (crousFromServer != null) {
                            // Add to local repository
                            crousRepository.addCrous(crousFromServer)

                            // Notify user and finish
                            Toast.makeText(
                                this@MainActivity,
                                "Crous added successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                            // Switch to the list fragment or refresh the view
                            displayListFragment()
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Failed to parse server response.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "API Error: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Crous>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onFavoriteToggled(crousId: String, isFavorite: Boolean) {
        // Update local repository immediately (optimistic update)
        crousRepository.toggleFavorite(crousId, isFavorite)

        // Notify the fragment to refresh the UI
        refreshListFragment()

        // Sync the change with the server
        crousService.toggleFavorite(crousId)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (!response.isSuccessful) {
                        // Revert local change if server call fails
                        crousRepository.toggleFavorite(crousId, !isFavorite)
                        refreshListFragment()

                        Toast.makeText(
                            this@MainActivity,
                            "Failed to sync with the server. Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    // Revert local change if server call fails
                    crousRepository.toggleFavorite(crousId, !isFavorite)
                    refreshListFragment()

                    Toast.makeText(
                        this@MainActivity,
                        "Error: ${t.message}. Changes reverted.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun refreshListFragment() {
        val fragment = supportFragmentManager.findFragmentByTag("ListFragment") as? ListFragment
        fragment?.updateCrousList(crousRepository.getAllCrous())
    }



}