package com.myapplicationusinggoogleplaces.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Results : Serializable {
    var photos: Array<Photos>? = null
    var id: String? = null
    var place_id: String? = null
    var icon: String? = null
    var vicinity: String? = null
    var scope: String? = null
    var name: String? = null
    var rating: String? = null
    var types: Array<String>? = null
    var reference: String? = null
    var geometry: Geometry? = null
    @SerializedName("opening_hours")
    var openingHours: OpeningHours? = null

    override fun toString(): String {
        return "ClassPojo [photos = $photos, id = $id, place_id = $place_id, icon = $icon, vicinity = $vicinity, scope = $scope, name = $name, types = $types, reference = $reference, geometry = $geometry]"
    }

}