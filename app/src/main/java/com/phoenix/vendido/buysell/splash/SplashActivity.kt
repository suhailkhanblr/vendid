package com.phoenix.vendido.buysell.splash

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import com.phoenix.vendido.buysell.R
import com.phoenix.vendido.buysell.activities.BylancerBuilderActivity
import com.phoenix.vendido.buysell.appconfig.AppConfigDetail
import com.phoenix.vendido.buysell.appconfig.AppConfigModel
import com.phoenix.vendido.buysell.dashboard.DashboardActivity
import com.phoenix.vendido.buysell.utils.*
import com.phoenix.vendido.buysell.webservices.RetrofitController
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : BylancerBuilderActivity() {
    override fun setLayoutView(): Int {
        return R.layout.activity_splash
    }

    override fun initialize(savedInstanceState: Bundle?) {
        SessionState.instance.readValuesFromPreferences(this)
        getAppConfig()
    }

    private fun delayTimeForSplash() {
        Handler().postDelayed({
            saveAndLaunchScreen()
        }, SLEEP_TIME);
    }

    private fun saveAndLaunchScreen() {
        if (LanguagePack.instance.languagePackData.isNullOrEmpty()
                || !SessionState.instance.appVersionFromServer.equals(AppConfigDetail.appVersionFromServer, false)) {
            if (!AppConfigDetail.appVersionFromServer.isNullOrEmpty()) {
                SessionState.instance.saveValuesToPreferences(this, AppConstants.Companion.PREFERENCES.APP_VERSION_FROM_SERVER.toString(), AppConfigDetail.appVersionFromServer)
            }
            fetchLanguagePackDetails()
        } else {
            navigateToNextScreen()
        }
    }

    /**
     * Getting App Config
     */
    private fun getAppConfig() {
        if (Utility.isNetworkAvailable(this@SplashActivity)) {
            if (AppConfigDetail.category.isNullOrEmpty()) {
                RetrofitController.fetchAppConfig(getSelectedCountryCode(), object : Callback<AppConfigModel> {
                    override fun onFailure(call: Call<AppConfigModel>?, t: Throwable?) {
                        if (SessionState.instance.appName.isNullOrEmpty()) {
                            if (Utility.isNetworkAvailable(this@SplashActivity)) {
                                getAppConfig()
                            } else {
                                showLogoutAlertDialog()
                            }
                        } else {
                            saveAndLaunchScreen()
                        }
                    }

                    override fun onResponse(call: Call<AppConfigModel>?, response: Response<AppConfigModel>?) {
                        if (response != null && response.isSuccessful && response.body() != null) {
                            val appConfigUrl: AppConfigModel = response.body()!!
                            AppConfigDetail.saveAppConfigData(this@SplashActivity, Gson().toJson(appConfigUrl))
                            AppConfigDetail.initialize(Gson().toJson(appConfigUrl))
                            saveAndLaunchScreen()
                        }
                    }

                })
            } else {
                if (AppConstants.IS_APP_CONFIG_RELOAD_REQUIRED && Utility.isNetworkAvailable(this@SplashActivity)) {
                    AppConfigDetail.category = null
                    getAppConfig()
                } else {
                    delayTimeForSplash()
                }
            }
        } else {
            showLogoutAlertDialog()
        }
    }

    private fun getSelectedCountryCode(): String {
        return if (SessionState.instance.selectedLanguageCode.isNullOrEmpty()) "" else SessionState.instance.selectedLanguageCode
    }

    private fun showLogoutAlertDialog() {
        if (!this.isFinishing) {
            val builder = AlertDialog.Builder(this)
            builder.setCancelable(false)
            builder.setTitle("")
            builder.setMessage(LanguagePack.getString("Internet is required to configure your application"))
            builder.setPositiveButton(LanguagePack.getString("OK")){dialog, which ->
                dialog.dismiss()
                finish()
            }
            builder.setNegativeButton(LanguagePack.getString("")){dialog, which ->

            }
            builder.create().show()
        }
    }

    private fun fetchLanguagePackDetails() {
        //var mRequestQueue = Volley.newRequestQueue(this)
        val response = getJsonDataFromAsset(this)
        //String Request initialized
       // var mStringRequest = JsonArrayRequest(Request.Method.GET, AppConstants.BASE_URL + AppConstants.FETCH_LANGUAGE_PACK_URL, null, com.android.volley.Response.Listener<JSONArray> { response ->
            if (response != null) {
                LanguagePack.instance.saveLanguageData(this@SplashActivity, response.toString())
                LanguagePack.instance.setLanguageData(response.toString())
                navigateToNextScreen()
            } else {
                navigateToNextScreen()
            }
        /*}, com.android.volley.Response.ErrorListener {
            navigateToNextScreen()
            *//*if (Utility.isNetworkAvailable(this@SplashActivity)) {
                fetchLanguagePackDetails()
            } else {
                Utility.showSnackBar(activity_login_user_parent_layout, getString(R.string.internet_issue), this)
            }*//*

        })

        mRequestQueue.add(mStringRequest)*/
    }

    private fun navigateToNextScreen() {
        SessionState.instance.readValuesFromPreferences(this)
        if (SessionState.instance.isLoginFirstTime) {
            SessionState.instance.isLoginFirstTime = false
            SessionState.instance.saveBooleanToPreferences(this@SplashActivity,
                    AppConstants.Companion.PREFERENCES.IS_FIRST_TIME_LOGIN.toString() ,false)
            startActivity(LanguageSelectionActivity :: class.java, false)
        } else {
            startActivity(DashboardActivity :: class.java, false)
        }
        finish()
    }

    companion object {
        const val SLEEP_TIME : Long = 3000L
    }
}
