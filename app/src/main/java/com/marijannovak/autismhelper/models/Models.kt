package com.marijannovak.autismhelper.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_ANSWERS
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_CATEGORIES
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
         var email : String?
)

@Entity(tableName = TABLE_ANSWERS)
data class Answer (
         @PrimaryKey
         var id : Int?,
         var text: String?,
         var isCorrect: Boolean?,
         var questionId: String?
)

@Entity(tableName = TABLE_CATEGORIES)
data class Category(
         @PrimaryKey
         var id: Int?,
         var name: String?
)

@Entity(tableName = TABLE_QUESTIONS)
data class Question(
         var text: String?,
         @PrimaryKey
         var id: Int?,
         var categoryId: Int?,
         var typeId: Int?,
         var extraData: String?
)

@Entity(tableName = TABLE_QUESTION_TYPES)
data class QuestionType(
         @PrimaryKey
         var id: Int?,
         var name: String?
)