package com.myapplicationusinggoogleplaces.model

import java.io.Serializable

class Location : Serializable {
    var lng: String? = null
    var lat: String? = null

    override fun toString(): String {
        return "ClassPojo [lng = $lng, lat = $lat]"
    }
}