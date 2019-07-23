package com.bylancer.classified.bylancerclassified.dashboard

import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bylancer.classified.bylancerclassified.R
import com.bylancer.classified.bylancerclassified.utils.AppConstants
import com.bylancer.classified.bylancerclassified.utils.Utility
import com.bylancer.classified.bylancerclassified.utils.checkIfNumber
import com.bylancer.classified.bylancerclassified.webservices.productlist.ProductsData


class DashboardItemAdapter(private val dashboardItemList : List<ProductsData>, private val onProductItemClickListener: OnProductItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var holder : RecyclerView.ViewHolder? = null
        when (DashboardItemTypes.getByVal(viewType)) {
            DashboardItemTypes.TOP_LAYOUT -> {
                holder = DashboardTopLayoutItem(R.layout.dashboard_top_item_layout, parent)
            }
            DashboardItemTypes.LIST_ITEM -> {
                holder = DashboardListItemLayoutItem(R.layout.dashboard_top_picks_item_layout, parent)
            }
        }
        return holder!!
    }

    override fun getItemCount(): Int {
        return dashboardItemList.size
    }

    override fun onBindViewHolder(holder : RecyclerView.ViewHolder, position : Int) {
        val dataModel : ProductsData = dashboardItemList.get(position)
        when(DashboardItemTypes.getByVal(dataModel.itemType)) {
            DashboardItemTypes.TOP_LAYOUT -> {
                val dashboardTopLayoutItem : DashboardTopLayoutItem =  holder as DashboardTopLayoutItem
            }
            DashboardItemTypes.LIST_ITEM -> {
                val dashboardListItemLayout : DashboardListItemLayoutItem = holder as DashboardListItemLayoutItem
                dashboardListItemLayout.listItemDescription!!.text = dataModel.productName
                dashboardListItemLayout.listItemDistance!!.text = dataModel.location
                if (!dataModel.price.isNullOrEmpty() && checkIfNumber(dataModel.price!!)) {
                    dashboardListItemLayout.listItemPrice!!.text = dataModel.price
                    dashboardListItemLayout.listItemPrice!!.visibility = View.VISIBLE
                } else {
                    dashboardListItemLayout.listItemPrice!!.visibility = View.GONE
                }

                var symbol = ""
                if (dataModel.currency != null) {
                    symbol = Utility.decodeUnicode(dataModel.currency!!)
                }
                if (AppConstants.CURRENCY_IN_LEFT.equals(dataModel.currencyInLeft)) {
                    dashboardListItemLayout.listItemPrice!!.text  = symbol + dataModel.price
                } else {
                    dashboardListItemLayout.listItemPrice!!.text  = dataModel.price + symbol
                }
                Glide.with(dashboardListItemLayout.listItemImageView!!.context).load("https://www.advertwide.com/storage/products/1563537457_pom7.jpg").apply(RequestOptions().fitCenter()).into(dashboardListItemLayout.listItemImageView!!)
                dashboardListItemLayout.itemView.setOnClickListener {
                    if(onProductItemClickListener != null) {
                        onProductItemClickListener.onProductItemClicked(dataModel.id, dataModel.productName, dataModel.username)
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return dashboardItemList.get(position).itemType
    }

    private inner class DashboardTopLayoutItem(id : Int, parent: ViewGroup) : BaseVH(id, parent) {
        init {

        }
    }

    private inner class DashboardListItemLayoutItem(id : Int, parent: ViewGroup) : BaseVH(id, parent) {
        var listItemImageView : ImageView? = null
        var listItemPrice : AppCompatTextView? = null
        var listItemDescription : AppCompatTextView? = null
        var listItemDistance : AppCompatTextView? = null
        init {
            listItemImageView = findViewById(R.id.dashboard_item_image_view)
            listItemPrice = findViewById(R.id.dashboard_item_price)
            listItemDescription = findViewById(R.id.dashboard_item_description)
            listItemDistance = findViewById(R.id.dashboard_item_distance)
        }
    }

    internal abstract inner class BaseVH(id: Int, parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(id, parent, false)) {
        fun <T : View> findViewById(id: Int) : T {
            return this.itemView.findViewById(id)
        }
    }

    enum class DashboardItemTypes private constructor(val type: Int) {
        TOP_LAYOUT(0),
        LIST_ITEM(1) {};

        companion object {
            internal fun getByVal(type : Int) : DashboardItemTypes {
                for(item in DashboardItemTypes.values()) {
                    if(type == item.type) {
                        return item
                    }
                }
                return TOP_LAYOUT
            }
        }
    }
}