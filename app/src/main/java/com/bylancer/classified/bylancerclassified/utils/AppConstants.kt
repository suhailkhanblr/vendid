package com.bylancer.classified.bylancerclassified.utils

/**
 * Created by Ani on 3/24/18.
 */
class AppConstants {
    companion object {
        const val DETAIL_ACTIVITY_PARCELABLE = "DETAIL_ACTIVITY_PARCELABLE"
        const val BUNDLE = "bundle"
        const val BASE_URL = "https://www.bylancer.com/"
        const val REGISTER_URL = "quickad-demo/api/v1/?action=register"
        const val LOGIN_URL = "quickad-demo/api/v1/?action=login"
        const val FORGOT_PASSWORD_URL = "/api/v1/?action=forgot_password"
        const val PRODUCT_LIST_URL =  "quickad-demo/api/v1/index.php?action=home_latest_ads"
        const val PRODUCT_DETAIL_URL = "quickad-demo/api/v1/index.php?action=ad_detail"
        const val COUNTRY_DETAIL_URL = "quickad-demo/api/v1/index.php?action=installed_countries"
        const val STATE_DETAIL_URL = "quickad-demo/api/v1/index.php?action=getStateByCountryCode"
        const val CITY_DETAIL_URL = "quickad-demo/api/v1/index.php?action=getCityByStateCode"
        const val IMAGE_URL = BASE_URL  + "/storage/profile/"
        const val IMAGE_URL_SMALL = BASE_URL + "/storage/profile/small_"
        const val PRODUCT_IMAGE_URL = BASE_URL + "quickad-demo/storage/products/"
        const val APP_CONFIG_URL = "quickad-demo/api/v1/index.php?action=app_config"
        const val FETCH_CHAT_URL = "quickad-demo/api/v1/index.php?action=get_all_msg"
        const val MAKE_AN_OFFER = "/quickad-demo/api/v1/?action=make_offer"
        const val FETCH_GROUP_CHAT_URL = "quickad-demo/api/v1/?action=chat_conversation"
        const val FETCH_LANGUAGE_PACK_URL = "quickad-demo/api/v1/index.php?action=language_file"
        const val SEND_CHAT_URL = "quickad-demo/api/v1/index.php?action=send_message"
        const val GET_NOTIFICATION_MESSAGE_URL = "quickad-demo/api/v1/index.php?action=get_notification"
        const val IS_ADMIN_APP = false
        val CURRENT_VERSION = "1.0"
        val CURRENCY_IN_LEFT= "0"
        val PREF_FILE = "bylancer_appsgeek"
        val SUPPORT_EMAIL = ""

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
        const val CATEGORY = "category"
        const val SUB_CATEGORY = "sub_category"
        const val DATABASE_NAME = "property_app_db"

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
            PROFILE_PIC("profile_pic"),
            LANGUAGE_PACK("language_pack"),
            COUNTRY_LIST("country_list"),
            CONTINUE_BROWSING_TEXT("continue_browsing"),
            CONTINUE_BROWSING_CATEGORY_ID("continue_browsing_category"),
            CONTINUE_BROWSING_IMAGE("continue_browsing_image"),
            CONTINUE_BROWSING_SUB_CATEGORY_ID("continue_browsing_sub_category"),
            APP_CONFIG("app_config_details");

            override fun toString(): String {
                return this.value
            }
        };
    }
}