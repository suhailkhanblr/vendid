package com.bylancer.classified.bylancerclassified.webservices

import com.bylancer.classified.bylancerclassified.dashboard.DashboardDetailModel
import com.bylancer.classified.bylancerclassified.login.AppConfigModel
import com.bylancer.classified.bylancerclassified.utils.AppConstants
import com.bylancer.classified.bylancerclassified.webservices.chat.ChatMessageModel
import com.bylancer.classified.bylancerclassified.webservices.login.UserLoginStatus
import com.bylancer.classified.bylancerclassified.webservices.productlist.ProductsData
import com.bylancer.classified.bylancerclassified.webservices.registration.UserForgetPasswordStatus
import com.bylancer.classified.bylancerclassified.webservices.registration.UserRegistrationStatus
import com.bylancer.classified.bylancerclassified.webservices.settings.CityListModel
import com.bylancer.classified.bylancerclassified.webservices.settings.CountryListModel
import com.bylancer.classified.bylancerclassified.webservices.settings.StateListModel
import retrofit2.Call
import retrofit2.http.*

interface WebServiceApiInterface {

    @POST(AppConstants.REGISTER_URL)
    @FormUrlEncoded
    fun registerUser(@Field("name") name: String, @Field("email") email: String,
                     @Field("username") username: String, @Field("password") password: String, @Field("fb_login") fbLogin: String): Call<UserRegistrationStatus>

    @GET(AppConstants.LOGIN_URL)
    fun loginUserUsingUsername(@Query("username") name: String, @Query("password") email: String): Call<UserLoginStatus>

    @GET(AppConstants.LOGIN_URL)
    fun loginUserUsingEmail(@Query("email") name: String, @Query("password") email: String): Call<UserLoginStatus>

    @GET(AppConstants.FORGOT_PASSWORD_URL)
    fun forgetPassword(@Query("email") name: String): Call<UserForgetPasswordStatus>

    @GET(AppConstants.PRODUCT_LIST_URL)
    fun fetchProducts(@Query("status") status: String, @Query("country_code") countryCode: String,
                      @Query("page") page:String, @Query("limit") limit:String): Call<List<ProductsData>>

    @GET(AppConstants.PRODUCT_DETAIL_URL)
    fun fetchProductsDetails(@Query("item_id") itemID: String): Call<DashboardDetailModel>

    @GET(AppConstants.COUNTRY_DETAIL_URL)
    fun fetchCountryDetails(): Call<List<CountryListModel>>

    @GET(AppConstants.STATE_DETAIL_URL)
    fun fetchStateDetailsByCountry(@Query("country_code") countryId: String): Call<List<StateListModel>>

    @GET(AppConstants.CITY_DETAIL_URL)
    fun fetchCityDetailsByState(@Query("state_code") stateId: String): Call<List<CityListModel>>

    @GET(AppConstants.APP_CONFIG_URL)
    fun fetchAppConfiguration(): Call<AppConfigModel>

    @GET(AppConstants.FETCH_CHAT_URL)
    fun fetchChatMessage(@Query("my_username") myUserName:String, @Query("client") client:String, @Query("page") pageNumber:String): Call<List<ChatMessageModel>>

}