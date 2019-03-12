package com.bylancer.classified.bylancerclassified.dashboard


import android.os.Bundle
import android.app.Fragment
import android.os.Handler
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.view.animation.Animation
import com.bylancer.classified.bylancerclassified.R
import com.bylancer.classified.bylancerclassified.fragments.BylancerBuilderFragment
import com.bylancer.classified.bylancerclassified.utils.GridSpacingItemDecoration
import com.bylancer.classified.bylancerclassified.webservices.RetrofitController
import com.bylancer.classified.bylancerclassified.webservices.productlist.ProductInputData
import com.bylancer.classified.bylancerclassified.webservices.productlist.ProductsData
import kotlinx.android.synthetic.main.fragment_dashboard.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.bylancer.classified.bylancerclassified.utils.AppConstants
import com.bylancer.classified.bylancerclassified.utils.Utility
import android.widget.Toast
import ir.mirrajabi.searchdialog.core.SearchResultListener
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat
import com.bylancer.classified.bylancerclassified.utils.LanguagePack
import android.graphics.Typeface
import kotlinx.android.synthetic.main.activity_dashboard.*

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DashboardFragment : BylancerBuilderFragment(), Callback<List<ProductsData>>, OnProductItemClickListener {
    val SPAN_COUNT = 2
    var animUpDown: Animation? = null

    override fun setLayoutView() = R.layout.fragment_dashboard

    override fun initialize(savedInstanceState: Bundle?) {
        dashboard_recycler_view.layoutManager = GridLayoutManager(context, SPAN_COUNT);
        dashboard_recycler_view.setHasFixedSize(false)
        dashboard_recycler_view.isNestedScrollingEnabled = false
        dashboard_recycler_view.addItemDecoration(GridSpacingItemDecoration(SPAN_COUNT, 10, false))

        dashboard_search_button.setOnClickListener() {showItemSearchBar()}
        animateProgressView()
        Handler().postDelayed({
            fetchProductList()
        }, 2000)
    }

    private fun showItemSearchBar() {
        var searchItemList = arrayListOf<DashboardSearchItemModel>(DashboardSearchItemModel(LanguagePack.getString("Motors")), DashboardSearchItemModel(LanguagePack.getString("Classifieds")),
                DashboardSearchItemModel(LanguagePack.getString("Property for Sale")), DashboardSearchItemModel(LanguagePack.getString("Property for Rent")), DashboardSearchItemModel(LanguagePack.getString("Jobs")),
                DashboardSearchItemModel(LanguagePack.getString("Community")))

        val simpleSearchDialogCompat = SimpleSearchDialogCompat(context, LanguagePack.getString("Search..."),
                LanguagePack.getString("What are you looking for?"), null, searchItemList,
                SearchResultListener<DashboardSearchItemModel> { dialog, item, position ->
                    if (item != null) {
                        productsBasedOnSearchBar(item.title!!)
                    }

                    dialog.dismiss()
                })
        simpleSearchDialogCompat.show()
        //simpleSearchDialogCompat.setOnDismissListener { Utility.hideKeyboardFromDialogs(activity!!) }
        val typeface = Typeface.createFromAsset(context?.assets,
                "fonts/roboto_italic.ttf")
        if (simpleSearchDialogCompat.titleTextView != null) {
            simpleSearchDialogCompat.titleTextView.setTextColor(resources.getColor(R.color.shadow_black))
        }

        if (simpleSearchDialogCompat.searchBox != null) {
            simpleSearchDialogCompat.searchBox.setTextColor(resources.getColor(R.color.shadow_black))
            simpleSearchDialogCompat.searchBox.typeface = typeface
            simpleSearchDialogCompat.searchBox.setHintTextColor(resources.getColor(R.color.light_gray))
        }
        if (simpleSearchDialogCompat.recyclerView != null && simpleSearchDialogCompat.recyclerView.findViewHolderForAdapterPosition(0) != null
                && simpleSearchDialogCompat.recyclerView.findViewHolderForAdapterPosition(0)!!.itemView  != null){
            if ((simpleSearchDialogCompat.recyclerView.findViewHolderForAdapterPosition(0)!!.itemView) is TextView) {
                val recyclerTextView = simpleSearchDialogCompat.recyclerView.findViewHolderForAdapterPosition(0)!!.itemView as TextView
                recyclerTextView.setTextColor(resources.getColor(R.color.shadow_black))
                recyclerTextView.typeface = typeface
            }
        }
    }

    private fun productsBasedOnSearchBar(searchedItem: String) {

    }

    private fun animateProgressView() {
        progress_view_dashboard_frame.visibility = View.VISIBLE
        animUpDown = AnimationUtils.loadAnimation(context,
                R.anim.up_down)
        animUpDown?.setRepeatCount(Animation.INFINITE)
        animUpDown?.setRepeatMode(Animation.INFINITE)
        animUpDown?.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                if(animUpDown != null)
                    progress_view_dashboard.startAnimation(animUpDown)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        progress_view_dashboard.startAnimation(animUpDown)
    }

    private fun fetchProductList() {
        val productInputData = ProductInputData()
        productInputData.countryCode = "in"
        productInputData.limit = "30"
        productInputData.pageNumber = "1"
        productInputData.status = "active"

        RetrofitController.fetchProducts(productInputData, this)
    }

    override fun onResponse(call: Call<List<ProductsData>>?, response: Response<List<ProductsData>>?) {
        if(this.isAdded && this.isVisible) {
            progress_view_dashboard_frame.visibility = View.GONE
            progress_view_dashboard.clearAnimation()
            animUpDown = null

            if(response != null && response.isSuccessful) {
                val productList: List<ProductsData> = response.body()
                if(productList != null) {
                    dashboard_recycler_view.adapter = DashboardItemAdapter(productList, this)
                } else {
                    Utility.showSnackBar(dashboard_fragment_parent_layout, "Work In Progress", context!!)
                }
            }
        }
    }

    override fun onFailure(call: Call<List<ProductsData>>?, t: Throwable?) {
        progress_view_dashboard_frame.visibility = View.GONE
        progress_view_dashboard.clearAnimation()
        animUpDown = null
    }

    override fun onProductItemClicked(productId: String?, productName: String?, userName: String?) {
        val bundle = Bundle()
        bundle.putString(AppConstants.PRODUCT_ID, productId)
        bundle.putString(AppConstants.PRODUCT_NAME, productName)
        bundle.putString(AppConstants.PRODUCT_OWNER_NAME, userName)
        startActivity(DashboardProductDetailActivity::class.java, bundle)
    }
}
