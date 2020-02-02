package com.myapplicationusinggoogleplaces.view.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.myapplicationusinggoogleplaces.R
import com.myapplicationusinggoogleplaces.contants.PlacesConstant
import com.myapplicationusinggoogleplaces.model.MyPlaces
import com.myapplicationusinggoogleplaces.model.Results
import com.myapplicationusinggoogleplaces.service.MyPlacesService
import com.myapplicationusinggoogleplaces.view.fragments.ListFragment
import com.myapplicationusinggoogleplaces.view.fragments.ProfileFragment
import com.myapplicationusinggoogleplaces.view.fragments.RadarFragment


class NavigationActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    private var navigationView: BottomNavigationView? = null
    private var toolbar: Toolbar? = null
    private var menu: Menu? = null
    private var placesType: Int = 0
    private var layout: LinearLayout? = null
    private var locationsNames = arrayOf(
        "Todos",
        "Aeroportos",
        "Restaurantes",
        "Baladas",
        "Supermercados",
        "Shopping Centers"
    )
    private var options: HashMap<Int, String> = hashMapOf(
        0 to "all",
        1 to "airport",
        2 to "restaurant",
        3 to "bar",
        4 to "supermarket",
        5 to "shopping_mall"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        this.toolbar = findViewById(R.id.toolbar)
        this.navigationView = findViewById(R.id.navigationView)
        this.navigationView?.setOnNavigationItemSelectedListener(this)
        this.layout = findViewById(R.id.llProgressBar)

        if (savedInstanceState == null) {
            this.toolbar?.title = "Radar"
            openFragment(RadarFragment())
        }

        PlacesConstant.latLng.observe(this, Observer<LatLng> {
            getLocations(this, options, placesType, it)
        })

        setSupportActionBar(this.toolbar)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_radar -> {
                this.toolbar?.title = "Radar"
                this.menu?.findItem(R.id.filter)?.isVisible = true
                openFragment(RadarFragment())
            }
            R.id.navigation_list -> {
                this.toolbar?.title = "Lista"
                this.menu?.findItem(R.id.filter)?.isVisible = true
                openFragment(ListFragment())
            }
            R.id.navigation_profile -> {
                this.toolbar?.title = "Perfil"
                this.menu?.findItem(R.id.filter)?.isVisible = false
                openFragment(ProfileFragment())
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

    fun chooseLocations(context: Context) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Categorias")
        builder.setSingleChoiceItems(locationsNames, placesType) { dialog, which ->
            placesType = which
            Log.e("Options", "${options.getValue(which)}")
            Log.e("latLng", "${PlacesConstant.latLng.value!!}")
            getLocations(this, options, placesType, PlacesConstant.latLng.value!!)
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar", null)
        val dialog = builder.create()
        dialog.show()

    }

    private fun getLocations(
        context: Context,
        options: HashMap<Int, String>,
        placesType: Int,
        latLng: LatLng
    ) {
        layout?.visibility = View.VISIBLE
        object : Thread() {
            override fun run() {

                val listResults = mutableListOf<Results>()
                val arrayResponse = mutableListOf<String>()
                when (options.getValue(placesType)) {
                    "all" -> {
                        for (options in options.values) {
                            if (options != "all") {
                                arrayResponse.add(
                                    MyPlacesService.getResult(
                                        context,
                                        latLng,
                                        options
                                    )
                                )
                            }
                        }
                    }
                    else -> {
                        arrayResponse.add(
                            MyPlacesService.getResult(
                                context,
                                latLng,
                                options.getValue(placesType)
                            )
                        )
                    }
                }

                Log.e("array", "ARRAY: $arrayResponse")

                for (s in arrayResponse) {
                    val gson = Gson()
                    var myPlaces = gson.fromJson(s, MyPlaces::class.java)
                    for (results in myPlaces.results!!) {
                        listResults.add(results)
                    }
                }

                PlacesConstant.results.postValue(listResults)
                runOnUiThread { layout?.visibility = View.GONE }
            }
        }.start()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.filter) {
            if(PlacesConstant.latLng.value != null) {
                chooseLocations(this)
            } else {
                Toast.makeText(this, "Parece que sua localização está desativada! :(", Toast.LENGTH_LONG).show()
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

}
