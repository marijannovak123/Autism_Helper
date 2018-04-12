package com.marijannovak.autismhelper.data.database.typeconverters

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.marijannovak.autismhelper.data.models.Child

class ChildConverter {

    @TypeConverter
    fun fromChildListToJson(childList: List<Child>) : String {
        val type = object:  TypeToken<List<Child>>() {}.type
        return Gson().toJson(childList, type)
    }

    @TypeConverter
    fun fromJsonToChildList(json: String) : List<Child> {
        val type = object: TypeToken<List<Child>>() {}.type
        return Gson().fromJson(json, type)
    }
}