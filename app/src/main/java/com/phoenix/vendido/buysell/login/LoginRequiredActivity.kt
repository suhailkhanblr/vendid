package com.phoenix.vendido.buysell.login


import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import com.phoenix.vendido.buysell.R
import com.phoenix.vendido.buysell.activities.BylancerBuilderActivity
import com.phoenix.vendido.buysell.utils.LanguagePack
import kotlinx.android.synthetic.main.fragment_login_required.*


/**
 * A simple [Fragment] subclass.
 *
 */
class LoginRequiredActivity : BylancerBuilderActivity(), View.OnClickListener {
    override fun setLayoutView() = R.layout.fragment_login_required

    override fun initialize(savedInstanceState: Bundle?) {
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        setFinishOnTouchOutside(false)

        login_required_text_view.text = LanguagePack.getString(getString(R.string.login_required))
        login_required_sub_text_view.text = LanguagePack.getString(getString(R.string.must_login))
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.login_cancel_button -> onBackPressed()
            R.id.login_ok_button -> {
                onBackPressed()
                Handler().postDelayed({
                    startActivity(LoginActivity::class.java, false)
                }, 500)
            }
        }
    }

}
