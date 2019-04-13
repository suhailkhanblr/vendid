package com.bylancer.classified.bylancerclassified.database

import android.arch.persistence.room.TypeConverter
import com.bylancer.classified.bylancerclassified.dashboard.CustomData
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson


class DataConverterListImages {

    @TypeConverter
    fun fromStringList(customData: List<String>?): String? {
        if (customData == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<String>>() {

        }.type
        return gson.toJson(customData, type)
    }

    @TypeConverter
    fun toStringList(customDataString: String?): List<String>? {
        if (customDataString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<String>>() {

        }.type
        return gson.fromJson(customDataString, type)
    }
}