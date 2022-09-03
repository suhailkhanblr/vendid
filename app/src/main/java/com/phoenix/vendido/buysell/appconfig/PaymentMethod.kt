package com.phoenix.vendido.buysell.appconfig

import com.phoenix.vendido.buysell.utils.AppConstants
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PaymentMethod {

    @SerializedName("paypal")
    @Expose
    var paypal: String? = null
    @SerializedName("stripe")
    @Expose
    var stripe: String? = null
    @SerializedName("wire_transfer")
    @Expose
    var wireTransfer: String? = null
    @SerializedName("2checkout")
    @Expose
    var checkout2:String? = null
    @SerializedName("paystack")
    @Expose
    var paystack: String? = null
    @SerializedName("payumoney")
    @Expose
    var payumoney: String? = null

    fun isPayPalActive() = AppConstants.IS_ACTIVE.equals(paypal)
    fun isStripeActive() = AppConstants.IS_ACTIVE.equals(stripe)
    fun isWireTransferActive() = AppConstants.IS_ACTIVE.equals(wireTransfer)
    fun is2CheckoutActive() = AppConstants.IS_ACTIVE.equals(checkout2)
    fun isPayStackActive(): Boolean = AppConstants.IS_ACTIVE.equals(paystack)
    fun isPayUMoneyActive(): Boolean = AppConstants.IS_ACTIVE.equals(payumoney)

}