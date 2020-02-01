package com.myapplicationusinggoogleplaces.model

import java.io.Serializable

class Geometry : Serializable {
    var viewport: Viewport? = null
    var location: Location? = null

    override fun toString(): String {
        return "ClassPojo [viewport = $viewport, location = $location]"
    }
}