package com.bylancer.classified.bylancerclassified.premium

import android.net.Uri
import com.bylancer.classified.bylancerclassified.utils.SessionState
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalPayment
import java.math.BigDecimal

class PayPal {
    companion object {
        /**
         * - Set to PayPalConfiguration.ENVIRONMENT_PRODUCTION to move real money.
         * - Set to PayPalConfiguration.ENVIRONMENT_SANDBOX to use your test credentials
         * from https://developer.paypal.com
         * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
         * without communicating to PayPal's servers.
         */
        private val CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK

        // note that these credentials will differ between live & sandbox environments.
        private val CONFIG_CLIENT_ID = "credentials from developer.paypal.com"
        fun getPayPalConfig(env: String?, clientId: String?): PayPalConfiguration {
            return PayPalConfiguration()
                    .environment(CONFIG_ENVIRONMENT)
                    .clientId(CONFIG_CLIENT_ID)
                    .merchantName(SessionState.instance.appName ?: "")
                    .merchantPrivacyPolicyUri(Uri.parse(SessionState.instance.privacyPolicyUrl))
                    .merchantUserAgreementUri(Uri.parse(SessionState.instance.termsConditionUrl))
        }

        /*
         * PAYMENT_INTENT_SALE will cause the payment to complete immediately.
         * Change PAYMENT_INTENT_SALE to
         *   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
         *   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
         *     later via calls from your server.
         *
         * Also, to include additional payment details and an item list, see getPayPalItemDetails() below.
         */
        fun getPayPalItemDetails(amount: String, itemDetail: String): PayPalPayment {
            return PayPalPayment(BigDecimal(amount), SessionState.instance.paymentCurrencyCode, itemDetail,
                    PayPalPayment.PAYMENT_INTENT_SALE)
        }
    }
}