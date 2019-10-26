package com.bylancer.classified.bylancerclassified.webservices.transaction

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TransactionVendorModel {
    @SerializedName("paypal_sandbox_mode")
    @Expose
    var paypalSandboxMode: String? = null
    @SerializedName("paypal_api_username")
    @Expose
    var paypalApiUsername: String? = null
    @SerializedName("paypal_api_password")
    @Expose
    var paypalApiPassword: String? = null
    @SerializedName("paypal_api_signature")
    @Expose
    var paypalApiSignature: String? = null
    @SerializedName("stripe_publishable_key")
    @Expose
    var stripePublishableKey: String? = null
    @SerializedName("stripe_secret_key")
    @Expose
    var stripeSecretKey: String? = null
    @SerializedName("paystack_public_key")
    @Expose
    var paystackPublicKey: String? = null
    @SerializedName("paystack_secret_key")
    @Expose
    var paystackSecretKey: String? = null
    @SerializedName("payumoney_sandbox_mode")
    @Expose
    var payumoneySandboxMode: String? = null
    @SerializedName("payumoney_merchant_key")
    @Expose
    var payumoneyMerchantKey: String? = null
    @SerializedName("payumoney_merchant_salt")
    @Expose
    var payumoneyMerchantSalt: String? = null
    @SerializedName("checkout_account_number")
    @Expose
    var checkoutAccountNumber: String? = null
    @SerializedName("checkout_public_key")
    @Expose
    var checkoutPublicKey: String? = null
    @SerializedName("checkout_private_key")
    @Expose
    var checkoutPrivateKey: String? = null
    @SerializedName("PAYTM_ENVIRONMENT")
    @Expose
    var paytmenvironment: Any? = null
    @SerializedName("PAYTM_MERCHANT_KEY")
    @Expose
    var paytmmerchantkey: Any? = null
    @SerializedName("PAYTM_MERCHANT_MID")
    @Expose
    var paytmmerchantmid: Any? = null
    @SerializedName("PAYTM_MERCHANT_WEBSITE")
    @Expose
    var paytmmerchantwebsite: Any? = null
    @SerializedName("company_bank_info")
    @Expose
    var companyBankInfo: String? = null
    @SerializedName("company_cheque_info")
    @Expose
    var companyChequeInfo: Any? = null
    @SerializedName("cheque_payable_to")
    @Expose
    var chequePayableTo: Any? = null
    @SerializedName("skrill_merchant_id")
    @Expose
    var skrillMerchantId: Any? = null
    @SerializedName("nochex_merchant_id")
    @Expose
    var nochexMerchantId: Any? = null
}
