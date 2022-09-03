package com.phoenix.vendido.buysell.uploadproduct.subcategoryselection

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import android.view.View
import com.phoenix.vendido.buysell.R
import com.phoenix.vendido.buysell.activities.BylancerBuilderActivity
import com.phoenix.vendido.buysell.appconfig.AppConfigDetail
import com.phoenix.vendido.buysell.uploadproduct.OnUploadCategorySelection
import com.phoenix.vendido.buysell.uploadproduct.categoryselection.UploadSubCategoryAdapter
import com.phoenix.vendido.buysell.uploadproduct.postdetails.UploadSelectATitleActivity
import com.phoenix.vendido.buysell.utils.AppConstants
import com.phoenix.vendido.buysell.utils.GridSpacingItemDecoration
import com.phoenix.vendido.buysell.utils.LanguagePack
import com.phoenix.vendido.buysell.utils.Utility
import kotlinx.android.synthetic.main.activity_upload_sub_category_selection.*

class UploadSubCategorySelectionActivity : BylancerBuilderActivity(), OnUploadCategorySelection, View.OnClickListener {
    val SPAN_COUNT = 2
    var categoryPosition = 0
    var SELECT_TITLE_REQUEST = 102

    override fun setLayoutView() = R.layout.activity_upload_sub_category_selection

    override fun initialize(savedInstanceState: Bundle?) {
        sub_category_selection_title_text_view.text = LanguagePack.getString(getString(R.string.select_sub_category))
        sub_category_selection_sub_title_text_view.text = LanguagePack.getString(getString(R.string.choose_sub_category_in_upload))
        sub_category_selection_recycler_view.layoutManager = GridLayoutManager(this, SPAN_COUNT)
        sub_category_selection_recycler_view.setHasFixedSize(false)
        sub_category_selection_recycler_view.addItemDecoration(GridSpacingItemDecoration(SPAN_COUNT, 10, false))

        if (AppConfigDetail.category != null && intent != null) {
            val bundle = intent.getBundleExtra(AppConstants.BUNDLE)
            if (bundle != null) {
                var categoryId = bundle.getString(AppConstants.SELECTED_CATEGORY_ID)
                categoryPosition = bundle.getInt(AppConstants.SELECTED_CATEGORY_POSITION)
                sub_category_selection_recycler_view.adapter = UploadSubCategoryAdapter(AppConfigDetail.category!!.get(categoryPosition).subCategory!!, this)
            }
        } else {
            Utility.showSnackBar(upload_category_parent_layout, getString(R.string.some_wrong), this)
        }
    }

    override fun onCategorySelected(subCategoryId: String, position: Int) {
        val bundle = Bundle()
        bundle.putInt(AppConstants.SELECTED_CATEGORY_POSITION, categoryPosition)
        bundle.putString(AppConstants.SELECTED_SUB_CATEGORY_ID, subCategoryId)
        startActivityForResult(UploadSelectATitleActivity::class.java, false, bundle, SELECT_TITLE_REQUEST)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.close_sub_category_selection_image_view -> onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_TITLE_REQUEST && resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
