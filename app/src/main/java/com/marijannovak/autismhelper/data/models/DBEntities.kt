package com.marijannovak.autismhelper.data.models

import android.arch.persistence.room.*
import com.google.gson.annotations.SerializedName
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_AAC
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_ANSWERS
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_CATEGORIES
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_CHILDREN
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_CHILD_SCORES
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_QUESTIONS
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_SENTENCE
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
        var profilePicPath: String?,
        @Ignore
        var children: Map<String, Child>?,
        @Ignore
        @SerializedName("child_scores")
        var childScores: Map<String, ChildScore>?
) {
    constructor() : this("", "", "", "", "", emptyMap(), null)

    override fun equals(other: Any?): Boolean {
        return if (other !is User) {
            false
        } else {
            other.id == this.id && other.username == this.username && other.email == this.email && other.parentPassword == this.parentPassword
        }
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (username?.hashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (parentPassword?.hashCode() ?: 0)
        result = 31 * result + (profilePicPath?.hashCode() ?: 0)
        result = 31 * result + (children?.hashCode() ?: 0)
        result = 31 * result + (childScores?.hashCode() ?: 0)
        return result
    }
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
) : Serializable, Comparator<ChildScore> {
    override fun compare(o1: ChildScore, o2: ChildScore): Int {
        return (o1.timestamp - o2.timestamp).toInt()
    }

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
    constructor() : this(0, "", 0, 0, "", null, emptyList())

    override fun equals(other: Any?): Boolean {
        if(other is Question) {
            if(this.id == other.id && this.text == other.text && this.categoryId == other.categoryId) {
                return true
            }
        }
        return false
    }
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
        var text: String,
        var iconPath: String
) {
    constructor() : this(0, "", "", "")

    override fun equals(other: Any?): Boolean {
        if(other is AacPhrase) {
            if(this.id == other.id && this.name == other.name) {
                return true
            }
        }
        return false
    }
}

@Entity(tableName = TABLE_SENTENCE)
data class SavedSentence(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        var phrases: List<AacPhrase>
)

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
