package com.marijannovak.autismhelper.database.typeconverters

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.marijannovak.autismhelper.models.Answer

class AnswerConverter {

    @TypeConverter
    fun fromAnswerListToJson(answerList: List<Answer>) : String {
        val type = object:  TypeToken<List<Answer>>() {}.type
        return Gson().toJson(answerList, type)
    }

    @TypeConverter
    fun fromJsonToAnswerList(json: String) : List<Answer> {
        val type = object: TypeToken<List<Answer>>() {}.type
        return Gson().fromJson(json, type)
    }

}