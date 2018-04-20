package com.marijannovak.autismhelper.data.models

import android.arch.persistence.room.*
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
data class User (
    var username: String? = "",
    @PrimaryKey
    var id: String = "",
    var email: String? = "",
    @Ignore
    var children: List<Child>?
) {
    constructor() : this("", "", "", ArrayList())
}

@Entity(tableName = TABLE_CHILDREN)
data class Child (
        @PrimaryKey
        var id: String = "",
        var parentId: String = "",
        var name: String? = "",
        var sex: String? = "",
        var dateOfBirth: Long = 0
)


@Entity(tableName = TABLE_CHILD_SCORES)
data class ChildScore (
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        var childId: Int = 0,
        var timestamp: Long = 0,
        var score: Long = 0
)

@Entity(tableName = TABLE_CATEGORIES)
data class Category (
         @PrimaryKey(autoGenerate = true)
         var id: Int = 0,
         var name: String? = ""
)

@Entity(tableName = TABLE_QUESTIONS)
data class Question (
         var text: String? = "",
         @PrimaryKey(autoGenerate = true)
         var id: Int = 0,
         var categoryId: Int? = 0,
         var typeId: Int? = 0,
         var extraData: String? = "",
         @Ignore
         var answers: List<Answer>
){
    constructor() : this("", 0, 0, 0, "", ArrayList())
}

@Entity(tableName = TABLE_ANSWERS)
data class Answer (
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        var text: String? = "",
        var isCorrect: Boolean? = false,
        var questionId: Int = 0
)

@Entity(tableName = TABLE_QUESTION_TYPES)
data class QuestionType(
         @PrimaryKey(autoGenerate = true)
         var id: Int,
         var name: String?
)

class UserChildrenJoin {
    @Embedded
    var user: User = User()
    @Relation(parentColumn = "id", entityColumn = "parentId")
    var children: List<Child> = ArrayList()

}


class QuestionAnswersJoin {
    @Embedded
    var question: Question = Question()
    @Relation(parentColumn = "id", entityColumn = "questionId")
    var answers: List<Answer> = ArrayList()
}