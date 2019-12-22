package com.bylancer.classified.bylancerclassified.utils

interface LazyProductLoading {
    fun onProductLoadRequired(currentVisibleItem: Int)
}