package com.bylancer.classified.bylancerclassified.helper

import com.bylancer.classified.bylancerclassified.activities.BylancerBuilderActivity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

object PhoneNumberAuthHelper {
    private const val TAG = "PhoneAuth"
    private const val STATE_INITIALIZED = 1
    private const val STATE_VERIFY_FAILED = 3
    private const val STATE_VERIFY_SUCCESS = 4
    private const val STATE_CODE_SENT = 2
    private const val STATE_SIGNIN_FAILED = 5
    private const val STATE_SIGNIN_SUCCESS = 6
    private const val STATE_PHONE_WRONG = 7
    private const val STATE_QUOTA_EXCEEDS = 8
    private const val STATE_INVALID_CODE = 9
    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    private var verificationInProgress = false
    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var mActivity: BylancerBuilderActivity
    private lateinit var mOTPListener: OnOTPResponse

    fun startPhoneNumberVerification(phoneNumber: String, phoneNumberAuthCallback: OnOTPResponse,
                                     activity: BylancerBuilderActivity) {
        mActivity = activity
        mOTPListener = phoneNumberAuthCallback
        auth = FirebaseAuth.getInstance()
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber, // Phone number to verify
                60, // Timeout duration
                TimeUnit.SECONDS, // Unit of timeout
                activity, // Activity (for callback binding)
                callbacks) // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        verificationInProgress = true
    }

   // private var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
    // Initialize phone auth callbacks
    // [START phone_auth_callbacks]
   private var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
           // Log.d(TAG, "onVerificationCompleted:$credential")
            // [START_EXCLUDE silent]
            verificationInProgress = false
            // [END_EXCLUDE]

            // [START_EXCLUDE silent]
            // Update the UI and attempt sign in with the phone credential
            updateUI(STATE_VERIFY_SUCCESS, credential)
            // [END_EXCLUDE]
            signInWithPhoneAuthCredential(credential, mActivity)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
           // Log.w(TAG, "onVerificationFailed", e)
            // [START_EXCLUDE silent]
            verificationInProgress = false
            // [END_EXCLUDE]

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // [START_EXCLUDE]
                mOTPListener?.onOTPResponse(STATE_PHONE_WRONG)
                // [END_EXCLUDE]
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // [START_EXCLUDE]
                mOTPListener?.onOTPResponse(STATE_QUOTA_EXCEEDS)
                // [END_EXCLUDE]
            }

            // Show a message and update the UI
            // [START_EXCLUDE]
            updateUI(STATE_VERIFY_FAILED)
            // [END_EXCLUDE]
        }

        override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
           // Log.d(TAG, "onCodeSent:$verificationId")

            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            resendToken = token

            // [START_EXCLUDE]
            // Update UI
            updateUI(STATE_CODE_SENT)
            // [END_EXCLUDE]
        }
    }
    // [END phone_auth_callbacks]

    fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        // [START verify_with_code]
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential, mActivity)
    }

    // [START resend_verification]
    fun resendVerificationCode(
            phoneNumber: String,
            token: PhoneAuthProvider.ForceResendingToken?,
            activity: BylancerBuilderActivity
    ) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber, // Phone number to verify
                60, // Timeout duration
                TimeUnit.SECONDS, // Unit of timeout
                activity, // Activity (for callback binding)
                callbacks, // OnVerificationStateChangedCallbacks
                token) // ForceResendingToken from callbacks
    }

    // [START sign_in_with_phone]
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential, activity: BylancerBuilderActivity) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                       // Log.d(TAG, "signInWithCredential:success")

                        val user = task.result?.user
                        // [START_EXCLUDE]
                        updateUI(STATE_SIGNIN_SUCCESS, user)
                        // [END_EXCLUDE]
                    } else {
                        // Sign in failed, display a message and update the UI
                       // Log.w(TAG, "signInWithCredential:failure", task.exception)
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            // [START_EXCLUDE silent]
                            mOTPListener?.onOTPResponse(STATE_INVALID_CODE)
                            // [END_EXCLUDE]
                        }
                        // [START_EXCLUDE silent]
                        // Update UI
                        updateUI(STATE_SIGNIN_FAILED)
                        // [END_EXCLUDE]
                    }
                }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            updateUI(STATE_SIGNIN_SUCCESS, user)
        } else {
            updateUI(STATE_INITIALIZED)
        }
    }

    private fun updateUI(uiState: Int, cred: PhoneAuthCredential) {
        updateUI(uiState, null, cred)
    }

    private fun updateUI(
            uiState: Int,
            user: FirebaseUser? = auth.currentUser,
            cred: PhoneAuthCredential? = null
    ) {
        mOTPListener?.onOTPResponse(uiState)
        when (uiState) {
            STATE_INITIALIZED -> {
                // Initialized state, show only the phone number field and start button

            }
            STATE_CODE_SENT -> {

            }
            STATE_VERIFY_FAILED -> {
                // Verification has failed, show all options

            }
            STATE_VERIFY_SUCCESS -> {
                // Verification has succeeded, proceed to firebase sign in

            }
            STATE_SIGNIN_FAILED -> {

            }
                // No-op, handled by sign-in check
            STATE_SIGNIN_SUCCESS -> {

            }
        } // Np-op, handled by sign-in check

    }

    private fun signOut() {
        auth.signOut()
    }

}