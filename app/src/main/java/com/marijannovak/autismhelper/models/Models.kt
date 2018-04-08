package com.marijannovak.autismhelper.models

import android.arch.persistence.room.PrimaryKey


data class Child (
        @PrimaryKey
        var id: Int,
        var name: String?,
        var sex: String?,
        var parentId: String,
        var dateOfBirth: Long
)

data class Answer (
        var text: String?,
        var isCorrect: Boolean?,
        var questionId: Int?
)