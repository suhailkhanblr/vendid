package com.phoenix.vendido.buysell.utils

interface LazyProductLoading {
    fun onProductLoadRequired(currentVisibleItem: Int)
}