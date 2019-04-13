package com.bylancer.classified.bylancerclassified.webservices.languagepack

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONObject
import java.util.*

class LanguagePackModel() {

    @SerializedName("language")
    @Expose
    var language: String? = null
    @SerializedName("text")
    @Expose
    var text: Map<String, String>? = null

}
