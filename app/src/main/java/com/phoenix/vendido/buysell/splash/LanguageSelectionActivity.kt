package com.phoenix.vendido.buysell.splash

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.phoenix.vendido.buysell.R
import com.phoenix.vendido.buysell.activities.BylancerBuilderActivity
import com.phoenix.vendido.buysell.appconfig.AppConfigDetail
import com.phoenix.vendido.buysell.appconfig.AppConfigModel
import com.phoenix.vendido.buysell.dashboard.DashboardActivity
import com.phoenix.vendido.buysell.utils.AppConstants
import com.phoenix.vendido.buysell.utils.LanguagePack
import com.phoenix.vendido.buysell.utils.SessionState
import com.phoenix.vendido.buysell.webservices.RetrofitController
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_language_selection.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LanguageSelectionActivity : BylancerBuilderActivity(), LanguageSelection {

    override fun setLayoutView() = R.layout.activity_language_selection

    override fun initialize(savedInstanceState: Bundle?) {
        language_selection_app_name_text_view.text = if (SessionState.instance.appName != null) SessionState.instance.appName else getString(R.string.app_name)
        language_selection_sub_title_text_view.text = LanguagePack.getString(getString(R.string.choose_language))

        language_list_recycler_view.layoutManager = LinearLayoutManager(this)
        language_list_recycler_view.setHasFixedSize(false)
        language_list_recycler_view.layoutAnimation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation)
        if (!LanguagePack.instance.languagePackData.isNullOrEmpty()) {
            language_list_recycler_view.adapter = LanguageSelectionAdapter(LanguagePack.instance.languagePackData!!, this@LanguageSelectionActivity)
        }
    }

    override fun onLanguageSelected(languageString: String?, languageCode : String?, languageDirection : String?) {
        SessionState.instance.selectedLanguage = languageString ?: ""
        SessionState.instance.selectedLanguageCode = languageCode ?: ""
        SessionState.instance.selectedLanguageDirection = languageDirection ?: ""
        SessionState.instance.saveValuesToPreferences(this@LanguageSelectionActivity, AppConstants.Companion.PREFERENCES.SELECTED_LANGUAGE.toString(),
                SessionState.instance.selectedLanguage)
        SessionState.instance.saveValuesToPreferences(this@LanguageSelectionActivity, AppConstants.Companion.PREFERENCES.SELECTED_LANGUAGE_CODE.toString(),
                SessionState.instance.selectedLanguageCode)
        SessionState.instance.saveValuesToPreferences(this@LanguageSelectionActivity, AppConstants.Companion.PREFERENCES.SELECTED_LANGUAGE_DIRECTION.toString(),
                SessionState.instance.selectedLanguageDirection)
        refreshCategoriesWithLanguageCode(SessionState.instance.selectedLanguageCode)
    }

    private fun refreshCategoriesWithLanguageCode(languageCode: String) {
        showProgressDialog(getString(R.string.loading))
        RetrofitController.fetchAppConfig(languageCode, object : Callback<AppConfigModel> {
            override fun onFailure(call: Call<AppConfigModel>?, t: Throwable?) {
                if (!this@LanguageSelectionActivity.isFinishing) {
                    dismissProgressDialog()
                }
            }

            override fun onResponse(call: Call<AppConfigModel>?, response: Response<AppConfigModel>?) {
                if (!this@LanguageSelectionActivity.isFinishing && response != null && response.isSuccessful && response.body() != null) {
                    val appConfigUrl: AppConfigModel = response.body()!!
                    AppConfigDetail.saveAppConfigData(this@LanguageSelectionActivity, Gson().toJson(appConfigUrl))
                    AppConfigDetail.initialize(Gson().toJson(appConfigUrl))
                    startActivity(DashboardActivity :: class.java, false)
                }
                dismissProgressDialog()
            }

        })
    }
}



