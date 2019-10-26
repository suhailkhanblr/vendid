package com.bylancer.classified.bylancerclassified.webservices.transaction

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TransactionVendorModel {

    @SerializedName("paypal_sandbox_mode")
    @Expose
    var paypalSandboxMode: String? = null
    @SerializedName("paypal_client_id")
    @Expose
    var paypalClientId: String? = null
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
    @SerializedName("payumoney_merchant_id")
    @Expose
    var payumoneyMerchantId: String? = null
    @SerializedName("checkout_account_number")
    @Expose
    var checkoutAccountNumber: String? = null
    @SerializedName("checkout_public_key")
    @Expose
    var checkoutPublicKey: String? = null
    @SerializedName("checkout_private_key")
    @Expose
    var checkoutPrivateKey: String? = null
}

