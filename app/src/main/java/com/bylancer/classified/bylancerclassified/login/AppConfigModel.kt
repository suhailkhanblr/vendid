package com.bylancer.classified.bylancerclassified.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AppConfigModel {

    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("app_name")
    @Expose
    var appName: String? = null
    @SerializedName("default_country")
    @Expose
    var defaultCountry: String? = null
    @SerializedName("detect_live_location")
    @Expose
    var detectLiveLocation: String? = null
    @SerializedName("terms_page_link")
    @Expose
    var termsPageLink: String? = null
    @SerializedName("policy_page_link")
    @Expose
    var policyPageLink: String? = null

}