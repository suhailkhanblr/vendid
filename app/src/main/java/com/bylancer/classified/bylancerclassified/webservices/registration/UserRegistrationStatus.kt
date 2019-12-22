package com.bylancer.classified.bylancerclassified.webservices.registration

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserRegistrationStatus {
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("user_id")
    @Expose
    var userId: String? = null
    @SerializedName("username")
    @Expose
    var username: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("email")
    @Expose
    var email: String? = null
    @SerializedName("picture")
    @Expose
    var profilePicture: String? = null
}