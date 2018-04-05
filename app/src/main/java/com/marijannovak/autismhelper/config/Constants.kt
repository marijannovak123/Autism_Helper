package com.marijannovak.autismhelper.config

/**
 * Created by Marijan on 23.3.2018..
 */
class Constants {
    companion object {

        //region firebase rest api
        const val BASE_URL = "https://autism-helper.firebaseio.com/api/"
        const val FIREBASE_AUTH = "auth"
        const val FIREBASE_API_KEY = "2Sm3l55sGpTgeojlAnyREqj5KOwcks1kx379XidA"
        const val RESULT_CODE_GOOGLE_SIGNIN = 9001;
        //endregion

        //region local db
        const val DB_NAME = "autism-helper-db"
        const val TABLE_USER = "user"
        const val TABLE_ANSWERS = "answers"
        const val TABLE_QUESTIONS = "questions"
        const val TABLE_QUESTION_TYPES = "question_types"
        const val TABLE_CATEGORIES = "categories"
        //endregion

        //region validation
        const val VALIDATION_EMAIL = "email"
        const val VALIDATION_PASSWORD = "password"
        const val VALIDATION_DATE = "date"
        const val VALIDATION_USERNAME = "username"
        //endregion
    }
}