package com.bylancer.classified.bylancerclassified.database

import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Database
import com.bylancer.classified.bylancerclassified.dashboard.DashboardDetailModel


@Database(entities = [DashboardDetailModel::class], version = 1, exportSchema = false)
abstract class FavPropertyDatabase : RoomDatabase() {
    abstract fun daoAccess(): DaoAccess
}