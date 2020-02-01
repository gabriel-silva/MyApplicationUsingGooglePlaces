package com.myapplicationusinggoogleplaces

import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.myapplicationusinggoogleplaces.fragments.ListFragment
import com.myapplicationusinggoogleplaces.fragments.ProfileFragment
import com.myapplicationusinggoogleplaces.fragments.RadarFragment

class MainActivityNavigation : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    private var navigationView: BottomNavigationView? = null
    private var toolbar: Toolbar? = null
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_navigation)

        this.toolbar = findViewById(R.id.toolbar)
        this.navigationView = findViewById(R.id.navigationView)
        this.navigationView?.setOnNavigationItemSelectedListener(this)

        if(savedInstanceState == null) {
            this.toolbar?.title = "Radar"
            openFragment(RadarFragment.newInstance())
        }

        setSupportActionBar(this.toolbar)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_radar -> {
                this.toolbar?.title = "Radar"
                this.menu?.findItem(R.id.filter)?.isVisible = true
                openFragment(RadarFragment.newInstance())
            }
            R.id.navigation_list -> {
                this.toolbar?.title = "Lista"
                this.menu?.findItem(R.id.filter)?.isVisible = true
                openFragment(ListFragment.newInstance())
            }
            R.id.navigation_profile -> {
                this.toolbar?.title = "Perfil"
                this.menu?.findItem(R.id.filter)?.isVisible = false
                openFragment(ProfileFragment.newInstance())
            }
        }

        return true
    }

    override fun onCreatePanelMenu(featureId: Int, menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.toolbar, menu)
        this.menu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.filter) {

            val colors = arrayOf("red", "green", "blue", "black")

            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Pick a color")
            builder.setItems(colors, DialogInterface.OnClickListener { dialog, which ->
                // the user clicked on colors[which]
            })
            builder.show()

            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

}
