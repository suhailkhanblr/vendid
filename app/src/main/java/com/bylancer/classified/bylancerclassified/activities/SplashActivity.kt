package com.bylancer.classified.bylancerclassified.activities

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.view.View
import com.bylancer.classified.bylancerclassified.R
import com.bylancer.classified.bylancerclassified.appconfig.AppConfigDetail
import com.bylancer.classified.bylancerclassified.appconfig.AppConfigModel
import com.bylancer.classified.bylancerclassified.dashboard.DashboardActivity
import com.bylancer.classified.bylancerclassified.utils.LanguagePack
import com.bylancer.classified.bylancerclassified.utils.SessionState
import com.bylancer.classified.bylancerclassified.utils.Utility
import com.bylancer.classified.bylancerclassified.webservices.RetrofitController
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : BylancerBuilderActivity() {
    private val SLEEP_TIME : Long = 3000L

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
        SessionState.instance.readValuesFromPreferences(this)
        startActivity(DashboardActivity :: class.java, true)
        finish()
    }

    /**
     * Getting App Config
     */
    private fun getAppConfig() {
        if (Utility.isNetworkAvailable(this@SplashActivity)) {
            if (AppConfigDetail.category.isNullOrEmpty()) {
                RetrofitController.fetchAppConfig(object: Callback<AppConfigModel> {
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
                        if (response != null && response.isSuccessful) {
                            val appConfigUrl: AppConfigModel = response.body()
                            AppConfigDetail.saveAppConfigData(this@SplashActivity, Gson().toJson(appConfigUrl))
                            AppConfigDetail.initialize(Gson().toJson(appConfigUrl))

                            saveAndLaunchScreen()
                        }
                    }

                })
            } else {
                saveAndLaunchScreen()
            }
        } else {
            showLogoutAlertDialog()
        }
    }

    private fun showLogoutAlertDialog() {
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
