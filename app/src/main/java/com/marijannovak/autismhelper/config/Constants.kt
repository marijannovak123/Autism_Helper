package com.marijannovak.autismhelper.config

/**
 * Created by Marijan on 23.3.2018..
 */
class Constants {
    companion object {
        //region api
        const val BASE_URL = "https://autism-helper.firebaseio.com/api/"
        const val FIREBASE_AUTH = "auth"
        const val FIREBASE_API_KEY = "2Sm3l55sGpTgeojlAnyREqj5KOwcks1kx379XidA"
        const val RSS_URL = "https://www.npr.org/rss/rss.php?id=136582388"
        const val RETROFIT_JSON = "jsonfit"
        const val RETROFIT_XML = "xmlfit"
        const val API_JSON = "jsonapi"
        const val API_XML = "xmlapi"
        //endregion

        //region login/signup
        const val RESULT_CODE_GOOGLE_SIGNIN = 9001
        const val RESULT_CODE_SIGNUP = 9002
        const val REQUEST_CODE_IMAGE_LOADED = 9003
        const val KEY_SIGNUP_REQUEST = "signup_request"
        //endregion

        //region local db
        const val DB_NAME = "autism_helper_db"
        const val TABLE_USER = "user"
        const val TABLE_CHILD_SCORES = "child_scores"
        const val TABLE_QUESTIONS = "questions"
        const val TABLE_CATEGORIES = "categories"
        const val TABLE_CHILDREN = "children"
        const val TABLE_ANSWERS = "answers"
        const val TABLE_AAC = "aac"
        const val TABLE_RSS = "rss"
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
        const val KEY_PARENT_PASSWORD = "parent_password"
        const val KEY_SOUND_ON = "sound_on"
        const val KEY_TTS_PITCH = "tts_pitch"
        const val KEY_TTS_SPEED = "tts_speed"
        //endregion

        //region extras
        const val EXTRA_CATEGORY_ID = "category_id"
        const val EXTRA_CHILD = "child"
        //endregion

        //region fragments
        const val FRAGMENT_PROFILE = "profile_fragment"
        const val FRAGMENT_CHILDREN = "children_fragment"
        const val FRAGMENT_PHRASES = "phrases_fragment"
        const val FRAGMENT_RSS = "rss_fragment"
        //endregion

        //region other
        const val FALSE = "False"
        const val CORRECT = "Correct"
        const val FINGERPRINT_KEY = "fingerprintkey123321"
        //endregion
    }
}