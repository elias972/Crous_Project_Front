// MainActivity.kt
package com.example.crous_project

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), ListFragment.OnCrousSelectedListener {

    private val crousRepository = CrousRepository
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Load sample data
        loadSampleData()

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

    private fun loadSampleData() {
        // Sample data initialization
        val crousList = listOf(
            Crous(
                id = "1",
                type = "Restaurant",
                zone = "Zone A",
                nom = "Crous Restaurant 1",
                description = "A popular student restaurant in Zone A.",
                contact = "contact@crous1.com",
                lat = 48.8566,
                informations = "Open from 8 AM to 8 PM.",
                closing = null,
                geolocalisation = listOf(48.8566, 2.3522),
                zone2 = null,
                crousandgo = null,
                album = null,
                photo = "",
                favorite = false
            ),
            Crous(
                id = "2",
                type = "Cafeteria",
                zone = "Zone B",
                nom = "Crous Cafeteria 2",
                description = "A cozy cafeteria in Zone B.",
                contact = "contact@crous2.com",
                lat = 43.6047,
                informations = "Open weekdays from 9 AM to 5 PM.",
                closing = "Closed on weekends.",
                geolocalisation = listOf(43.6047, 1.4442),
                zone2 = null,
                crousandgo = null,
                album = null,
                photo = "",
                favorite = false
            )
            // Add more sample Crous objects as needed
        )

        // Update the repository
        crousRepository.clear()
        crousList.forEach { crousRepository.addCrous(it) }
        crousRepository.loadFavorites(this)

        // Update the UI
        updateCurrentFragment()
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
                loadSampleData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
