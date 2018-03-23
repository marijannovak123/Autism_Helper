package com.marijannovak.autismhelper.models

/**
 * Created by Marijan on 23.3.2018..
 */
data class User (
        private var name: String?,
        private var id: Int?,
        private var isAdmin: Boolean?
)

data class Answer (
        private var text: String?,
        private var isCorrect: Boolean?
)

data class Category(
        private var id: Int?,
        private var name: String?
)

data class Question(
        private var text: String?,
        private var id: Int?,
        private var categoryId: Int?,
        private var typeId: Int?,
        private var extraData: String?,
        private var answers : List<Answer>?
)

data class QuestionType(
        private var id: Int?,
        private var name: String?
)