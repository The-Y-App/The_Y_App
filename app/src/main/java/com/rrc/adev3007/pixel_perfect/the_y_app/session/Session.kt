package com.rrc.adev3007.pixel_perfect.the_y_app.session

import android.content.Context
import android.content.SharedPreferences


class Session private constructor(context: Context?) {
    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = context!!.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun putString(key: String?, value: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String?, defaultValue: String?): String? {
        return sharedPreferences.getString(key, defaultValue)
    }

    fun putBoolean(key: String?, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }
    
    fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun clearKey(key: String?) {
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }

    fun clearSession() {
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        private const val PREFS_NAME = "MyPrefs"
        private var instance: Session? = null
        @Synchronized
        fun getInstance(context: Context?): Session? {
            if (instance == null) {
                instance = Session(context)
            }
            return instance
        }
    }
}
