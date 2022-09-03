package com.phoenix.vendido.buysell.database

import androidx.room.RoomDatabase
import androidx.room.Database
import com.phoenix.vendido.buysell.dashboard.DashboardDetailModel
import com.phoenix.vendido.buysell.utils.AppConstants


@Database(entities = [DashboardDetailModel::class], version = AppConstants.DATABASE_VERSION, exportSchema = false)
abstract class FavPropertyDatabase : RoomDatabase() {
    abstract fun daoAccess(): DaoAccess
}