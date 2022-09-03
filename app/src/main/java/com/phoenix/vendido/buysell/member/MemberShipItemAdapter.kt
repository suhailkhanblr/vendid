package com.phoenix.vendido.buysell.member

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.phoenix.vendido.buysell.R
import com.phoenix.vendido.buysell.dashboard.OnProductItemClickListener
import com.phoenix.vendido.buysell.utils.AppConstants
import com.phoenix.vendido.buysell.utils.LanguagePack
import com.phoenix.vendido.buysell.utils.SessionState
import com.phoenix.vendido.buysell.utils.getSpannableString
import com.phoenix.vendido.buysell.webservices.membership.MembershipPlan

class MemberShipItemAdapter(private val mMembershipPlanItemList : List<MembershipPlan>, private val onProductItemClickListener: OnProductItemClickListener) : RecyclerView.Adapter<MemberShipItemAdapter.MembershipPlanItemListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembershipPlanItemListHolder {
        return MembershipPlanItemListHolder(R.layout.membership_plan_item_layout, parent)
    }

    override fun getItemCount(): Int {
        return mMembershipPlanItemList.size
    }

    override fun onBindViewHolder(holder : MembershipPlanItemListHolder, position : Int) {
        val dataModel : MembershipPlan = mMembershipPlanItemList[position]
        holder.chargesTV?.text = String.format("%s%s / %s",HtmlCompat.fromHtml(SessionState.instance.paymentCurrencySign,
                HtmlCompat.FROM_HTML_MODE_LEGACY), dataModel.cost, dataModel.term)
        holder.postLimitTV?.context?.let { context ->
            holder.postLimitTV?.text = getSpannableString(LanguagePack.getString(context.getString(R.string.ad_post_limit)), dataModel.limit)

            holder.adExpiryTV?.text = if (AppConstants.UNSELECTED == dataModel.selected) {
                getSpannableString(LanguagePack.getString(context.getString(R.string.ad_expiry_in)),
                        String.format("%s %s", dataModel.duration, LanguagePack.getString(context.getString(R.string.days))))
            } else {
                getSpannableString(LanguagePack.getString(context.getString(R.string.ad_expiry_in)), dataModel.duration)
            }

            holder.featuredFeeTV?.text = getSpannableString(LanguagePack.getString(context.getString(R.string.ad_featured_fee)),
                    "${dataModel.featuredFee} ${LanguagePack.getString(context.getString(R.string.for_text))} ${dataModel.featuredDuration} ${LanguagePack.getString(context.getString(R.string.days))}")

            holder.urgentFeeTV?.text = getSpannableString(LanguagePack.getString(context.getString(R.string.ad_urgent_fee)),
                    "${dataModel.urgentFee} ${LanguagePack.getString(context.getString(R.string.for_text))} ${dataModel.urgentDuration} ${LanguagePack.getString(context.getString(R.string.days))}")

            holder.highlightAdFeeTV?.text = getSpannableString(LanguagePack.getString(context.getString(R.string.ad_highlighted_fee)),
                    "${dataModel.highlightFee} ${LanguagePack.getString(context.getString(R.string.for_text))} ${dataModel.highlightDuration} ${LanguagePack.getString(context.getString(R.string.days))}")

            holder.topSearchTV?.text = context.getString(R.string.ad_top_search_result_category)
            holder.showAdAsPremiumOnHomeTV?.text = context.getString(R.string.ad_show_as_premium_on_home)
            holder.showAdFromSearchOnHomeTV?.text = context.getString(R.string.ad_show_on_home_as_search_result)

            setDrawableStart(holder.topSearchTV, dataModel.topSearchResult)
            setDrawableStart(holder.showAdAsPremiumOnHomeTV, dataModel.showOnHome)
            setDrawableStart(holder.showAdFromSearchOnHomeTV, dataModel.showInHomeSearch)

            if (AppConstants.SELECTED == dataModel.selected) {
                holder.upgradeMembershipButton?.visibility = View.GONE
                holder.currentPlanTV?.visibility = View.VISIBLE
                holder.currentPlanTV?.text = LanguagePack.getString(context.getString(R.string.your_current_plan))
            } else {
                holder.upgradeMembershipButton?.visibility = View.VISIBLE
                holder.currentPlanTV?.visibility = View.GONE
            }
        }

        holder.upgradeMembershipButton?.setOnClickListener {
            onProductItemClickListener?.onProductItemClicked(dataModel.id, dataModel.cost, dataModel.title)
        }
    }

    private fun setDrawableStart(textView: AppCompatTextView?, isSelected: String?) {
        if (AppConstants.YES.equals(isSelected, true)) {
            textView?.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_ok_32, 0, 0, 0)
        } else {
            textView?.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_close_32, 0, 0, 0)
        }
    }

    inner class MembershipPlanItemListHolder(id : Int, parent: ViewGroup) : BaseVH(id, parent) {
        var chargesTV : AppCompatTextView? = null
        var postLimitTV : AppCompatTextView? = null
        var adExpiryTV : AppCompatTextView? = null
        var featuredFeeTV : AppCompatTextView? = null
        var urgentFeeTV : AppCompatTextView? = null
        var highlightAdFeeTV : AppCompatTextView? = null
        var topSearchTV: AppCompatTextView? = null
        var showAdAsPremiumOnHomeTV: AppCompatTextView? = null
        var showAdFromSearchOnHomeTV: AppCompatTextView? = null
        var upgradeMembershipButton: AppCompatTextView? = null
        var currentPlanTV: AppCompatTextView? = null
        init {
            chargesTV = findViewById(R.id.charges_tv)
            postLimitTV = findViewById(R.id.post_limit_tv)
            adExpiryTV = findViewById(R.id.ad_expiry_tv)
            featuredFeeTV = findViewById(R.id.featured_fee_tv)
            urgentFeeTV = findViewById(R.id.urgent_fee_tv)
            highlightAdFeeTV = findViewById(R.id.highlight_ad_fee_tv)
            topSearchTV = findViewById(R.id.top_search_tv)
            showAdAsPremiumOnHomeTV = findViewById(R.id.show_ad_as_premium_on_home_tv)
            showAdFromSearchOnHomeTV = findViewById(R.id.show_ad_from_search_on_home_tv)
            upgradeMembershipButton = findViewById(R.id.upgrade_membership_button)
            currentPlanTV = findViewById(R.id.current_plan_tv)
        }
    }

    abstract inner class BaseVH(id: Int, parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(id, parent, false)) {
        fun <T : View> findViewById(id: Int) : T {
            return this.itemView.findViewById(id)
        }
    }
}