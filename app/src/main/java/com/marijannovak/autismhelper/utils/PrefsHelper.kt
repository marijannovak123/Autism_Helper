package com.marijannovak.autismhelper.utils

import com.marijannovak.autismhelper.config.Constants.Companion.KEY_PARENT_PASSWORD
import com.marijannovak.autismhelper.config.Constants.Companion.KEY_SOUND_ON
import com.marijannovak.autismhelper.config.Constants.Companion.KEY_TTS_PITCH
import com.marijannovak.autismhelper.config.Constants.Companion.KEY_TTS_SPEED
import com.tumblr.remember.Remember

class PrefsHelper {

    fun getParentPassword() = Remember.getString(KEY_PARENT_PASSWORD, "")

    fun setParentPassword(password: String) {
        Remember.putString(KEY_PARENT_PASSWORD, password)
    }

    fun setSoundsOn(soundsOn: Boolean) {
        Remember.putBoolean(KEY_SOUND_ON, soundsOn)
    }

    fun isSoundOn() = Remember.getBoolean(KEY_SOUND_ON, true)

    fun setTtsSpeed(speed: Float) {
        Remember.putFloat(KEY_TTS_SPEED, speed)
    }

    fun getTtsSpeed() = Remember.getFloat(KEY_TTS_SPEED, 1f)

    fun setTtsPitch(pitch: Float) {
        Remember.getFloat(KEY_TTS_PITCH, pitch)
    }

    fun getTtsPitch() = Remember.getFloat(KEY_TTS_PITCH, 1f)
}