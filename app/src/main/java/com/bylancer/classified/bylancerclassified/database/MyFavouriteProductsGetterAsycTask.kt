package com.bylancer.classified.bylancerclassified.database

import android.content.Context
import android.os.AsyncTask
import android.support.v7.widget.AppCompatImageView
import android.view.View
import com.bylancer.classified.bylancerclassified.R
import com.bylancer.classified.bylancerclassified.dashboard.DashboardDetailModel
import com.bylancer.classified.bylancerclassified.settings.FetchAllSavedProduct
import com.bylancer.classified.bylancerclassified.utils.LanguagePack
import com.bylancer.classified.bylancerclassified.utils.Utility

class MyFavouriteProductsGetterAsycTask(val context: Context, val fetchAllSavedProduct: FetchAllSavedProduct): AsyncTask<Void, Void, List<DashboardDetailModel>>() {

    override fun doInBackground(vararg p0: Void): List<DashboardDetailModel> {
        return DBUtil.getDatabaseInstance(context).daoAccess().
                    fetchAllProperties()
    }

    override fun onPostExecute(result: List<DashboardDetailModel>) {
        super.onPostExecute(result)
        if (fetchAllSavedProduct != null) {
            fetchAllSavedProduct.onAllMyFavoriteProductsFetched(result)
        }
    }

}