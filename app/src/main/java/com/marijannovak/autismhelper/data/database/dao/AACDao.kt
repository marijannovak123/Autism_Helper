package com.marijannovak.autismhelper.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.marijannovak.autismhelper.common.base.BaseDao
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_AAC
import com.marijannovak.autismhelper.data.models.AacPhrase
import io.reactivex.Flowable

@Dao
interface AACDao : BaseDao<AacPhrase> {

    @Query("SELECT * FROM $TABLE_AAC")
    fun getAllPhrases(): Flowable<List<AacPhrase>>

    @Query("SELECT * FROM $TABLE_AAC")
    suspend fun getPhrasesSuspend(): List<AacPhrase>

    @Query("SELECT * FROM $TABLE_AAC WHERE categoryId = :categoryId")
    fun getCategoryPhrases(categoryId: Int): Flowable<List<AacPhrase>>

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
        phrases.forEach {
            if(i < savedPhrases.size && savedPhrases.contains(it)) {
                update(it.id, it.name, savedPhrases[i].iconPath)
            } else {
                insert(it)
            }
            i = i.inc()
        }
    }
}