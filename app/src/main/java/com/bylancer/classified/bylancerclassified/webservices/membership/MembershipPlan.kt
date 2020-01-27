package com.bylancer.classified.bylancerclassified.webservices.membership

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MembershipPlan {

    @SerializedName("Selected")
    @Expose
    var selected: Int? = null
    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("recommended")
    @Expose
    var recommended: String? = null
    @SerializedName("cost")
    @Expose
    var cost: String? = null
    @SerializedName("pay_mode")
    @Expose
    var payMode: String? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
    @SerializedName("term")
    @Expose
    var term: String? = null
    @SerializedName("limit")
    @Expose
    var limit: String? = null
    @SerializedName("duration")
    @Expose
    var duration: String? = null
    @SerializedName("featured_fee")
    @Expose
    var featuredFee: String? = null
    @SerializedName("urgent_fee")
    @Expose
    var urgentFee: String? = null
    @SerializedName("highlight_fee")
    @Expose
    var highlightFee: String? = null
    @SerializedName("featured_duration")
    @Expose
    var featuredDuration: String? = null
    @SerializedName("urgent_duration")
    @Expose
    var urgentDuration: String? = null
    @SerializedName("highlight_duration")
    @Expose
    var highlightDuration: String? = null
    @SerializedName("top_search_result")
    @Expose
    var topSearchResult: String? = null
    @SerializedName("show_on_home")
    @Expose
    var showOnHome: String? = null
    @SerializedName("show_in_home_search")
    @Expose
    var showInHomeSearch: String? = null

}