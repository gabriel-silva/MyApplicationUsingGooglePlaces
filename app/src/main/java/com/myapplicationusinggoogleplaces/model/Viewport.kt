package com.myapplicationusinggoogleplaces.model

import java.io.Serializable

class Viewport : Serializable {
    var southwest: Southwest? = null
    var northeast: Northeast? = null

    override fun toString(): String {
        return "ClassPojo [southwest = $southwest, northeast = $northeast]"
    }

}