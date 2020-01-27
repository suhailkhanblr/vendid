package com.bylancer.classified.bylancerclassified.webservices.membership

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MembershipPlanList {
    @SerializedName("plans")
    @Expose
    var plans: ArrayList<MembershipPlan>? = null
}
