package com.bylancer.classified.bylancerclassified.webservices.membership

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CurrentUserMembershipPlan {
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("package_id")
    @Expose
    var packageId: String? = null
    @SerializedName("plan_title")
    @Expose
    var planTitle: String? = null
    @SerializedName("amount")
    @Expose
    var amount: String? = null
    @SerializedName("pay_mode")
    @Expose
    var payMode: String? = null
    @SerializedName("plan_term")
    @Expose
    var planTerm: String? = null
    @SerializedName("start_date")
    @Expose
    var startDate: String? = null
    @SerializedName("expiry_date")
    @Expose
    var expiryDate: String? = null
    @SerializedName("image_url")
    @Expose
    var imageUrl: String? = null

}