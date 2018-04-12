package com.marijannovak.autismhelper.data.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_CATEGORIES
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_CHILD_SCORES
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_QUESTIONS
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_QUESTION_TYPES
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_USER

/**
 * Created by Marijan on 23.3.2018..
 */
@Entity(tableName = TABLE_USER)
data class User (
    var username: String?,
    @PrimaryKey
    var id: String,
    var email: String?,
    var children: List<Child>
)

@Entity(tableName = TABLE_CHILD_SCORES)
data class ChildScore (
        @PrimaryKey(autoGenerate = true)
        var id: Int,
        var childId: Int,
        var timestamp: Long,
        var score: Long
)

@Entity(tableName = TABLE_CATEGORIES)
data class Category(
         @PrimaryKey
         var id: Int?,
         var name: String?
)

@Entity(tableName = TABLE_QUESTIONS)
data class Question (
         var text: String?,
         @PrimaryKey
         var id: Int?,
         var categoryId: Int?,
         var typeId: Int?,
         var extraData: String?,
         var answers : List<Answer>
)

@Entity(tableName = TABLE_QUESTION_TYPES)
data class QuestionType(
         @PrimaryKey
         var id: Int?,
         var name: String?
)