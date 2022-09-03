package com.phoenix.vendido.buysell.database

import androidx.room.*
import com.phoenix.vendido.buysell.dashboard.DashboardDetailModel

@Dao
public interface DaoAccess {

    @Insert
    fun insertProperty (property: DashboardDetailModel)

    @Query("SELECT COUNT(*) FROM DashboardDetailModel WHERE productId = :propertyId")
    fun checkPropertyExist(propertyId: String) : Int

    @Query("SELECT * FROM DashboardDetailModel")
    fun fetchAllProperties(): List<DashboardDetailModel>

    @Update
    fun updateProperty(property: DashboardDetailModel)

    @Delete
    fun deleteProperty(property: DashboardDetailModel)
}