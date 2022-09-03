package com.phoenix.vendido.buysell.webservices.transaction

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TransactionResponseModel {
    @SerializedName("status")
    @Expose
    var success : String? = null
}
