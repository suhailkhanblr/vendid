package com.phoenix.vendido.buysell.settings

import com.phoenix.vendido.buysell.dashboard.DashboardDetailModel

interface FetchAllSavedProduct {
    fun onAllMyFavoriteProductsFetched(savedProductList: List<DashboardDetailModel>)
}