package com.bylancer.classified.bylancerclassified.premium

import android.net.Uri
import com.bylancer.classified.bylancerclassified.utils.AppConstants
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
        fun getPayPalConfig(env: String?, clientId: String?): PayPalConfiguration {
            var CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION
            if (AppConstants.YES.equals(env, true)) {
                CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX
            }
            return PayPalConfiguration()
                    .environment(CONFIG_ENVIRONMENT)
                    .clientId(clientId)
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