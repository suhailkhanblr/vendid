package com.bylancer.classified.bylancerclassified.settings

import com.bylancer.classified.bylancerclassified.dashboard.DashboardDetailModel

interface FetchAllSavedProduct {
    fun onAllMyFavoriteProductsFetched(savedProductList: List<DashboardDetailModel>)
}