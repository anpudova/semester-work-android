package com.itis.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi

class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)


    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    fun saveDataString(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    fun saveDataLong(key: String, value: Long) {
        val editor = sharedPreferences.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    fun deleteData() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    fun getDataString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun getDataLong(key: String, defaultValue: Long): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

}