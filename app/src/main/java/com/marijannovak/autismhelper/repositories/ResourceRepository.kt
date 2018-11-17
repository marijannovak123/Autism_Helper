package com.marijannovak.autismhelper.repositories

import com.marijannovak.autismhelper.App
import javax.inject.Inject

class ResourceRepository @Inject constructor(private val context: App) {
    fun getString(resId: Int, vararg formatValues: Any): String = context.getString(resId, *formatValues)
}