package com.marijannovak.autismhelper.utils

import com.marijannovak.autismhelper.data.models.*

class TestDataGenerator {

    companion object {

        fun createUserApiResponse() : User {
            val parentId = "002445"
            val child1 = Child("hehe1", parentId, "Roko", "M", 12312313)
            val child2 = child1.copy(sex = "F", name= "Rokica", id = "hehe2")
            return User("Nikica", parentId, "nikica@gmail.com", listOf(child1, child2))
        }

        fun createQuestionApiResponse(): List<Question> {
            val questionId = 123
            val answer1 = Answer(0, "hmm", true, questionId)
            val answer2 = Answer(1, "hmhm", false, questionId)
            val question = Question("Hmm?", questionId, 433, 322, "", listOf(answer1, answer2))
            return listOf(question)
        }

        fun createCategoriesApiResponse() : List<Category> {
            return emptyList()
        }

        fun createQuestionTypeApiResponse() : List<QuestionType> {
            return emptyList()
        }

        fun createChildScoresApiResponse(): List<ChildScore> {
            return emptyList()
        }
    }
}