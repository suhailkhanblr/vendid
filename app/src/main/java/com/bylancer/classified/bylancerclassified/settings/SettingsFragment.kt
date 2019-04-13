package com.bylancer.classified.bylancerclassified.settings


import android.os.Bundle
import android.app.Fragment
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.view.View
import android.view.ViewTreeObserver
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bylancer.classified.bylancerclassified.R
import com.bylancer.classified.bylancerclassified.appconfig.AppConfigDetail
import com.bylancer.classified.bylancerclassified.fragments.BylancerBuilderFragment
import com.bylancer.classified.bylancerclassified.login.LoginActivity
import com.bylancer.classified.bylancerclassified.login.LoginRequiredActivity
import com.bylancer.classified.bylancerclassified.login.TermsAndConditionWebView
import com.bylancer.classified.bylancerclassified.utils.AppConstants
import com.bylancer.classified.bylancerclassified.utils.LanguagePack
import com.bylancer.classified.bylancerclassified.utils.SessionState
import com.bylancer.classified.bylancerclassified.utils.Utility
import com.bylancer.classified.bylancerclassified.webservices.RetrofitController
import com.bylancer.classified.bylancerclassified.webservices.settings.CityListModel
import com.bylancer.classified.bylancerclassified.webservices.settings.CountryListModel
import com.bylancer.classified.bylancerclassified.webservices.settings.StateListModel
import com.gmail.samehadar.iosdialog.IOSDialog
import kotlinx.android.synthetic.main.fragment_settings.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A settings [Fragment] class.
 *
 */
class SettingsFragment : BylancerBuilderFragment(), View.OnClickListener {
    var mProgressDialog: IOSDialog? = null
    val countryList = arrayListOf<CountryListModel>()
    val stateList = arrayListOf<StateListModel>()
    val cityList = arrayListOf<CityListModel>()
    var isKeyboardOpen: Boolean = false

    override fun setLayoutView() = R.layout.fragment_settings

    override fun initialize(savedInstanceState: Bundle?) {
        initializeTextViewsWithLanguagePack()

        settings_login_sign_up_frame.setOnClickListener(this)
        settings_terms_condition_frame.setOnClickListener(this)
        settings_my_fav_frame.setOnClickListener(this)
        settings_my_ads_frame.setOnClickListener(this)
        settings_support_frame.setOnClickListener(this)
        if (SessionState.instance.isLoggedIn) {
            settings_login_sign_up_icon.setImageResource(R.drawable.ic_settings_logout)
            settings_login_sign_up_text.text = LanguagePack.getString(getString(R.string.log_out))
        } else {
            settings_login_sign_up_text.text = LanguagePack.getString(getString(R.string.login_sign_up))
        }

        settings_country_spinner.setHintTextColor(resources.getColor(R.color.light_gray)) //change title of spinner-dialog programmatically
        if (!AppConstants.EMPTY.equals(SessionState.instance.selectedCountry)) {
            settings_country_spinner.setText(SessionState.instance.selectedCountry)
        }
        settings_country_spinner.setExpandTint(R.color.transparent)
        settings_country_spinner.setOnItemClickListener{ position ->
            if (countryList.get(position) != null) {
                SessionState.instance.selectedCountry = countryList.get(position).name!!
                SessionState.instance.saveValuesToPreferences(context!!, AppConstants.Companion.PREFERENCES.SELECTED_COUNTRY.toString(),
                        countryList.get(position).name!!)
                SessionState.instance.selectedState = ""
                SessionState.instance.selectedCity = ""
                settings_state_spinner.setText("")
                settings_city_spinner.setText("")
                SessionState.instance.saveValuesToPreferences(context!!, AppConstants.Companion.PREFERENCES.SELECTED_STATE.toString(),
                        "")
                SessionState.instance.saveValuesToPreferences(context!!, AppConstants.Companion.PREFERENCES.SELECTED_CITY.toString(),
                        "")
                fetchStateList(countryList.get(position).lowercaseCode!!)
            }
        }

        if (!AppConstants.EMPTY.equals(SessionState.instance.selectedState)) {
            settings_state_spinner.setText(SessionState.instance.selectedState)
        }

        settings_state_spinner.setOnItemClickListener{ position ->
            SessionState.instance.selectedState = if (stateList.get(position) != null) stateList.get(position).name!! else ""
            SessionState.instance.saveValuesToPreferences(context!!, AppConstants.Companion.PREFERENCES.SELECTED_STATE.toString(),
                    if (stateList.get(position) != null) stateList.get(position).name!! else "")
            settings_city_spinner.setText("")
            SessionState.instance.selectedCity = ""
            SessionState.instance.saveValuesToPreferences(context!!, AppConstants.Companion.PREFERENCES.SELECTED_CITY.toString(),
                    "")
            fetchCityList(stateList.get(position).code!!)
        }

        if (!AppConstants.EMPTY.equals(SessionState.instance.selectedCity)) {
            settings_city_spinner.setText(SessionState.instance.selectedCity)
        }

        settings_city_spinner.setOnItemClickListener{ position ->
            SessionState.instance.selectedCity = if (cityList.get(position) != null) cityList.get(position).name!! else ""
            SessionState.instance.saveValuesToPreferences(context!!, AppConstants.Companion.PREFERENCES.SELECTED_CITY.toString(),
                    if (cityList.get(position) != null) cityList.get(position).name!! else "")
        }

        if (LanguagePack.instance.languagePackData == null) {
            fetchLanguagePackDetails()
        } else {
            initializeLanguagePack()
        }
    }

    private fun fetchLanguagePackDetails() {
        var mRequestQueue = Volley.newRequestQueue(context)

        //String Request initialized
        var mStringRequest = StringRequest(Request.Method.GET, AppConstants.BASE_URL + AppConstants.FETCH_LANGUAGE_PACK_URL, com.android.volley.Response.Listener<String> { response ->
            LanguagePack.instance.saveLanguageData(context!!, response)
            LanguagePack.instance.setLanguageData(response)
            initializeLanguagePack()
        }, com.android.volley.Response.ErrorListener {
            initializeLanguagePack()
        })

        mRequestQueue.add(mStringRequest)
    }

    private fun initializeTextViewsWithLanguagePack() {
        appCompatTextView.text = LanguagePack.getString(getString(R.string.my_account))
        settings_my_ads_text.text = LanguagePack.getString(getString(R.string.my_ads))
        settings_my_fav_text.text = LanguagePack.getString(getString(R.string.my_favorites))
        settings_my_saved_search_text.text = LanguagePack.getString(getString(R.string.my_saved_search))
        settings_caption_text_view.text = LanguagePack.getString(getString(R.string.settings))
        settings_country_text.text = LanguagePack.getString(getString(R.string.country))
        settings_state_text.text = LanguagePack.getString(getString(R.string.state))
        settings_city_text.text = LanguagePack.getString(getString(R.string.city))
        settings_language_text.text = LanguagePack.getString(getString(R.string.language))
        settings_support_text.text = LanguagePack.getString(getString(R.string.support))
        settings_terms_condition_text.text = LanguagePack.getString(getString(R.string.terms_condition))
        settings_language_spinner.hint = LanguagePack.getString(getString(R.string.select_language))
        settings_country_spinner.hint = LanguagePack.getString(getString(R.string.select_country))
        settings_state_spinner.hint = LanguagePack.getString(getString(R.string.select_state))
        settings_city_spinner.hint = LanguagePack.getString(getString(R.string.select_city))
        appCompatTextView.text = LanguagePack.getString(getString(R.string.my_account))
        if (SessionState.instance.isLoggedIn) {
            settings_login_sign_up_text.text = LanguagePack.getString(getString(R.string.log_out))
        } else {
            settings_login_sign_up_text.text = LanguagePack.getString(getString(R.string.login_sign_up))
        }
    }

    private fun initializeLanguagePack() {
        settings_language_spinner.setHint(LanguagePack.getString(getString(R.string.select_language)))
        settings_language_spinner.setOnItemClickListener{ position ->
            if(AppConfigDetail.languageList != null) {
                SessionState.instance.selectedLanguage = AppConfigDetail.languageList?.get(position)?.name!!
                SessionState.instance.saveValuesToPreferences(context!!, AppConstants.Companion.PREFERENCES.SELECTED_LANGUAGE.toString(),
                        AppConfigDetail.languageList?.get(position)?.name!!)
                initializeTextViewsWithLanguagePack()
            } else  {
                SessionState.instance.selectedLanguage = "English"
                SessionState.instance.saveValuesToPreferences(context!!, AppConstants.Companion.PREFERENCES.SELECTED_LANGUAGE.toString(),
                        "English")
            }
        }

        if (!AppConstants.EMPTY.equals(SessionState.instance.selectedLanguage)) {
            settings_language_spinner.setText(SessionState.instance.selectedLanguage)
        }

        var languageList = arrayListOf<String>()
        if (AppConfigDetail.languageList != null) {
            for (x in AppConfigDetail.languageList !!) {
                languageList.add(x.name!!)
            }
        } else  {
            languageList.add("English")
        }
        settings_language_spinner.setItems(languageList.toArray(arrayOfNulls<String>(languageList.size))) //this is important, you must set it to set the item list
        settings_language_spinner.setHintTextColor(resources.getColor(R.color.light_gray))
        settings_language_spinner.setExpandTint(R.color.transparent)
        settings_language_spinner.setExpandTint(R.color.transparent)

        if (settings_country_spinner.text.isNullOrEmpty()) {
            fetchCountryList() // To load only first time
        }
    }

    private fun fetchCountryList() {
        showProgressDialog(getString(R.string.fetch_country))
        RetrofitController.fetchCountryDetails(object: Callback<List<CountryListModel>> {
            override fun onResponse(call: Call<List<CountryListModel>>?, response: Response<List<CountryListModel>>?) {
                if (response != null && response.isSuccessful) {
                    countryList.addAll(response.body())
                    val listOfCountry = arrayListOf<String>()
                    countryList.forEach { listOfCountry.add(it.name!!)}
                    settings_country_spinner.setItems(listOfCountry.toArray(arrayOfNulls<String>(countryList.size)))
                }
                dismissProgressDialog()
            }

            override fun onFailure(call: Call<List<CountryListModel>>?, t: Throwable?) {
                dismissProgressDialog()
                showNetworkErrorSnackBar()
            }
        })
    }

    private fun fetchStateList(countryId: String) {
        showProgressDialog(getString(R.string.fetch_state))
        RetrofitController.fetchStateDetails(countryId, object: Callback<List<StateListModel>> {
            override fun onResponse(call: Call<List<StateListModel>>?, response: Response<List<StateListModel>>?) {
                dismissProgressDialog()
                if (response != null && response.isSuccessful) {
                    stateList.addAll(response.body())
                    val listOfState = arrayListOf<String>()
                    stateList.forEach { listOfState.add(it.name!!)}
                    settings_state_spinner.setItems(listOfState.toArray(arrayOfNulls<String>(stateList.size)))
                }
            }

            override fun onFailure(call: Call<List<StateListModel>>?, t: Throwable?) {
                dismissProgressDialog()
                showNetworkErrorSnackBar()
            }
        })
    }

    private fun fetchCityList(stateId: String) {
        showProgressDialog(getString(R.string.fetch_city))
        RetrofitController.fetchCityDetails(stateId, object: Callback<List<CityListModel>> {
            override fun onResponse(call: Call<List<CityListModel>>?, response: Response<List<CityListModel>>?) {
                dismissProgressDialog()
                if (response != null && response.isSuccessful) {
                    cityList.addAll(response.body())
                    val listOfCity = arrayListOf<String>()
                    cityList.forEach { listOfCity.add(it.name!!)}
                    settings_city_spinner.setItems(listOfCity.toArray(arrayOfNulls<String>(cityList.size)))
                }
            }

            override fun onFailure(call: Call<List<CityListModel>>?, t: Throwable?) {
                dismissProgressDialog()
                showNetworkErrorSnackBar()
            }
        })
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.settings_login_sign_up_frame ->
                if (SessionState.instance.isLoggedIn) {
                    showLogoutAlertDialog()
                } else  {
                    startActivity(LoginActivity::class.java, false)
                }
            R.id.settings_terms_condition_frame -> {
                loadTermsAndConditionWebView(R.string.terms_condition, if (SessionState.instance.termsConditionUrl != null) SessionState.instance.termsConditionUrl else "")
            }
            R.id.settings_support_frame -> {
                val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", AppConstants.SUPPORT_EMAIL, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Regarding ");
                //emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi, \n \n"+ LanguagePack.getString("I am interested in your property") + " " + product_detail_title_text_view.text + ".\n "+ LanguagePack.getString("We can have discussion on") +" \n\n" + LanguagePack.getString("Regards")+",\n"+ SessionState.instance.displayName);
                startActivityForResult(Intent.createChooser(emailIntent,  LanguagePack.getString("Send email...")), 0);
            }
            R.id.settings_my_fav_frame -> {
                if (SessionState.instance.isLoggedIn) {
                    startActivity(MyFavoritesActivity::class.java, false)
                } else {
                    startActivity(LoginRequiredActivity::class.java, false)
                }
            }
            R.id.settings_my_ads_frame -> {
                if (SessionState.instance.isLoggedIn) {
                    startActivity(MyPostedProductActivity::class.java, false)
                } else {
                    startActivity(LoginRequiredActivity::class.java, false)
                }
            }
        }
    }

    private fun logoutUser() {
        SessionState.instance.clearSession()
        SessionState.instance.removePreference(activity!!)
        startActivity(LoginActivity::class.java, true)
        this.activity?.finish()
    }

    private fun showNetworkErrorSnackBar() {
        if (fragment_settings_user_parent_layout != null) Utility.showSnackBar(fragment_settings_user_parent_layout, "Please check your internet connection", context!!)
    }

    fun showProgressDialog(message: String) {
        mProgressDialog = Utility.getProgressDialog(context!!, message)
        mProgressDialog?.show()
    }

    fun dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog?.dismiss()
            mProgressDialog = null
        }
    }

    private fun showLogoutAlertDialog() {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle("")
        builder.setMessage(LanguagePack.getString("Are you sure you want to log out"))
        builder.setPositiveButton(LanguagePack.getString("YES")){dialog, which ->
            dialog.dismiss()
            logoutUser()
        }
        builder.setNegativeButton(LanguagePack.getString("No")){dialog, which ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun loadTermsAndConditionWebView(titleId: Int, url: String) {
        var bundle = Bundle()
        bundle.putString(AppConstants.TERMS_CONDITION_TITLE, LanguagePack.getString(getString(titleId)))
        bundle.putString(AppConstants.TERMS_CONDITION_URL, url)
        startActivity(TermsAndConditionWebView ::class.java, false, bundle)
    }
}
