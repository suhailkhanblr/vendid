package com.phoenix.vendido.buysell.uploadproduct

interface OnUploadCategorySelection {
    fun onCategorySelected(categoryId: String, position: Int)
}