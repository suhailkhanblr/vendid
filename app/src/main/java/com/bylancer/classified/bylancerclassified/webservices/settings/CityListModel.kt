package com.bylancer.classified.bylancerclassified.webservices.settings

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CityListModel {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null

}