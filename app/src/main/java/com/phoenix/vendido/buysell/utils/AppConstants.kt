package com.phoenix.vendido.buysell.utils

import android.graphics.Color

/**
 * Created by Ani on 3/24/18.
 */
class AppConstants {

    companion object {
        const val DETAIL_ACTIVITY_PARCELABLE = "DETAIL_ACTIVITY_PARCELABLE"
        const val BUNDLE = "bundle"
        const val BASE_URL = "https://flybuy.site/"
        const val REGISTER_URL = "api/v1/?action=register"
        const val LOGIN_URL = "api/v1/?action=login"
        const val FORGOT_PASSWORD_URL = "api/v1/?action=forgot_password"
        const val PRODUCT_LIST_URL =  "api/v1/index.php?action=home_latest_ads"
        const val MY_PRODUCT_LIST_URL =  "api/v1/index.php?action=home_latest_ads"
        const val FEATURED_URGENT_LIST_URL =  "api/v1/index.php?action=featured_urgent_ads"
        const val SEARCH_LIST_URL =  "api/v1/index.php?action=search_post"
        const val PRODUCT_DETAIL_URL = "api/v1/index.php?action=ad_detail"
        const val COUNTRY_DETAIL_URL = "api/v1/index.php?action=installed_countries"
        const val STATE_DETAIL_URL = "api/v1/index.php?action=getStateByCountryCode"
        const val CITY_DETAIL_URL = "api/v1/index.php?action=getCityByStateCode"
        const val PAY_U_HASH_URL = "api/v1/moneyhash.php"
        const val IMAGE_URL = BASE_URL  + "storage/profile/"
        const val IMAGE_URL_SMALL = BASE_URL + "storage/profile/small_"
        const val PRODUCT_IMAGE_URL = BASE_URL + "storage/products/"
        const val APP_CONFIG_URL = "api/v1/index.php?action=app_config"
        const val FETCH_CHAT_URL = "api/v1/index.php?action=get_all_msg"
        const val MAKE_AN_OFFER = "api/v1/?action=make_offer"
        const val FETCH_GROUP_CHAT_URL = "api/v1/?action=chat_conversation"
        const val FETCH_LANGUAGE_PACK_URL = "api/v1/index.php?action=language_file"
        const val SEND_CHAT_URL = "api/v1/index.php?action=send_message"
        const val GET_NOTIFICATION_MESSAGE_URL = "api/v1/index.php?action=get_notification"
        const val ADD_FIREBASE_DEVICE_TOKEN_URL = "api/v1/index.php?action=add_firebase_device_token"
        const val UPLOAD_PROFILE_PIC_URL = "api/v1/index.php?action=upload_profile_picture"
        const val UPLOAD_PRODUCT_PIC_URL = "api/v1/index.php?action=upload_product_picture"
        const val UPLOAD_PRODUCT_SAVE_POST_URL = "api/v1/index.php?action=save_post"
        const val UPLOAD_PRODUCT_PREMIUM_TRANSACTION_URL = "api/v1/index.php?action=payment_success_saving"
        const val GET_UNREAD_MESSAGE_COUNT_URL = "api/v1/index.php?action=unread_note_chat_count"
        const val GET_TRANSACTION_VENDOR_CRED_URL = "api/v1/index.php?action=payment_api_detail_config"
        const val GET_MEMBERSHIP_PLAN_URL = "api/v1/index.php?action=get_membership_plan"
        const val GET_USER_MEMBERSHIP_PLAN_URL = "api/v1/index.php?action=get_userMembership_by_id"
        const val UPLOAD_PRODUCT_ADDITIONAL_INFO_URL = BASE_URL + "api/v1/?action=getCustomFieldByCatID&catid=%s&subcatid=%s&additionalinfo=%s"
        const val MEMBERSHIP_URL = BASE_URL + "membership/changeplan?username=%s&password=%s&isApp=%s"
        const val FLAG_IMAGE_URL = "https://www.countryflags.io/%s/flat/64.png"
        const val IS_ADMIN_APP = false
        const val IS_APP_CONFIG_RELOAD_REQUIRED = true // UPDATE IT TO FALSE IF YOU DON'T WANT TO RELOAD YOUR CATEGORIES EVERY LAUNCH
        val CURRENT_VERSION = "1.0"
        const val CURRENCY_IN_LEFT= "1"
        const val PREF_FILE = "bylancer_appsgeek"
        const val SUPPORT_EMAIL = "apps.bylancer@gmail.com"
        const val PRODUCT_LOADING_LIMIT = "16"
        const val PRODUCT_LOADING_OFFSET = 8
        const val PRODUCT_STATUS = "active"

        const val ERROR = "error"
        const val SUCCESS = "success"
        const val MESSAGE = "message"
        const val PRODUCT_ID = "product_id"
        const val PRODUCT_NAME = "product_name"
        const val PRODUCT_OWNER_NAME = "product_owner_name"
        const val TERMS_CONDITION_TITLE = "terms_condition_title"
        const val TERMS_CONDITION_URL = "terms_condition_url"
        const val HIDE_PHONE = "1"
        const val HIDE_PHONE_NO = "0"
        const val IS_NEGOTIABLE_NO = "0"
        const val IS_NEGOTIABLE_YES = "1"
        const val YES = "yes"
        const val NO = "no"
        const val IS_ACTIVE = "1"
        const val CHAT_USER_NAME = "chat_user_name"
        const val CHAT_TITLE = "chat_title"
        const val CHAT_USER_IMAGE = "chat_user_image"
        const val CHAT_USER_ID = "chat_user_id"
        const val ASSET_TYPE_TEXT = "text"
        const val EMPTY = ""
        const val ZERO = "0"
        const val CATEGORY = "category"
        const val SUB_CATEGORY = "sub_category"
        const val DATABASE_NAME = "property_app_db"
        const val DATABASE_VERSION = 5
        const val COUNTRY_PAGER_POSITION = 0
        const val STATE_PAGER_POSITION = 1
        const val CITY_PAGER_POSITION = 2

        const val AD_APPROVE = "ad_approve"
        const val AD_DELETE = "ad_delete"
        const val AD_MESSAGE = ""

        const val GO_FOR_PREMIUM_APP = 1
        const val GO_FOR_PREMIUM_AD = 2
        const val PREMIUM_ADS_FREE_COST = 1
        const val PREMIUM_PRIORITY_SUPPORT_COST = 2
        const val PREMIUM_ALL_ADS_PREMIUM_COST = 2

        //COSTING
        const val PREMIUM_ADS_FEATURED_COST = 1
        const val PREMIUM_ADS_URGENT_COST = 1
        const val PREMIUM_ADS_HIGHLIGHTED_COST = 1
        //COSTING ENDS

        const val SELECTED = 1
        const val UNSELECTED = 0

        const val IMAGE_PICKER_FRAGMENT = "picker"
        const val SELECTED_CATEGORY_POSITION = "selected_category_position"
        const val SELECTED_SUB_CATEGORY_ID = "selected_sub_category_id"
        const val SELECTED_CATEGORY_ID = "selected_category_id"
        const val SELECTED_KEYWORD = "selected_keywords"
        const val SELECTED_PRODUCT_LONGITUDE = "selected_product_longitude"
        const val SELECTED_PRODUCT_LATITUDE = "selected_product_latitude"
        const val UPLOAD_PRODUCT_SELECTED_TITLE = "UPLOAD_PRODUCT_SELECTED_TITLE"
        const val ADDITIONAL_INFO_ACTIVITY_TITLE = "ADDITIONAL_INFO_ACTIVITY_TITLE"
        const val DIRECTION_RTL = "rtl"

        const val BANNER_DELAY = 3.0
        const val INTERSTITIAL_DELAY = 3.0
        const val FACEBOOK_INTERSTITIAL_PLACEMENT = "2244587745860156_2351931271792469"

        const val DASHBOARD_PULL_TO_REFRESH_COLOR = 0xFFCA0009
        const val SEARCH_PULL_TO_REFRESH_COLOR = 0xFF313C3F
        const val PULL_TO_REFRESH_COLOR_SCHEME = Color.WHITE

        const val PAYMENT_TYPE_PREMIUM = "premium"
        const val PAYMENT_TYPE_APP_PREMIUM= "subscr"
        const val PAYMENT_TRANSACTION_DETAILS = "package"
        const val PRODUCT_ACTIVE = "active"

        const val PLAN_BUSINESS = "Business"
        const val PLAN_PROFESSIONAL = "Proffesional"
        const val PLAN_BASIC = "Basic"


        /*PayUMoney Constants */
        const val PAY_U_MONEY = "payumoney"
        const val SURL = "https://www.payumoney.com/mobileapp/payumoney/success.php"
        const val FURL = "https://www.payumoney.com/mobileapp/payumoney/failure.php"
        const val PAY_U_MONEY_TEST_ACCOUNT = "test"
        /* PayUMoney Constants Ends */

        /*PayStack*/
        const val PAY_STACK = "paystack"
        const val PAY_STACK_CARD_DETAILS = "pay_stack_card_details"
        const val PAY_STACK_PUBLIC_KEY = "pk_live_d66ae0cbe93f69d213ef9766ea76afa3a54d6dd0"
        /*PayStack Ends*/
        const val PAY_STACK_ACTIVE = true
        const val PAY_U_MONEY_ACTIVE = true
        const val WIRE_TRANSFER_ACTIVE = true
        const val CHECKOUT_2_ACTIVE = true
        const val PAY_STRIPE_ACTIVE = true

        const val PAY_PAL = "paypal"
        const val PAY_PAL_ACTIVE = true

        enum class PREFERENCES private constructor(private val value: String) {
            USER_ID("user_id"),
            DEVICE_ID("deviceId"),
            IS_TOKEN_SENT("is_token_sent"),
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
            SELECTED_COUNTRY_CODE("selected_country_code"),
            SELECTED_STATE_CODE("selected_state_code"),
            SELECTED_CITY_CODE("selected_city_code_id"),
            SELECTED_STATE("selected_state"),
            SELECTED_CITY("selected_city"),
            SELECTED_LANGUAGE("selected_Language"),
            SELECTED_LANGUAGE_DIRECTION("selected_Language_direction"),
            LIVE_LOCATION("live_location"),
            TNC_URL("tnc_url"),
            PROFILE_PIC("profile_pic"),
            LANGUAGE_PACK("language_pack"),
            COUNTRY_LIST("country_list"),
            STATE_LIST("state_list"),
            CITY_LIST("city_list"),
            CONTINUE_BROWSING_TEXT("continue_browsing"),
            CONTINUE_BROWSING_CATEGORY_ID("continue_browsing_category"),
            CONTINUE_BROWSING_IMAGE("continue_browsing_image"),
            SELECTED_LANGUAGE_CODE("selected_language_code_details"),
            CONTINUE_BROWSING_SUB_CATEGORY_ID("continue_browsing_sub_category"),
            APP_CONFIG("app_config_details"),
            APP_VERSION_FROM_SERVER("app_version_server"),
            PREMIUM_APP("premium_app"),
            FEATURED_PRODUCT_PRICE("featured_product_price"),
            URGENT_PRODUCT_PRICE("urgent_product_price"),
            HIGHLIGHTED_PRODUCT_PRICE("highlighted_product_price"),
            IS_FIRST_TIME_LOGIN("is_first_time_login"),
            GOOGLE_BANNER("google_banner"),
            GOOGLE_INTERSTITIAL("google_interstitial"),
            FACEBOOK_INTERSTITIAL("facebook_interstitial"),
            DATABASE_DETAIL("database_detail");

            override fun toString(): String {
                return this.value
            }
        }
    }
}