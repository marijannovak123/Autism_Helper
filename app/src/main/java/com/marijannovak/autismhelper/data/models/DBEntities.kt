package com.marijannovak.autismhelper.data.models

import android.arch.persistence.room.*
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_AAC
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_ANSWERS
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_CATEGORIES
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_CHILDREN
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_CHILD_SCORES
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_QUESTIONS
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_USER
import java.io.Serializable

/**
 * Created by Marijan on 23.3.2018..
 */

@Entity(tableName = TABLE_USER)
data class User(
        @PrimaryKey
        var id: String,
        var username: String?,
        var email: String?,
        var parentPassword: String?,
        @Ignore
        var children: List<Child>?,
        @Ignore
        var childScoresResponse: ChildScoresResponse?
) {
    constructor() : this("", "", "", "", emptyList(), null)
}

@Entity(tableName = TABLE_CHILDREN)
data class Child(
        @PrimaryKey
        var id: String,
        var parentId: String,
        var name: String,
        var gender: String,
        var dateOfBirth: Long
) : Serializable {
    constructor() : this("", "", "", "", 0)
}

@Entity(tableName = TABLE_CHILD_SCORES)
data class ChildScore(
        @PrimaryKey
        var id: Int,
        var childId: String,
        var parentId: String,
        var timestamp: Long,
        var duration: Long,
        var mistakes: Int
) : Serializable {
    constructor() : this(0, "", "", 0, 0, 0)
}

@Entity(tableName = TABLE_CATEGORIES)
data class Category(
        @PrimaryKey
        var id: Int,
        var name: String,
        @Ignore
        var questionsWithAnswers: List<QuestionAnswersJoin>
) {
    constructor() : this(0, "", ArrayList())
}

@Entity(tableName = TABLE_QUESTIONS)
data class Question(
        @PrimaryKey
        var id: Int,
        var text: String,
        var categoryId: Int,
        var typeId: Int,
        var extraData: String?,
        var imgPath: String?,
        @Ignore
        var answers: List<Answer>
) {
    constructor() : this(0, "", 0, 0, "", null, ArrayList())
}

@Entity(tableName = TABLE_ANSWERS)
data class Answer(
        @PrimaryKey(autoGenerate = true)
        var id: Int,
        var text: String,
        var isCorrect: Boolean,
        var questionId: Int
) {
    constructor() : this(0, "", false, 0)
}

@Entity(tableName = TABLE_AAC)
data class AacPhrase(
        @PrimaryKey
        var id: Int,
        var name: String,
        var iconPath: String
) {
    constructor() : this(0, "", "")
}

class UserChildrenJoin : Serializable {
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

class CategoryQuestionsAnswersJoin {
    @Embedded
    var category: Category = Category()
    @Relation(parentColumn = "id", entityColumn = "categoryId", entity = Question::class)
    var questionsAnswers: List<QuestionAnswersJoin> = ArrayList()
}