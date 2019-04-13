package com.bylancer.classified.bylancerclassified.database

import android.arch.persistence.room.Room
import android.content.Context
import com.bylancer.classified.bylancerclassified.utils.AppConstants

class DBUtil {
    companion object {
        var mPropertyDatabase: FavPropertyDatabase? = null

        fun getDatabaseInstance(context: Context): FavPropertyDatabase {
            if (mPropertyDatabase == null) {
                mPropertyDatabase = Room.databaseBuilder(context,
                        FavPropertyDatabase::class.java, AppConstants.DATABASE_NAME)
                        .build()
            }

            return mPropertyDatabase!!
        }
    }

}