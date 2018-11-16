package com.marijannovak.autismhelper.utils

data class Status (
        var state: State,
        var message: String? = null,
        var messageId: Int? = null
)