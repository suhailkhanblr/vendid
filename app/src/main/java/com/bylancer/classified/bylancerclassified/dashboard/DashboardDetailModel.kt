package com.bylancer.classified.bylancerclassified.dashboard

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DashboardDetailModel {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("featured")
    @Expose
    var featured: String? = null
    @SerializedName("urgent")
    @Expose
    var urgent: String? = null
    @SerializedName("highlight")
    @Expose
    var highlight: String? = null
    @SerializedName("category_id")
    @Expose
    var categoryId: String? = null
    @SerializedName("sub_category_id")
    @Expose
    var subCategoryId: String? = null
    @SerializedName("category_name")
    @Expose
    var categoryName: String? = null
    @SerializedName("sub_category_name")
    @Expose
    var subCategoryName: String? = null
    @SerializedName("map_latitude")
    @Expose
    var mapLatitude: String? = null
    @SerializedName("map_longitude")
    @Expose
    var mapLongitude: String? = null
    @SerializedName("hide_phone")
    @Expose
    var hidePhone: String? = null
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("seller_name")
    @Expose
    var sellerName: String? = null
    @SerializedName("seller_username")
    @Expose
    var sellerUsername: String? = null
    @SerializedName("seller_email")
    @Expose
    var sellerEmail: String? = null
    @SerializedName("seller_image")
    @Expose
    var sellerImage: String? = null
    @SerializedName("price")
    @Expose
    var price: String? = null
    @SerializedName("currency")
    @Expose
    var currency: String? = null
    @SerializedName("currency_in_left")
    @Expose
    var currencyInLeft: String? = null
    @SerializedName("negotiable")
    @Expose
    var negotiable: String? = null
    @SerializedName("location")
    @Expose
    var location: String? = null
    @SerializedName("city")
    @Expose
    var city: String? = null
    @SerializedName("state")
    @Expose
    var state: String? = null
    @SerializedName("country")
    @Expose
    var country: String? = null
    @SerializedName("view")
    @Expose
    var view: String? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
    @SerializedName("original_images_path")
    @Expose
    var originalImagesPath: String? = null
    @SerializedName("small_images_path")
    @Expose
    var smallImagesPath: String? = null
    @SerializedName("images")
    @Expose
    var images: List<String>? = null
    @SerializedName("tag")
    @Expose
    var tag: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("custom_data")
    @Expose
    var customData: List<CustomData>? = null

}