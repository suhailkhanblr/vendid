package com.phoenix.vendido.buysell.webservices.membership

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MembershipPlanList {
    @SerializedName("plans")
    @Expose
    var plans: ArrayList<MembershipPlan>? = null
}
