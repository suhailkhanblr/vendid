package com.phoenix.vendido.buysell.webservices.settings

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StateListModel {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("code")
    @Expose
    var code: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null

}