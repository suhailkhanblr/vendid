package com.bylancer.classified.bylancerclassified.appconfig

import android.content.Context
import com.bylancer.classified.bylancerclassified.utils.AppConstants
import com.bylancer.classified.bylancerclassified.utils.SessionState
import com.bylancer.classified.bylancerclassified.webservices.settings.CityListModel
import com.bylancer.classified.bylancerclassified.webservices.settings.CountryListModel
import com.bylancer.classified.bylancerclassified.webservices.settings.StateListModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import java.io.Reader
import java.io.StringReader

class AppConfigDetail {
    companion object {
        var languageList: List<Language>? = null
        var category:List<Category>? = null
        var countryList:List<CountryListModel>? = null
        var stateList:List<StateListModel>? = null
        var cityList:List<CityListModel>? = null
        var appVersionFromServer = ""

        fun initialize(appConfigStr: String) {
            if (!appConfigStr.isNullOrEmpty()) {
                var appConfig = Gson().fromJson(appConfigStr, AppConfigModel::class.java)
                setCategoryDetail(appConfig.categories)
                setLanguageDetail(appConfig.languages)
                SessionState.instance.termsConditionUrl = if (appConfig.termsPageLink != null) appConfig.termsPageLink!! else ""
                SessionState.instance.appName = if (appConfig.appName != null) appConfig.appName!! else ""
                SessionState.instance.privacyPolicyUrl = if (appConfig.policyPageLink != null) appConfig.policyPageLink!! else ""
                SessionState.instance.detectLiveLocation = if (appConfig.detectLiveLocation != null) appConfig.detectLiveLocation!! else ""
                SessionState.instance.defaultCountry = if (appConfig.defaultCountry != null) appConfig.defaultCountry!! else ""
                SessionState.instance.isGoogleInterstitialSupported = if (appConfig.googleInterstitial != null) appConfig.googleInterstitial!! else true
                SessionState.instance.isUserHasPremiumApp = if (appConfig.premiumApp != null) appConfig.premiumApp!! else true
                SessionState.instance.isGoogleBannerSupported = if (appConfig.googleBanner != null) appConfig.googleBanner!! else true
                SessionState.instance.isFacebookInterstitialSupported = if (appConfig.facebookInterstitial != null) appConfig.facebookInterstitial!! else true
                SessionState.instance.paymentCurrencyCode = if (appConfig.currencyCode != null) appConfig.currencyCode!! else "INR"
                SessionState.instance.paymentCurrencySign = if (appConfig.currencySign != null) appConfig.currencySign!! else "₹"
                SessionState.instance.featuredProductFee = if (appConfig.featuredFee != null) appConfig.featuredFee!! else AppConstants.PREMIUM_ADS_FEATURED_COST.toString()
                SessionState.instance.urgentProductFee = if (appConfig.urgentFee != null) appConfig.urgentFee!! else AppConstants.PREMIUM_ADS_URGENT_COST.toString()
                SessionState.instance.highlightProductFee = if (appConfig.highlightFee != null) appConfig.highlightFee!! else AppConstants.PREMIUM_ADS_HIGHLIGHTED_COST.toString()

                SessionState.instance.is2CheckOutActive = if (appConfig.paymentMethod?.is2CheckoutActive() != null) appConfig.paymentMethod?.is2CheckoutActive()!!  else AppConstants.CHECKOUT_2_ACTIVE
                SessionState.instance.isPayPalActive = if (appConfig.paymentMethod?.isPayPalActive() != null) appConfig.paymentMethod?.isPayPalActive()!!  else AppConstants.PAY_PAL_ACTIVE
                SessionState.instance.isPayUMoneyActive = if (appConfig.paymentMethod?.isPayUMoneyActive() != null) appConfig.paymentMethod?.isPayUMoneyActive()!!  else AppConstants.PAY_U_MONEY_ACTIVE
                SessionState.instance.isPayStackActive = if (appConfig.paymentMethod?.isPayStackActive() != null) appConfig.paymentMethod?.isPayStackActive()!!  else AppConstants.PAY_STACK_ACTIVE
                SessionState.instance.isWireTransferActive = if (appConfig.paymentMethod?.isWireTransferActive() != null) appConfig.paymentMethod?.isWireTransferActive()!!  else AppConstants.WIRE_TRANSFER_ACTIVE
                SessionState.instance.isStripeActive = if (appConfig.paymentMethod?.isStripeActive() != null) appConfig.paymentMethod?.isStripeActive()!!  else AppConstants.PAY_STRIPE_ACTIVE

                appVersionFromServer = if (appConfig.appVersion != null) appConfig.appVersion!! else ""
                if (SessionState.instance.selectedLanguage.isNullOrEmpty() && !appConfig.defaultLang.isNullOrEmpty()) {
                    SessionState.instance.selectedLanguage = appConfig.defaultLang!!
                }

                if (SessionState.instance.selectedLanguageCode.isNullOrEmpty() && !appConfig.defaultLangCode.isNullOrEmpty()) {
                    SessionState.instance.selectedLanguageCode = appConfig.defaultLangCode!!
                }
            }
        }

        private fun setLanguageDetail(languageList: List<Language>?) {
            this.languageList = languageList
        }

        private fun setCategoryDetail(categoryList: List<Category>?) {
            this.category = categoryList
        }

        fun saveAppConfigData(context: Context, appConfigData:String) {
            SessionState.instance.saveValuesToPreferences(context, AppConstants.Companion.PREFERENCES.APP_CONFIG.toString(),
                    appConfigData)
        }

        fun saveCountryListData(context: Context, countryListData:String) {
            SessionState.instance.saveValuesToPreferences(context, AppConstants.Companion.PREFERENCES.COUNTRY_LIST.toString(),
                    countryListData)
        }

        fun saveSateListData(context: Context, countryListData:String) {
            SessionState.instance.saveValuesToPreferences(context, AppConstants.Companion.PREFERENCES.STATE_LIST.toString(),
                    countryListData)
        }

        fun saveCityListData(context: Context, countryListData:String) {
            SessionState.instance.saveValuesToPreferences(context, AppConstants.Companion.PREFERENCES.CITY_LIST.toString(),
                    countryListData)
        }

        fun loadLocationDetail(context: Context?) {
            if (context != null) {
                val prefs = context.getSharedPreferences(AppConstants.PREF_FILE, Context.MODE_PRIVATE)
                val countryListDataStr =  prefs.getString(AppConstants.Companion.PREFERENCES.COUNTRY_LIST.toString(), "")
                if (!countryListDataStr.isNullOrEmpty()) {
                    val reader = JsonReader(StringReader(countryListDataStr) as Reader?)
                    reader.isLenient = true
                    countryList = Gson().fromJson(reader, object : TypeToken<List<CountryListModel>>() {}.type)
                }

                val stateListDataStr =  prefs.getString(AppConstants.Companion.PREFERENCES.STATE_LIST.toString(), "")
                if (!stateListDataStr.isNullOrEmpty()) {
                    val reader = JsonReader(StringReader(stateListDataStr) as Reader?)
                    reader.isLenient = true
                    stateList = Gson().fromJson(reader, object : TypeToken<List<StateListModel>>() {}.type)
                }

                val cityListDataStr =  prefs.getString(AppConstants.Companion.PREFERENCES.CITY_LIST.toString(), "")
                if (!cityListDataStr.isNullOrEmpty()) {
                    val reader = JsonReader(StringReader(cityListDataStr) as Reader?)
                    reader.isLenient = true
                    cityList = Gson().fromJson(reader, object : TypeToken<List<CityListModel>>() {}.type)
                }
            }

        }
    }

}