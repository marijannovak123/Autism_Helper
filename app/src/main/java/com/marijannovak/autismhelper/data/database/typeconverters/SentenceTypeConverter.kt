package com.marijannovak.autismhelper.data.database.typeconverters

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.marijannovak.autismhelper.data.models.AacPhrase
import java.lang.reflect.Type

class SentenceTypeConverter {

    val type: Type = object: TypeToken<List<AacPhrase>>(){}.type

    @TypeConverter
    fun sentenceToString(phrases: List<AacPhrase>): String {
        return Gson().toJson(phrases, type)
    }

    @TypeConverter
    fun stringToSentence(string: String): List<AacPhrase> {
        return Gson().fromJson(string, type)
    }
}