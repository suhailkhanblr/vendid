package com.phoenix.vendido.buysell.premium

interface OnPremiumItemSelection {
    fun onItemSelection(isSelected : Boolean, cost : Int, isFor : String)
}
