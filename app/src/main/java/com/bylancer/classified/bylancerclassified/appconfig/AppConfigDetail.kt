package com.bylancer.classified.bylancerclassified.appconfig

import android.content.Context
import com.bylancer.classified.bylancerclassified.utils.AppConstants
import com.bylancer.classified.bylancerclassified.utils.SessionState
import com.google.gson.Gson

class AppConfigDetail {
    companion object {
        var languageList: List<Language>? = null
        var category:List<Category>? = null
        var countryList:List<Category>? = null

        fun initialize(appConfigStr: String) {
            if (!appConfigStr.isNullOrEmpty()) {
                var appConfig = Gson().fromJson(appConfigStr, AppConfigModel::class.java)
                setCategoryDetail(appConfig.categories)
                setLanguageDetail(appConfig.languages)
                SessionState.instance.termsConditionUrl = appConfig.termsPageLink!!
                SessionState.instance.appName = appConfig.appName!!
                SessionState.instance.privacyPolicyUrl = appConfig.policyPageLink!!
                SessionState.instance.detectLiveLocation = appConfig.detectLiveLocation!!
                SessionState.instance.defaultCountry = appConfig.defaultCountry!!
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
    }


}