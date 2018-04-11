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
        //endregion

        //region login/signup
        const val RESULT_CODE_GOOGLE_SIGNIN = 9001
        const val RESULT_CODE_SIGNUP = 9002
        const val KEY_SIGNUP_REQUEST = "signup_request"
        //endregion

        //region local db
        const val DB_NAME = "autism_helper_db"
        const val TABLE_USER = "user"
        const val TABLE_CHILD_SCORES = "child_scores"
        const val TABLE_QUESTIONS = "questions"
        const val TABLE_QUESTION_TYPES = "question_types"
        const val TABLE_CATEGORIES = "categories"
        //endregion

        //region validation
        const val VALIDATION_EMAIL = "email"
        const val VALIDATION_PASSWORD = "password"
        const val VALIDATION_DATE = "date"
        const val VALIDATION_USERNAME = "username"
        const val VALIDATION_NAME = "name"
        //endregion

        //region child related constants
        const val CHILD_ID_SUFFIX = "CHILD"
        val GENDERS = listOf("Male", "Female")
        //endregion

        //region shared prefs
        const val PREFS_NAME = "autism_helper_prefs"
        const val KEY_LOGGED_IN = "logged_in"
        //endregion
    }
}