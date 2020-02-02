package com.myapplicationusinggoogleplaces.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myapplicationusinggoogleplaces.R
import com.myapplicationusinggoogleplaces.contants.PlacesConstant
import com.myapplicationusinggoogleplaces.view.adapter.PlaceRecyclerViewAdapter

class ListFragment : Fragment() {

    private lateinit var recyclerViewPlaces: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.list_fragment, container, false)
        recyclerViewPlaces = view.findViewById(R.id.recyclerViewPlaces)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PlacesConstant.results.observe(this, Observer {

            val adapter = PlaceRecyclerViewAdapter(it, PlacesConstant.latLng.value!!)
            val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
            recyclerViewPlaces.layoutManager = layoutManager
            recyclerViewPlaces.itemAnimator = DefaultItemAnimator()
            recyclerViewPlaces.adapter = adapter
            adapter.notifyDataSetChanged()

        })

    }
}