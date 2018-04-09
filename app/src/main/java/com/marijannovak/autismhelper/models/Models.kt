package com.marijannovak.autismhelper.models

import android.arch.persistence.room.PrimaryKey
import java.io.Serializable


data class Child (
        @PrimaryKey
        var id: String,
        var name: String?,
        var sex: String?,
        var parentId: String,
        var dateOfBirth: Long
) : Serializable

data class Answer (
        var text: String?,
        var isCorrect: Boolean?,
        var questionId: Int?
)