package com.bylancer.classified.bylancerclassified.activities

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.NoConnectionError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bylancer.classified.bylancerclassified.R
import com.bylancer.classified.bylancerclassified.chat.ChatActivity
import com.bylancer.classified.bylancerclassified.login.LoginRequiredActivity
import com.bylancer.classified.bylancerclassified.login.LoginActivity
import com.bylancer.classified.bylancerclassified.login.ManualLoginActivity
import com.bylancer.classified.bylancerclassified.login.RegisterUserActivity
import com.bylancer.classified.bylancerclassified.splash.SplashActivity
import com.bylancer.classified.bylancerclassified.uploadproduct.categoryselection.UploadCategorySelectionActivity
import com.bylancer.classified.bylancerclassified.utils.*
import com.bylancer.classified.bylancerclassified.widgets.ProgressUtils
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.payumoney.core.PayUmoneyConfig
import com.payumoney.core.PayUmoneyConstants
import com.payumoney.core.PayUmoneySdkInitializer
import com.payumoney.core.entity.TransactionResponse
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager
import com.payumoney.sdkui.ui.utils.ResultModel
import org.json.JSONException
import org.json.JSONObject
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

/**
 * Created by Ani on 3/20/18.
 */
abstract class BylancerBuilderActivity : AppCompatActivity() {
    lateinit var mInterstitialAd: InterstitialAd
    private var mPremiumUpgradeType: Int = AppConstants.GO_FOR_PREMIUM_APP

    override fun onCreate(savedInstanceState: Bundle?) {
        isRTLSupportRequired()
        super.onCreate(savedInstanceState)
        if(this::class.simpleName != LoginRequiredActivity::class.simpleName &&
                this::class.simpleName != LoginActivity::class.simpleName &&
                this::class.simpleName != ChatActivity::class.simpleName &&
                this::class.simpleName != UploadCategorySelectionActivity::class.simpleName) {
            Utility.slideActivityRightToLeft(this)
        } else {
            Utility.slideActivityBottomToTop(this)
        }
        setContentView(setLayoutView())

        setOrientation() // Fixing Android O issue

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = getString(R.string.ad_mob_interstitial_ad_unit)
        loadInterstitialAd()
        addInterstitialAdListener()

        if (SessionState.instance.isGoogleInterstitialSupported
                && this::class.simpleName != LoginRequiredActivity::class.simpleName
                && this::class.simpleName != LoginActivity::class.simpleName
                && this::class.simpleName != RegisterUserActivity::class.simpleName
                && this::class.simpleName != SplashActivity::class.simpleName
                && this::class.simpleName != ManualLoginActivity::class.simpleName) {
            scheduleInterstitialAd()
        }

        initialize(savedInstanceState)
    }

    protected abstract fun setLayoutView(): Int

    protected abstract fun initialize(savedInstanceState: Bundle?)

    fun startActivity(clazz: Class<out Activity>, isNewTask:Boolean) {
        val intent = Intent(this, clazz)
        if(isNewTask) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    fun startActivityWithAffinity(clazz: Class<out Activity>, isNewTask:Boolean) {
        val intent = Intent(this, clazz)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun setOrientation() {
       // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    }

    fun startActivity(clazz: Class<out Activity>, isNewTask:Boolean, bundle: Bundle) {
        val intent = Intent(this, clazz)
        intent.putExtra(AppConstants.BUNDLE, bundle)
        if(isNewTask) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    fun startActivityForResult(clazz: Class<out Activity>, isNewTask:Boolean, bundle: Bundle, activityStartCode: Int) {
        val intent = Intent(this, clazz)
        intent.putExtra(AppConstants.BUNDLE, bundle)
        if(isNewTask) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivityForResult(intent, activityStartCode)
    }

    fun startActivity(clazz: Class<out Activity>, extra:Bundle, parcelName:String) {
        val intent = Intent(this, clazz)
        intent.putExtra(AppConstants.BUNDLE, extra)
        startActivity(intent)
    }

    override fun onBackPressed() {
        try {
            super.onBackPressed()
            if(this::class.simpleName != LoginRequiredActivity::class.simpleName &&
                    this::class.simpleName != LoginActivity::class.simpleName &&
                    this::class.simpleName != ChatActivity::class.simpleName &&
                    this::class.simpleName != UploadCategorySelectionActivity::class.simpleName) {
                Utility.slideActivityLeftToRight(this)
            } else {
                Utility.slideActivityTopToBottom(this)
            }
        } catch (nullPointerException: NullPointerException) {

        } finally {
            finish()
        }
    }

    private fun loadInterstitialAd() {
        mInterstitialAd?.loadAd(AdRequest.Builder().build())
    }

    private fun addInterstitialAdListener() {
        mInterstitialAd?.adListener  = object: AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                loadInterstitialAd()
            }

            override fun onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() {
                loadInterstitialAd()
            }
        }
    }

    private fun scheduleInterstitialAd() {
        val timer = Timer()
        val interstitialTask = object : TimerTask() {
            override fun run() {
                this@BylancerBuilderActivity.runOnUiThread() {
                    if (mInterstitialAd?.isLoaded && !this@BylancerBuilderActivity.isFinishing) {
                        mInterstitialAd?.show()
                    }
                }
            }
        }
        val delay = (1000 * 60 * AppConstants.INTERSTITIAL_DELAY)
        timer.schedule(interstitialTask, 0L, delay.toLong())
    }

    private fun isRTLSupportRequired() {
        if (AppConstants.DIRECTION_RTL.equals(SessionState.instance.selectedLanguageDirection)) {
            window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
        } else {
            window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
        }
    }

    /**
     * Pay U Money Payment
     */
    fun launchPaymentFlow(title: String, amount: String, upgradeType: Int) {
        this.mPremiumUpgradeType = upgradeType
        val payUmoneyConfig = PayUmoneyConfig.getInstance()
        payUmoneyConfig.payUmoneyActivityTitle = if (SessionState.instance.appName.isNullOrEmpty()) getString(R.string.app_name) else SessionState.instance.appName
        payUmoneyConfig.doneButtonText = "Pay " + getString(R.string.rupees) + amount
        if (SessionState.instance.phoneNumber.isNullOrEmpty()) {
            SessionState.instance.phoneNumber = "9999999999"
        }

        val builder = PayUmoneySdkInitializer.PaymentParam.Builder()
        builder.setAmount(amount)
                .setTxnId(System.currentTimeMillis().toString() + "")
                .setPhone(SessionState.instance.phoneNumber)
                .setProductName(title)
                .setFirstName(SessionState.instance.userName)
                .setEmail(SessionState.instance.email)
                .setsUrl(AppConstants.SURL)
                .setfUrl(AppConstants.FURL)
                .setUdf1("Aa")
                .setUdf2("bb")
                .setUdf3("cc")
                .setUdf4("dd")
                .setUdf5("ee")
                .setIsDebug(AppConstants.DEBUG)
                .setKey(AppConstants.MERCHANT_KEY)
                .setMerchantId(AppConstants.MERCHANT_ID)

        try {
            var mPaymentParams = builder.build()
            calculateHashInServer(mPaymentParams)
        } catch (e: Exception) {
            showToast(e.message)
        }
    }

    private fun calculateHashInServer(mPaymentParams: PayUmoneySdkInitializer.PaymentParam) {
        ProgressUtils.showLoadingDialog(this)
        val url = AppConstants.BASE_URL + AppConstants.PAY_U_HASH_URL
        val request = object : StringRequest(Request.Method.POST, url,

                Response.Listener { response ->
                    var merchantHash = ""

                    try {
                        val jsonObject = JSONObject(response)
                        merchantHash = jsonObject.getString("payment_hash")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    ProgressUtils.cancelLoading()

                    if (merchantHash.isEmpty() || merchantHash == "") {
                        Toast.makeText(this, "Could not generate hash", Toast.LENGTH_SHORT).show()
                    } else {
                        mPaymentParams.setMerchantHash(merchantHash)
                        //PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, this, R.style.PayUMoney, true)
                        if (PayUAppPreferences.selectedTheme != -1) {
                            PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, this, PayUAppPreferences.selectedTheme, PayUAppPreferences.isOverrideResultScreen)
                        } else {
                            PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, this, R.style.PayUMoney, PayUAppPreferences.isOverrideResultScreen)
                        }
                    }
                },

                Response.ErrorListener { error ->
                    if (error is NoConnectionError) {
                        Toast.makeText(this, "Connect to internet Volley", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    }
                    ProgressUtils.cancelLoading()
                }) {
            override fun getParams(): Map<String, String> {
                return mPaymentParams.params
            }
        }
        request.setShouldCache(false)
        Volley.newRequestQueue(this).add(request)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == Activity.RESULT_OK && data != null) {

            val transactionResponse = data.getParcelableExtra<TransactionResponse>(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE)
            val resultModel = data.getParcelableExtra<ResultModel>(PayUmoneyFlowManager.ARG_RESULT)

            if (transactionResponse?.getPayuResponse() != null) {
                when {
                    transactionResponse.transactionStatus == TransactionResponse.TransactionStatus.SUCCESSFUL -> showAlert("Payment Successful")
                    transactionResponse.transactionStatus == TransactionResponse.TransactionStatus.CANCELLED -> showAlert("Payment Cancelled")
                    transactionResponse.transactionStatus == TransactionResponse.TransactionStatus.FAILED -> showAlert("Payment Failed")
                }

            } else if (resultModel != null && resultModel.error != null) {
                Toast.makeText(this, "Error check log", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Both objects are null", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == Activity.RESULT_CANCELED) {
            showAlert("Payment Cancelled")
        }
    }
    /********************PayUMoney payment ends here ******************/

    private fun convertStringToDouble(str: String) = str.toDouble()

    private fun showAlert(msg: String) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setMessage(msg)
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })
        alertDialog.show()
    }
}

