package com.bylancer.classified.bylancerclassified.premium

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.text.Html
import android.text.Spannable
import android.view.WindowManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bylancer.classified.bylancerclassified.R
import com.bylancer.classified.bylancerclassified.utils.AppConstants
import com.bylancer.classified.bylancerclassified.utils.LanguagePack
import com.bylancer.classified.bylancerclassified.utils.showToast
import kotlinx.android.synthetic.main.go_premium_app_dialog.*

class PremiumAlertDialog(context: Context, private val productItemList : ArrayList<PremiumObjectDetails>,
                         private val theme: Int) : Dialog(context, theme), OnPremiumItemSelection {
    private var mContext : Context = context
    private var totalCost : Int = 0

    fun showDialog(type : Int, callback : OnPremiumDoneButtonClicked) {
        setContentView(R.layout.go_premium_app_dialog)
        window?.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawable(ColorDrawable(mContext?.resources.getColor(android.R.color.transparent)))
        setCanceledOnTouchOutside(false)
        premium_title?.text = LanguagePack.getString(mContext?.getString(R.string.premium_dialog_title))
        premium_confirm_text_view?.text = LanguagePack.getString(mContext?.getString(R.string.premium))
        setUpRecyclerView()

        close_premium_dialog?.setOnClickListener {
            dismiss()
        }
        premium_confirm_text_view.setOnClickListener {
            if (totalCost > 0) {
                dismiss()
                callback?.onPremiumDoneButtonClicked(totalCost.toString())
            } else {
                mContext?.showToast(mContext?.getString(R.string.select_one_feature))
            }
        }

        if (AppConstants.GO_FOR_PREMIUM_APP == type) {
            productItemList.forEach {
                totalCost += it.cost
            }
            onItemSelection(false, 0)
        }

        show()
    }

    private fun setUpRecyclerView() {
        if (mContext != null) {
            premium_recyclerView?.setHasFixedSize(false)
            premium_recyclerView?.layoutManager = LinearLayoutManager(mContext)
            premium_recyclerView?.itemAnimator = DefaultItemAnimator()
            premium_recyclerView?.adapter = PremiumItemAdapter(productItemList, this)

        }
    }

    override fun onItemSelection(isSelected: Boolean, cost: Int) {
        if (isSelected) {
            totalCost += cost
        } else {
            totalCost -= cost
        }

        if (totalCost > 0) {
            val cost = LanguagePack.getString("Total price") + " : $totalCost"
            premium_title?.text = Html.fromHtml(LanguagePack.getString(mContext?.getString(R.string.premium_dialog_title)) +
                    "<br><b><font color=#006400>$cost</font></b>")
        } else {
            premium_title?.text = LanguagePack.getString(mContext?.getString(R.string.premium_dialog_title))
        }
    }
}
