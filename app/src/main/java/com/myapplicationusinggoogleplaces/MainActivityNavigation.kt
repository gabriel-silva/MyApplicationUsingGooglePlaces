package com.myapplicationusinggoogleplaces

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.myapplicationusinggoogleplaces.fragments.ListFragment
import com.myapplicationusinggoogleplaces.fragments.ProfileFragment
import com.myapplicationusinggoogleplaces.fragments.RadarFragment

class MainActivityNavigation : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    private var navigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_navigation)

        this.navigationView = findViewById(R.id.navigationView)
        this.navigationView?.setOnNavigationItemSelectedListener(this)

        if(savedInstanceState == null) {
            openFragment(RadarFragment.newInstance())
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_radar -> {
                openFragment(RadarFragment.newInstance())
            }
            R.id.navigation_list -> {
                openFragment(ListFragment.newInstance())
            }
            R.id.navigation_profile -> {
                openFragment(ProfileFragment.newInstance())
            }
        }

        return true
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

}
