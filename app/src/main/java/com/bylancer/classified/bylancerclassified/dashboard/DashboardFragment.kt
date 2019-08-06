package com.bylancer.classified.bylancerclassified.dashboard


import android.os.Bundle
import android.app.Fragment
import android.content.Context
import android.os.Handler
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.view.animation.Animation
import com.bylancer.classified.bylancerclassified.R
import com.bylancer.classified.bylancerclassified.fragments.BylancerBuilderFragment
import com.bylancer.classified.bylancerclassified.webservices.RetrofitController
import com.bylancer.classified.bylancerclassified.webservices.productlist.ProductInputData
import com.bylancer.classified.bylancerclassified.webservices.productlist.ProductsData
import kotlinx.android.synthetic.main.fragment_dashboard.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.view.animation.AnimationUtils
import android.widget.TextView
import ir.mirrajabi.searchdialog.core.SearchResultListener
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat
import android.graphics.Typeface
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bylancer.classified.bylancerclassified.activities.SplashActivity
import com.bylancer.classified.bylancerclassified.appconfig.AppConfigDetail
import com.bylancer.classified.bylancerclassified.appconfig.SubCategory
import com.bylancer.classified.bylancerclassified.utils.*
import kotlinx.android.synthetic.main.dashboard_top_item_layout.*

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DashboardFragment : BylancerBuilderFragment(), Callback<List<ProductsData>>, OnProductItemClickListener {

    val SPAN_COUNT = 2
    var productPageNumber = 1
    var featuredProductPageNumber = 1
    var isProductDataLoading = false
    var animUpDown: Animation? = null
    val productDataList: ArrayList<ProductsData> = arrayListOf()
    val featuredDataList: ArrayList<ProductsData> = arrayListOf()

    override fun setLayoutView() = R.layout.fragment_dashboard

    override fun initialize(savedInstanceState: Bundle?) { //
        dashboard_category_menu_recycler_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        dashboard_category_menu_recycler_view.setHasFixedSize(false)
        if (productDataList.isEmpty()) {
            dashboard_category_menu_recycler_view.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation)
        }
        productDataList.clear()
        featuredDataList.clear()
        productPageNumber = 1
        featuredProductPageNumber = 1

        val parentActivity: DashboardActivity = (activity as DashboardActivity?)!!
        if (AppConfigDetail.category == null || dashboard_category_menu_recycler_view == null) {
            SessionState.instance.readValuesFromPreferences(context)
            startActivity(SplashActivity::class.java, true)
        }
        dashboard_category_menu_recycler_view.adapter = DashboardCategoryAdapter(AppConfigDetail.category!!, parentActivity)

        dashboard_featured_recycler_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        dashboard_featured_recycler_view.setHasFixedSize(false)
        dashboard_featured_recycler_view.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation)

        dashboard_recycler_view.layoutManager = GridLayoutManager(context, SPAN_COUNT);
        dashboard_recycler_view.setHasFixedSize(false)
        dashboard_recycler_view.isNestedScrollingEnabled = false
        dashboard_recycler_view.itemAnimator = DefaultItemAnimator()
        dashboard_recycler_view.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation)
        dashboard_recycler_view.addItemDecoration(GridSpacingItemDecoration(SPAN_COUNT, 10, false))
        initializingRecyclerViewScrollListener()

        featured_in_classified.text = LanguagePack.getString(getString(R.string.featured_ads))
        dashboard_search_button.text = LanguagePack.getString(getString(R.string.search_text))
        top_picks_in_classified.text = LanguagePack.getString(getString(R.string.top_picks_in_classified))

        dashboard_search_button.setOnClickListener() {showItemSearchBar()}
        animateProgressView()
        Handler().postDelayed({
            fetchFeaturedAndUrgentProductList()
        }, 2000)

        continue_browsing_parent_layout.setOnClickListener() {
            val bundle = Bundle()
            bundle.putString(AppConstants.SELECTED_CATEGORY_ID, SessionState.instance.continueBrowsingCategoryId)
            bundle.putString(AppConstants.SELECTED_SUB_CATEGORY_ID, SessionState.instance.continueBrowsingSubCategoryId)
            startActivity(ProductByCategoryActivity::class.java, bundle)
        }
    }

    private fun showItemSearchBar() {
        var searchItemList = arrayListOf<DashboardSearchItemModel>(DashboardSearchItemModel(LanguagePack.getString("Motors")), DashboardSearchItemModel(LanguagePack.getString("Classifieds")),
                DashboardSearchItemModel(LanguagePack.getString("Property for Sale")), DashboardSearchItemModel(LanguagePack.getString("Property for Rent")), DashboardSearchItemModel(LanguagePack.getString("Jobs")),
                DashboardSearchItemModel(LanguagePack.getString("Community")))

        if (AppConfigDetail.category != null) {
            searchItemList.clear()
            for (categoryName in AppConfigDetail.category!!) {
                searchItemList.add(DashboardSearchItemModel(LanguagePack.getString(categoryName.name!!)))
            }
        }

        val simpleSearchDialogCompat = SimpleSearchDialogCompat(context, LanguagePack.getString("Search..."),
                LanguagePack.getString("What are you looking for?"), null, searchItemList,
                SearchResultListener<DashboardSearchItemModel> { dialog, item, position ->
                    if (AppConfigDetail.category != null ) {
                        showSubCategoryInSearchBar(context!!, AppConfigDetail.category?.get(position)?.subCategory, AppConfigDetail.category?.get(position)?.id,
                                AppConfigDetail.category?.get(position)?.name, AppConfigDetail.category?.get(position)?.picture)
                    }

                    dialog.dismiss()
                })
        simpleSearchDialogCompat.show()

        simpleSearchDialogCompat.setOnDismissListener() {
            var searchedkeyWord = simpleSearchDialogCompat.searchBox.text
            if (!searchedkeyWord.isNullOrEmpty()) {

            }
        }

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

    private fun showSubCategoryInSearchBar(context: Context, subCategory: List<SubCategory>?, categoryId: String?, categoryName:String?, iconUrl: String?) {
        if (subCategory != null && categoryId != null && categoryName != null && iconUrl != null) {
            var searchItemList = arrayListOf<DashboardSearchItemModel>(DashboardSearchItemModel(LanguagePack.getString("All")), DashboardSearchItemModel(LanguagePack.getString("Property for Rent")), DashboardSearchItemModel(LanguagePack.getString("Jobs")),
                    DashboardSearchItemModel(LanguagePack.getString("Motors")), DashboardSearchItemModel(LanguagePack.getString("Classifieds")),
                    DashboardSearchItemModel(LanguagePack.getString("Property for Sale")), DashboardSearchItemModel(LanguagePack.getString("Property for Rent")), DashboardSearchItemModel(LanguagePack.getString("Jobs")),
                    DashboardSearchItemModel(LanguagePack.getString("Community")))

            if (subCategory != null) {
                searchItemList.clear()
                searchItemList.add(DashboardSearchItemModel(LanguagePack.getString("All")))
                for (subCategoryName in subCategory!!) {
                    searchItemList.add(DashboardSearchItemModel(LanguagePack.getString(subCategoryName.name!!)))
                }
            }

            val simpleSearchDialogCompat = SimpleSearchDialogCompat(context, LanguagePack.getString("Search..."),
                    LanguagePack.getString("What are you looking for?"), null, searchItemList,
                    SearchResultListener<DashboardSearchItemModel> { dialog, item, position ->
                        SessionState.instance.continueBrowsingText = categoryName + " | " + item.title!!
                        SessionState.instance.continueBrowsingCategoryId = categoryId
                        SessionState.instance.continueBrowsingImage = iconUrl
                        SessionState.instance.continueBrowsingSubCategoryId = if (position == 0) "0" else subCategory.get(position - 1).id!!
                        SessionState.instance.saveValuesToPreferences(context, AppConstants.Companion.PREFERENCES.CONTINUE_BROWSING_TEXT.toString(), SessionState.instance.continueBrowsingText)
                        SessionState.instance.saveValuesToPreferences(context, AppConstants.Companion.PREFERENCES.CONTINUE_BROWSING_CATEGORY_ID.toString(), SessionState.instance.continueBrowsingCategoryId)
                        SessionState.instance.saveValuesToPreferences(context, AppConstants.Companion.PREFERENCES.CONTINUE_BROWSING_SUB_CATEGORY_ID.toString(), SessionState.instance.continueBrowsingSubCategoryId)
                        SessionState.instance.saveValuesToPreferences(context, AppConstants.Companion.PREFERENCES.CONTINUE_BROWSING_IMAGE.toString(), SessionState.instance.continueBrowsingImage)
                        if (!subCategory.isNullOrEmpty()) {
                            val bundle = Bundle()
                            bundle.putString(AppConstants.SELECTED_CATEGORY_ID, categoryId)
                            val subCategoryId: String = if (position == 0) "0" else subCategory.get(position - 1).id!!
                            bundle.putString(AppConstants.SELECTED_SUB_CATEGORY_ID, subCategoryId)
                            startActivity(ProductByCategoryActivity::class.java, bundle)
                        }

                        dialog.dismiss()
                    })
            simpleSearchDialogCompat.show()
            //simpleSearchDialogCompat.setOnDismissListener { Utility.hideKeyboardFromDialogs(activity!!) }
            val typeface = Typeface.createFromAsset(context?.assets,
                    "fonts/roboto_italic.ttf")
            if (simpleSearchDialogCompat.titleTextView != null) {
                simpleSearchDialogCompat.titleTextView.setTextColor(context.resources.getColor(R.color.shadow_black))
            }

            if (simpleSearchDialogCompat.searchBox != null) {
                simpleSearchDialogCompat.searchBox.setTextColor(context.resources.getColor(R.color.shadow_black))
                simpleSearchDialogCompat.searchBox.typeface = typeface
                simpleSearchDialogCompat.searchBox.setHintTextColor(context.resources.getColor(R.color.light_gray))
            }
            if (simpleSearchDialogCompat.recyclerView != null && simpleSearchDialogCompat.recyclerView.findViewHolderForAdapterPosition(0) != null
                    && simpleSearchDialogCompat.recyclerView.findViewHolderForAdapterPosition(0)!!.itemView  != null){
                if ((simpleSearchDialogCompat.recyclerView.findViewHolderForAdapterPosition(0)!!.itemView) is TextView) {
                    val recyclerTextView = simpleSearchDialogCompat.recyclerView.findViewHolderForAdapterPosition(0)!!.itemView as TextView
                    recyclerTextView.setTextColor(context.resources.getColor(R.color.shadow_black))
                    recyclerTextView.typeface = typeface
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (SessionState.instance.continueBrowsingSubCategoryId.isNullOrEmpty()) {
            continue_browsing_parent_layout.visibility = View.GONE
        } else {
            continue_browsing_text_view.text = LanguagePack.getString(getString(R.string.continue_browsing))
            browsing_path_text_view.text = SessionState.instance.continueBrowsingText
            Glide.with(context!!).load(SessionState.instance.continueBrowsingImage).into(continue_browsing_item_image_view)
        }
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

    private fun fetchFeaturedAndUrgentProductList() {
        isProductDataLoading = true
        val productInputData = ProductInputData()
        productInputData.countryCode = SessionState.instance.selectedCountryCode
        productInputData.limit = AppConstants.PRODUCT_LOADING_LIMIT
        productInputData.pageNumber = featuredProductPageNumber.toString()
        productInputData.status = AppConstants.PRODUCT_STATUS

        RetrofitController.fetchFeaturedAndUrgentProducts(productInputData, object : Callback<List<ProductsData>> {
            override fun onFailure(call: Call<List<ProductsData>>?, t: Throwable?) {
                fetchProductList()
            }

            override fun onResponse(call: Call<List<ProductsData>>?, response: Response<List<ProductsData>>?) {
                if(this@DashboardFragment.isAdded && this@DashboardFragment.isVisible) {
                    if(response != null && response.isSuccessful) {
                        featuredDataList.addAll(response.body())
                        if (featuredDataList != null) {
                            featured_text_layout.visibility = View.VISIBLE
                            if (dashboard_featured_recycler_view.adapter != null) {
                                dashboard_featured_recycler_view.adapter?.notifyDataSetChanged()
                            } else  {
                                dashboard_featured_recycler_view.adapter = DashboardFeaturedItemAdapter(featuredDataList, this@DashboardFragment)
                            }
                        }
                    }
                }

                fetchProductList()
            }

        })
    }

    private fun fetchProductList() {
        isProductDataLoading = true
        val productInputData = ProductInputData()
        productInputData.countryCode = SessionState.instance.selectedCountryCode
        productInputData.limit = AppConstants.PRODUCT_LOADING_LIMIT
        productInputData.pageNumber = productPageNumber.toString()
        productInputData.status = AppConstants.PRODUCT_STATUS

        RetrofitController.fetchProducts(productInputData, this@DashboardFragment)
    }

    override fun onResponse(call: Call<List<ProductsData>>?, response: Response<List<ProductsData>>?) {
        if(this.isAdded && this.isVisible) {
            progress_view_dashboard_frame.visibility = View.GONE
            progress_view_dashboard.clearAnimation()
            animUpDown = null

            if(response != null && response.isSuccessful) {
                productDataList.addAll(response.body())
                if (productDataList != null) {
                    top_picks_layout.visibility = View.VISIBLE
                    if (dashboard_recycler_view.adapter != null) {
                        dashboard_recycler_view.adapter?.notifyDataSetChanged()
                    } else  {
                        dashboard_recycler_view.adapter = DashboardItemAdapter(productDataList, this)
                    }
                } else {
                    Utility.showSnackBar(dashboard_fragment_parent_layout, getString(R.string.internet_issue), context!!)
                }
            }
        }

        isProductDataLoading = false
    }

    override fun onFailure(call: Call<List<ProductsData>>?, t: Throwable?) {
        if (progress_view_dashboard_frame != null) progress_view_dashboard_frame.visibility = View.GONE
        if (progress_view_dashboard != null) progress_view_dashboard.clearAnimation()
        animUpDown = null
        isProductDataLoading = false
    }

    override fun onProductItemClicked(productId: String?, productName: String?, userName: String?) {
        val bundle = Bundle()
        bundle.putString(AppConstants.PRODUCT_ID, productId)
        bundle.putString(AppConstants.PRODUCT_NAME, productName)
        bundle.putString(AppConstants.PRODUCT_OWNER_NAME, userName)
        startActivity(DashboardProductDetailActivity::class.java, bundle)
    }

    private fun initializingRecyclerViewScrollListener() {
        dashboard_nested_scroll_view.initNestedScrollListener(object : LazyProductLoading {
            override fun onProductLoadRequired(currentVisibleItem: Int) {
                val itemSizeForLazyLoading = productDataList.size / 2
                if (!isProductDataLoading && productDataList != null && currentVisibleItem >= itemSizeForLazyLoading) {
                    productPageNumber += 1
                    fetchProductList()
                }
            }
        })

        dashboard_featured_recycler_view.initScrollListener(object : LazyProductLoading {
            override fun onProductLoadRequired(currentVisibleItem: Int) {
                val itemSizeForLazyLoading = featuredDataList.size / 2
                if (!isProductDataLoading && featuredDataList != null && currentVisibleItem >= itemSizeForLazyLoading) {
                    featuredProductPageNumber += 1
                    fetchFeaturedAndUrgentProductList()
                }
            }
        })
    }
}
