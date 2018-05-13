package com.marijannovak.autismhelper.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import com.marijannovak.autismhelper.common.base.BaseDao
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_AAC
import com.marijannovak.autismhelper.data.models.AacPhrase
import io.reactivex.Flowable

@Dao
interface AACDao : BaseDao<AacPhrase> {

    @Query("SELECT * FROM $TABLE_AAC")
    fun getAllPhrases(): Flowable<List<AacPhrase>>

    @Query("DELETE FROM $TABLE_AAC")
    fun deleteTable()

    @Query("UPDATE $TABLE_AAC SET name = :name, iconPath = :iconPath WHERE id = :id")
    fun update(id: Int, name: String, iconPath: String)

    @Query("SELECT * FROM $TABLE_AAC")
    fun queryAll(): List<AacPhrase>

    @Transaction
    fun updateMultiple(phrases: List<AacPhrase>){
        val savedPhrases = queryAll()
        var i = 0
        for(phrase: AacPhrase in phrases) {
            if(i < savedPhrases.size && savedPhrases.contains(phrase)) {
                update(phrase.id, phrase.name, savedPhrases[i].iconPath)
            } else {
                insert(phrase)
            }
            i = i.inc()
        }
    }
}