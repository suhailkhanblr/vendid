package com.bylancer.classified.bylancerclassified.utils

/**
 * Created by Ani on 3/24/18.
 */
class AppConstants {
    companion object {
        const val DETAIL_ACTIVITY_PARCELABLE = "DETAIL_ACTIVITY_PARCELABLE"
        const val BUNDLE = "bundle"
        const val REGISTER_URL = "api/v1/?action=register"
        const val LOGIN_URL = "api/v1/?action=login"
        const val FORGOT_PASSWORD_URL = "api/v1/?action=forgot_password"
        const val PRODUCT_LIST_URL = "/api/v1/index.php?action=home_premium_ads"
        const val PRODUCT_DETAIL_URL = "/api/v1/index.php?action=ad_detail"
        const val COUNTRY_DETAIL_URL = "/api/v1/index.php?action=installed_countries"
        const val STATE_DETAIL_URL = "/api/v1/index.php?action=getStateByCountryCode"
        const val CITY_DETAIL_URL = "/api/v1/index.php?action=getCityByStateCode"
        const val BASE_URL = "https://classified.bylancer.com/"
        const val IMAGE_URL = "https://classified.bylancer.com/storage/profile/"
        const val IMAGE_URL_SMALL = "https://classified.bylancer.com/storage/profile/small_"
        const val APP_CONFIG_URL = "/api/v1/index.php?action=app_config"
        const val FETCH_CHAT_URL = "/api/v1/index.php?action=get_all_msg"
        const val IS_ADMIN_APP = false
        val CURRENT_VERSION = "1.0"
        val CURRENCY_IN_LEFT= "0"
        val PREF_FILE = "bylancer_appsgeek"

        const val ERROR = "error"
        const val SUCCESS = "success"
        const val MESSAGE = "message"
        const val PRODUCT_ID = "product_id"
        const val PRODUCT_NAME = "product_name"
        const val PRODUCT_OWNER_NAME = "product_owner_name"
        const val TERMS_CONDITION_TITLE = "terms_condition_title"
        const val TERMS_CONDITION_URL = "terms_condition_url"
        const val HIDE_PHONE = "yes"
        const val CHAT_USER_NAME = "chat_user_name"
        const val CHAT_TITLE = "chat_title"
        const val CHAT_USER_IMAGE = "chat_user_image"
        const val ASSET_TYPE_TEXT = "text"
        const val EMPTY = ""

        enum class PREFERENCES private constructor(private val value: String) {
            USER_ID("user_id"),
            DEVICE_ID("deviceId"),
            USERNAME("username"),
            LOGIN_STATUS("LoginStatus"),
            EMAIL("email_id"),
            PHONE("phone"),
            ADDRESS("address"),
            DISPLAY_NAME("DisplayName"),
            APP_NAME("app_name"),
            PRIVACY_URL("privacy_url"),
            DEFAULT_COUNTRY("default_country"),
            SELECTED_COUNTRY("selected_country"),
            SELECTED_STATE("selected_state"),
            SELECTED_CITY("selected_city"),
            SELECTED_LANGUAGE("selected_Language"),
            LIVE_LOCATION("live_location"),
            TNC_URL("tnc_url"),
            PROFILE_PIC("profile_pic");

            override fun toString(): String {
                return this.value
            }
        };
    }
}