package com.myapplicationusinggoogleplaces.model

import java.io.Serializable

class MyPlaces : Serializable {
    var next_page_token: String? = null
    var results: List<Results>? = null
    var html_attributions: Array<String>?= null
    var status: String? = null

    override fun toString(): String {
        return "ClassPojo [next_page_token = $next_page_token, results = $results, html_attributions = $html_attributions, status = $status]"
    }

}