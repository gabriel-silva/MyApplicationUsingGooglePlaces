package com.myapplicationusinggoogleplaces.contants

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.myapplicationusinggoogleplaces.model.Results

object PlacesConstant {
    var results: MutableLiveData<MutableList<Results>> = MutableLiveData()
    var latLng: MutableLiveData<LatLng> = MutableLiveData()
}
