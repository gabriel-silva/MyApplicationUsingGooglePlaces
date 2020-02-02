package com.myapplicationusinggoogleplaces.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.myapplicationusinggoogleplaces.R
import com.myapplicationusinggoogleplaces.model.Results

class PlaceRecyclerViewAdapter(
    results: MutableList<Results>,
    latLng: LatLng
) :
    RecyclerView.Adapter<PlaceRecyclerViewAdapter.ViewHolder?>() {
    private val results: MutableList<Results>
    private val lat: Double
    private val lng: Double

    init {
        this.results = results
        this.lat = latLng.latitude
        this.lng = latLng.longitude
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.layout_place_single, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        val results: Results = results[position]
        holder.bind(results)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView
        var address: TextView

        fun bind(results: Results) {
            name.setText(results.name)
            address.setText(results.vicinity)
        }

        init {
            name = view.findViewById(R.id.textViewPlaceName)
            address = view.findViewById(R.id.textViewAddress)
        }
    }

    override fun getItemCount(): Int {
        return results.size
    }
}
