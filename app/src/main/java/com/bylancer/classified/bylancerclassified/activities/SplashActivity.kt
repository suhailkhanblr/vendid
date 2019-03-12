package com.bylancer.classified.bylancerclassified.activities

import android.os.Bundle
import android.os.Handler
import com.bylancer.classified.bylancerclassified.R
import com.bylancer.classified.bylancerclassified.dashboard.DashboardActivity
import com.bylancer.classified.bylancerclassified.utils.SessionState

class SplashActivity : BylancerBuilderActivity() {
    private val SLEEP_TIME : Long = 3000L

    override fun setLayoutView(): Int {
        return R.layout.activity_splash
    }

    override fun initialize(savedInstanceState: Bundle?) {
        delayTimeForSplash()
    }

    private fun delayTimeForSplash() {
        Handler().postDelayed({
            SessionState.instance.readValuesFromPreferences(this)
            startActivity(DashboardActivity :: class.java, true)
            finish()
        }, SLEEP_TIME);
    }
}
