package com.phoenix.vendido.buysell.webservices

import com.phoenix.vendido.buysell.appconfig.AppConfigModel
import com.phoenix.vendido.buysell.dashboard.DashboardDetailModel
import com.phoenix.vendido.buysell.utils.AppConstants
import com.phoenix.vendido.buysell.utils.AppConstants.Companion.BASE_URL
import com.phoenix.vendido.buysell.utils.SessionState
import com.phoenix.vendido.buysell.webservices.chat.ChatMessageModel
import com.phoenix.vendido.buysell.webservices.chat.ChatSentStatus
import com.phoenix.vendido.buysell.webservices.chat.GroupChatModel
import com.phoenix.vendido.buysell.webservices.languagepack.LanguagePackModel
import com.phoenix.vendido.buysell.webservices.login.UserLoginData
import com.phoenix.vendido.buysell.webservices.login.UserLoginStatus
import com.phoenix.vendido.buysell.webservices.makeanoffer.MakeAnOfferData
import com.phoenix.vendido.buysell.webservices.makeanoffer.MakeAnOfferStatus
import com.phoenix.vendido.buysell.webservices.membership.CurrentUserMembershipPlan
import com.phoenix.vendido.buysell.webservices.membership.MembershipPlanList
import com.phoenix.vendido.buysell.webservices.notificationmessage.AddTokenStatus
import com.phoenix.vendido.buysell.webservices.notificationmessage.NotificationCounter
import com.phoenix.vendido.buysell.webservices.notificationmessage.NotificationDataModel
import com.phoenix.vendido.buysell.webservices.productlist.ProductInputData
import com.phoenix.vendido.buysell.webservices.productlist.ProductsData
import com.phoenix.vendido.buysell.webservices.registration.UserForgetPasswordStatus
import com.phoenix.vendido.buysell.webservices.registration.UserRegistrationData
import com.phoenix.vendido.buysell.webservices.registration.UserRegistrationStatus
import com.phoenix.vendido.buysell.webservices.settings.CityListModel
import com.phoenix.vendido.buysell.webservices.settings.CountryListModel
import com.phoenix.vendido.buysell.webservices.settings.ProductUploadProductModel
import com.phoenix.vendido.buysell.webservices.settings.StateListModel
import com.phoenix.vendido.buysell.webservices.transaction.TransactionResponseModel
import com.phoenix.vendido.buysell.webservices.transaction.TransactionVendorModel
import com.phoenix.vendido.buysell.webservices.uploadproduct.PostedProductResponseModel
import com.phoenix.vendido.buysell.webservices.uploadproduct.UploadDataDetailModel
import com.phoenix.vendido.buysell.webservices.uploadproduct.UploadProductModel
import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*
import javax.security.cert.CertificateException

class RetrofitController {
    companion object {
        private val mRetrofit: Retrofit = getInstance()
        private val webserviceApi = mRetrofit.create<WebServiceApiInterface>(WebServiceApiInterface::class.java!!)

        fun getInstance() : Retrofit {
            if (mRetrofit == null) {
                val gson = GsonBuilder()
                        .setLenient()
                        .create()

                return Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .client(getOkHttpClientBuilder().build())
                        .build()
            }

            return mRetrofit
        }

        fun registerUser(userData: UserRegistrationData, registerUserCallBack: Callback<UserRegistrationStatus>) {
            val call = webserviceApi.registerUser(userData.name!!, userData!!.email!!, userData!!.username!!, userData!!.password!!, userData!!.fbLogin!!)
            call.enqueue(registerUserCallBack)
        }

        fun loginUserUsingEmail(userData: UserLoginData, registerUserCallBack: Callback<UserLoginStatus>) {
            val call = webserviceApi.loginUserUsingEmail(userData.email!!, userData!!.password!!)
            call.enqueue(registerUserCallBack)
        }

        fun loginUserUsingUsername(userData: UserLoginData, registerUserCallBack: Callback<UserLoginStatus>) {
            val call = webserviceApi.loginUserUsingUsername(userData.username!!, userData!!.password!!)
            call.enqueue(registerUserCallBack)
        }

        fun userForgetPassword(email: String, registerUserCallBack: Callback<UserForgetPasswordStatus>) {
            val call = webserviceApi.forgetPassword(email)
            call.enqueue(registerUserCallBack)
        }

        fun fetchProducts(productInputData: ProductInputData, fetchProductsCallBack: Callback<List<ProductsData>>) {
            val call = webserviceApi.fetchProducts(productInputData.status, productInputData.countryCode, productInputData.stateCode, productInputData.cityId, productInputData.pageNumber,
                    productInputData.limit)
            call.enqueue(fetchProductsCallBack)
        }

        fun fetchFeaturedAndUrgentProducts(productInputData: ProductInputData, fetchProductsCallBack: Callback<List<ProductsData>>) {
            val call = webserviceApi.fetchFeaturedAndUrgentProducts(productInputData.status, productInputData.countryCode, productInputData.stateCode, productInputData.cityId, productInputData.pageNumber,
                    productInputData.limit)
            call.enqueue(fetchProductsCallBack)
        }

        fun fetchProductsForUser(productInputData: ProductInputData, fetchProductsCallBack: Callback<List<ProductsData>>) {
            val call = webserviceApi.fetchProductsForUser(productInputData.pageNumber,
                    productInputData.limit, productInputData.userId)
            call.enqueue(fetchProductsCallBack)
        }

        fun fetchProductDetails(productId: String, fetchProductsDetailCallBack: Callback<DashboardDetailModel>) {
            val call = webserviceApi.fetchProductsDetails(productId)
            call.enqueue(fetchProductsDetailCallBack)
        }

        fun fetchProductDetailsByCategory(productInputData: ProductInputData, fetchProductsByCategoryCallBack: Callback<List<ProductsData>>) {
            val call = webserviceApi.fetchProductsByCategory(productInputData.status, productInputData.countryCode, productInputData.stateCode, productInputData.cityId, productInputData.pageNumber,
                    productInputData.limit, productInputData.categoryId, productInputData.subCategoryId, productInputData.keywords, productInputData.additionalSearchInfo)
            call.enqueue(fetchProductsByCategoryCallBack)
        }

        fun fetchCountryDetails(fetchCountriesDetailCallBack: Callback<List<CountryListModel>>) {
            val call = webserviceApi.fetchCountryDetails()
            call.enqueue(fetchCountriesDetailCallBack)
        }

        fun fetchStateDetails(countryId: String, fetchStateDetailCallBack: Callback<List<StateListModel>>) {
            val call = webserviceApi.fetchStateDetailsByCountry(countryId)
            call.enqueue(fetchStateDetailCallBack)
        }

        fun fetchCityDetails(stateId: String, fetchCityDetailCallBack: Callback<List<CityListModel>>) {
            val call = webserviceApi.fetchCityDetailsByState(stateId)
            call.enqueue(fetchCityDetailCallBack)
        }

        fun fetchAppConfig(langCode: String, appConfigModelCallBack: Callback<AppConfigModel>) {
            val call = webserviceApi.fetchAppConfiguration(langCode)
            call.enqueue(appConfigModelCallBack)
        }

        fun fetchChatMessages(userId:String, clientUserId: String, pageNo: String, fetchChatMessageCallback: Callback<List<ChatMessageModel>>) {
            val call = webserviceApi.fetchChatMessage(userId, clientUserId, pageNo)
            call.enqueue(fetchChatMessageCallback)
        }

        fun fetchGroupChatMessages(sessionUserId:String, fetchGroupChatMessageCallback: Callback<List<GroupChatModel>>) {
            val call = webserviceApi.fetchGroupChatMessage(sessionUserId)
            call.enqueue(fetchGroupChatMessageCallback)
        }

        fun fetchLanguagePack(fetchLanguagePackCallback: Callback<List<LanguagePackModel>>) {
            val call = webserviceApi.fetchLanguagePack()
            call.enqueue(fetchLanguagePackCallback)
        }

        fun makeAnOffer(makeAnOfferData: MakeAnOfferData, makeAnOfferCallCallback: Callback<MakeAnOfferStatus>) {
            val call = webserviceApi.makeAnOffer(makeAnOfferData.userId, makeAnOfferData.message, makeAnOfferData.senderId,
                    makeAnOfferData.senderName, makeAnOfferData.ownerName, makeAnOfferData.email, makeAnOfferData.subject, makeAnOfferData.productName,
                    makeAnOfferData.type, makeAnOfferData.productId)
            call.enqueue(makeAnOfferCallCallback)
        }

        fun sendChatMessage(fromId:String, toId: String, message:String, sendChatCallback: Callback<ChatSentStatus>) {
            val call = webserviceApi.sendChatMessage(fromId, toId, message)
            call.enqueue(sendChatCallback)
        }

        fun getNotificationMessage(user_id:String, notificationMessageCallback: Callback<List<NotificationDataModel>>) {
            val call = webserviceApi.getNotificationMessage(user_id)
            call.enqueue(notificationMessageCallback)
        }

        fun addDeviceToken(userId:String, userName:String, deviceId:String, token:String, addTokenCallback: Callback<AddTokenStatus>) {
            val call = webserviceApi.addFirebaseDeviceToken(userId, deviceId, userName, token)
            call.enqueue(addTokenCallback)
        }

        fun updateUserProfilePic(image: MultipartBody.Part, updateProfilePicCallback: Callback<ProductUploadProductModel>) {
            val call = webserviceApi.updateProfilePic(SessionState.instance.userId, image)
            call.enqueue(updateProfilePicCallback)
        }

        fun updateUserPostedProductPic(image: MultipartBody.Part, updateProfilePicCallback: Callback<UploadProductModel>) {
            val call = webserviceApi.updateUserPostedProductPic(image)
            call.enqueue(updateProfilePicCallback)
        }

        fun postUserProduct(uploadDataDetailModel: UploadDataDetailModel, postedProductCallback: Callback<PostedProductResponseModel>) {
            val call = webserviceApi.postUserProduct(uploadDataDetailModel.userId,
                    uploadDataDetailModel.title,
                    uploadDataDetailModel.categoryId,
                    uploadDataDetailModel.subcategoryId,
                    uploadDataDetailModel.countryCode,
                    uploadDataDetailModel.state,
                    uploadDataDetailModel.city,
                    uploadDataDetailModel.description,
                    uploadDataDetailModel.location,
                    uploadDataDetailModel.hidePhone,
                    uploadDataDetailModel.negotiable,
                    uploadDataDetailModel.price,
                    uploadDataDetailModel.phone,
                    uploadDataDetailModel.latitude,
                    uploadDataDetailModel.longitude,
                    uploadDataDetailModel.itemScreen,
                    uploadDataDetailModel.additionalInfo)
            call.enqueue(postedProductCallback)
        }

        fun postPremiumAdTransactionDetails(productName: String, amount: String, userId: String, productId: String,
                                            isFeatured: String, isUrgent: String, isHighlighted: String, folder: String,
                                            paymentType: String, transactionDetails: String,  postedTransactionCallback: Callback<TransactionResponseModel>) {
            val call = webserviceApi.postPremiumTransactionDetail(productName,
                    amount,
                    userId,
                    productId,
                    isFeatured,
                    isUrgent,
                    isHighlighted,
                    folder,
                    paymentType,
                    transactionDetails)
            call.enqueue(postedTransactionCallback)
        }

        fun postPremiumAppTransactionDetails(planName: String, amount: String, userId: String, subId: String, folder: String,
                                             postedTransactionCallback: Callback<TransactionResponseModel>) {
            val call = webserviceApi.postPremiumAppTransactionDetail(planName,
                    amount,
                    userId,
                    subId,
                    folder,
                    AppConstants.PAYMENT_TYPE_APP_PREMIUM)
            call.enqueue(postedTransactionCallback)
        }


        fun fetchNotificationCount(fetchNotificationCountCallBack: Callback<NotificationCounter>) {
            val call = webserviceApi.getUnReadMessageCount(SessionState.instance.userId)
            call.enqueue(fetchNotificationCountCallBack)
        }

        fun fetchTransactionVendorCredentials(fetchVendorCredentialsCallBack: Callback<TransactionVendorModel>) {
            val call = webserviceApi.fetchPaymentVendorCredentials()
            call.enqueue(fetchVendorCredentialsCallBack)
        }

        fun fetchMembershipPlan(fetchMemberShipPlan: Callback<MembershipPlanList>) {
            val call = webserviceApi.fetchMembershipPlan()
            call.enqueue(fetchMemberShipPlan)
        }

        fun fetchCurrentUserMembershipPlan(fetchCurrentUserMemberShipPlan: Callback<CurrentUserMembershipPlan>) {
            val call = webserviceApi.fetchCurrentUserMembershipPlan(SessionState.instance.userId)
            call.enqueue(fetchCurrentUserMemberShipPlan)
        }
    }
}

fun getOkHttpClientBuilder() : OkHttpClient.Builder {
    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }

        @Throws(CertificateException::class)
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {

        }
    })

    // Install the all-trusting trust manager

    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    val client = OkHttpClient.Builder()
    client.interceptors().add(httpLoggingInterceptor)
    client.readTimeout(180, TimeUnit.SECONDS)
    client.connectTimeout(180, TimeUnit.SECONDS)

    val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
    keyStore.load(null, null)

    val sslContext = SSLContext.getInstance("TLS")

    val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
    trustManagerFactory.init(keyStore)
    val keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
    keyManagerFactory.init(keyStore, "keystore_pass".toCharArray())
    sslContext.init(null, trustAllCerts, SecureRandom())
    client.sslSocketFactory(sslContext.socketFactory)
            .hostnameVerifier { _, _ -> true }
    return client
}