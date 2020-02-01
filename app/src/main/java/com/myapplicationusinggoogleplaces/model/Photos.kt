package com.myapplicationusinggoogleplaces.model

import java.io.Serializable

class Photos : Serializable {
    var photo_reference: String? = null
    var height: String? = null
    var html_attributions: Array<String>? = null
    var width: String? = null

    override fun toString(): String {
        return "ClassPojo [photo_reference = $photo_reference, height = $height, html_attributions = $html_attributions, width = $width]"
    }

}