package com.phoenix.vendido.buysell.dashboard

interface OnProductItemClickListener {
    fun onProductItemClicked(productId: String?, productName: String?, userName: String?)
}
