package com.phoenix.vendido.buysell.premium

interface OnPremiumDoneButtonClicked {
    fun onPremiumDoneButtonClicked(totalCost : String, premiumFeatures : Array<String>)
}
