package com.marijannovak.autismhelper.data.models

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.Relation
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_ANSWERS
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_CATEGORIES
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_CHILDREN
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_CHILD_SCORES
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_QUESTIONS
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_QUESTION_TYPES
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_USER

/**
 * Created by Marijan on 23.3.2018..
 */
@Entity(tableName = TABLE_USER)
class User (
    var username: String? = "",
    @PrimaryKey
    var id: String = "",
    var email: String? = ""
)

@Entity(tableName = TABLE_CHILDREN)
class Child (
        @PrimaryKey(autoGenerate = true)
        var id: String = "",
        var parentId: String = "",
        var name: String? = "",
        var sex: String? = "",
        var dateOfBirth: Long = 0
)


@Entity(tableName = TABLE_CHILD_SCORES)
class ChildScore (
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        var childId: Int = 0,
        var timestamp: Long = 0,
        var score: Long = 0
)

@Entity(tableName = TABLE_CATEGORIES)
class Category (
         @PrimaryKey(autoGenerate = true)
         var id: Int = 0,
         var name: String? = ""
)

@Entity(tableName = TABLE_QUESTIONS)
class Question (
         var text: String? = "",
         @PrimaryKey(autoGenerate = true)
         var id: Int = 0,
         var categoryId: Int? = 0,
         var typeId: Int? = 0,
         var extraData: String? = ""
)

@Entity(tableName = TABLE_ANSWERS)
class Answer (
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        var text: String? = "",
        var isCorrect: Boolean? = false,
        var questionId: Int = 0
)

@Entity(tableName = TABLE_QUESTION_TYPES)
class QuestionType(
         @PrimaryKey(autoGenerate = true)
         var id: Int,
         var name: String?
)

class UserChildrenJoin {
    @Embedded
    var user: User? = null
    @Relation(parentColumn = "id", entityColumn = "parentId")
    var children: List<Child> = ArrayList()

}


class QuestionAnswersJoin {
    @Embedded
    var question: Question? = null
    @Relation(parentColumn = "id", entityColumn = "questionId")
    var answers: List<Answer> = ArrayList()
}