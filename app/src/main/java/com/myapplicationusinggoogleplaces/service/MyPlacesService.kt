package com.myapplicationusinggoogleplaces.service

import android.content.Context
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.myapplicationusinggoogleplaces.R
import com.myapplicationusinggoogleplaces.model.MyPlaces
import com.myapplicationusinggoogleplaces.model.Results

class MyPlacesService {

    companion object {

        fun getResults(context: Context, latLng: LatLng, placeType: String): MutableList<Results> {
            val listResults: MutableList<Results> = mutableListOf()

            val apiKey = context.resources.getString(R.string.google_maps_key)
            var response = HttpConnection.getData(
                buildUrl(
                    latLng.latitude,
                    latLng.longitude,
                    placeType,
                    apiKey
                )
            )
            Log.e("Response: ", " " + response)

            if(response.isNotEmpty()) {
                val gson = Gson()
                var myPlaces = gson.fromJson(response, MyPlaces::class.java)

                for (results in myPlaces.results!!) {
                    listResults.add(results)
                }
            }

            return listResults

        }

        fun buildUrl(
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
            urlString.append("&radius=15000")
            urlString.append("&types=" + placeType.toLowerCase())
            urlString.append("&sensor=false&key=$apiKey")
            return urlString.toString()
        }

    }

}