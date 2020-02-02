package com.myapplicationusinggoogleplaces.service

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.myapplicationusinggoogleplaces.R

class MyPlacesService {

    companion object {

        fun getResult(
            context: Context,
            latLng: LatLng,
            placeType: String
        ): String {
            val apiKey = context.resources.getString(R.string.google_maps_key)
            return HttpConnection.getData(
                buildUrl(
                    latLng.latitude,
                    latLng.longitude,
                    placeType,
                    apiKey
                )
            )
        }

        private fun buildUrl(
            latitude: Double,
            longitude: Double,
            placeType: String,
            apiKey: String
        ): String {
            val urlString = StringBuilder("https://maps.googleapis.com/maps/api/place/search/json?")
            urlString.append("&location=")
            urlString.append(latitude)
            urlString.append(",")
            urlString.append(longitude)
            urlString.append("&radius=5000") //raio de 5km
            urlString.append("&types=" + placeType.toLowerCase())
            urlString.append("&sensor=false&key=$apiKey")
            return urlString.toString()
        }

    }

}