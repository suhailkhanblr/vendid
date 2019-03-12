package com.bylancer.classified.bylancerclassified.settings


import android.os.Bundle
import android.app.Fragment
import android.graphics.Rect
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.view.View
import android.view.ViewTreeObserver
import com.bylancer.classified.bylancerclassified.R
import com.bylancer.classified.bylancerclassified.fragments.BylancerBuilderFragment
import com.bylancer.classified.bylancerclassified.login.LoginActivity
import com.bylancer.classified.bylancerclassified.utils.AppConstants
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


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
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
        settings_login_sign_up_frame.setOnClickListener(this)
        if (SessionState.instance.isLoggedIn) {
            settings_login_sign_up_icon.setImageResource(R.drawable.ic_settings_logout)
            settings_login_sign_up_text.setText(getString(R.string.log_out))
        }

        settings_country_spinner.setHint("Select country") //change title of spinner-dialog programmatically
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

        settings_language_spinner.setOnItemClickListener{ position ->
            SessionState.instance.selectedLanguage = "English"
            SessionState.instance.saveValuesToPreferences(context!!, AppConstants.Companion.PREFERENCES.SELECTED_LANGUAGE.toString(),
                    "English")
        }

        if (!AppConstants.EMPTY.equals(SessionState.instance.selectedLanguage)) {
            settings_language_spinner.setText(SessionState.instance.selectedLanguage)
        }
        settings_language_spinner.setItems(arrayOf("English")) //this is important, you must set it to set the item list
        settings_language_spinner.setHintTextColor(resources.getColor(R.color.light_gray))
        settings_language_spinner.setExpandTint(R.color.transparent)
        settings_language_spinner.setExpandTint(R.color.transparent)

        fetchCountryList()
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
            R.id.settings_login_sign_up_frame -> if (SessionState.instance.isLoggedIn) {
                showLogoutAlertDialog()
            } else  {
                startActivity(LoginActivity::class.java, false)
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
        builder.setMessage("Are you sure you want to log out")
        builder.setPositiveButton("YES"){dialog, which ->
            dialog.dismiss()
            logoutUser()
        }
        builder.setNegativeButton("No"){dialog, which ->
            dialog.dismiss()
        }
        builder.create().show()
    }
}
