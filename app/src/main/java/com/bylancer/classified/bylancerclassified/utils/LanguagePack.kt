package com.bylancer.classified.bylancerclassified.utils

import android.content.Context
import com.bylancer.classified.bylancerclassified.webservices.languagepack.LanguagePackModel
import com.google.gson.Gson
import org.json.JSONArray
import com.google.gson.reflect.TypeToken



class LanguagePack private constructor() {
    var languagePackData:List<LanguagePackModel>? = null
    var currentLanguagePackData:LanguagePackModel? = null

    companion object {
        var languagePack:LanguagePack? = null

        val instance: LanguagePack
        get() {
            if(languagePack == null) {
                languagePack = LanguagePack()
            }
            return languagePack!!
        }

        fun getString(key: String): String {
            if (LanguagePack.instance.languagePackData != null) {
                if (LanguagePack.instance.currentLanguagePackData == null) {
                    for (languagePack in LanguagePack.instance.languagePackData!!) {
                        if (SessionState.instance.selectedLanguage.equals(languagePack.language)) {
                            LanguagePack.instance.currentLanguagePackData = languagePack
                            break
                        }
                    }
                }

                if (LanguagePack.instance.currentLanguagePackData != null
                        && SessionState.instance.selectedLanguage.equals(LanguagePack.instance.currentLanguagePackData?.language)
                        && LanguagePack.instance.currentLanguagePackData?.text!!.containsKey(key)) {
                    return  LanguagePack.instance.currentLanguagePackData!!.text!!.get(key)!!
                } else if(LanguagePack.instance.currentLanguagePackData == null) {
                    return updateLanguagePack(key, languagePack)
                } else if (!SessionState.instance.selectedLanguage.equals(LanguagePack.instance.currentLanguagePackData?.language)) {
                    return updateLanguagePack(key, languagePack)
                }
            }

            return key
        }

        private fun updateLanguagePack(key: String, languagePack: LanguagePack?): String {
            for (languagePack in LanguagePack.instance.languagePackData!!) {
                if (SessionState.instance.selectedLanguage.equals(languagePack.language)) {
                    LanguagePack.instance.currentLanguagePackData = languagePack
                    if (languagePack.text!!.containsKey(key)) {
                        return languagePack.text!!.get(key)!!
                    }
                }
            }

            return key
        }
    }

    fun saveLanguageData(context: Context, languagePackData:String) {
        SessionState.instance.saveValuesToPreferences(context, AppConstants.Companion.PREFERENCES.LANGUAGE_PACK.toString(),
                languagePackData)
    }

    fun setLanguageData(languagePackDataStr: String) {
        if (!languagePackDataStr.isNullOrEmpty())
            languagePackData = Gson().fromJson(languagePackDataStr, object : TypeToken<List<LanguagePackModel>>() {}.type)
    }
}