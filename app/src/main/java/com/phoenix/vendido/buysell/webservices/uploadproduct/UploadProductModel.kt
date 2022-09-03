package com.phoenix.vendido.buysell.webservices.uploadproduct

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UploadProductModel {
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("picture")
    @Expose
    var url: String? = null
}