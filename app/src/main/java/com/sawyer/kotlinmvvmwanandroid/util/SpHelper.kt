package com.sawyer.kotlinmvvmwanandroid.util

import android.content.Context
import androidx.core.content.edit

private const val SP_WAN_ANDROID = "sp_wan_android"

@JvmOverloads
fun <T> getSpValue(
    filename: String = SP_WAN_ANDROID,
    context: Context,
    key: String,
    defaultVal: T
): T {
    val sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE)
    return when (defaultVal) {
        is Boolean -> sp.getBoolean(key, defaultVal) as T
        is String -> sp.getString(key, defaultVal) as T
        is Int -> sp.getInt(key, defaultVal) as T
        is Long -> sp.getLong(key, defaultVal) as T
        is Float -> sp.getFloat(key, defaultVal) as T
        is Set<*> -> sp.getStringSet(key, defaultVal as Set<String>) as T
        else -> throw IllegalArgumentException("Unrecognized default value $defaultVal")
    }
}

@JvmOverloads
fun <T> putSpValue(
    filename: String = SP_WAN_ANDROID,
    context: Context,
    key: String,
    value: T
) {
    context.getSharedPreferences(filename,Context.MODE_PRIVATE).edit {
        when (value) {
            is Boolean ->putBoolean(key, value)
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            is Set<*> -> putStringSet(key, value as Set<String>)
            else -> throw UnsupportedOperationException("Unrecognized value $value")
        }
    }
}

@JvmOverloads
fun removeSpValue(filename: String = SP_WAN_ANDROID, context: Context, key: String) {
    context.getSharedPreferences(filename, Context.MODE_PRIVATE).edit{ remove(key) }
}

@JvmOverloads
fun clearSpValue(filename: String = SP_WAN_ANDROID, context: Context) {
    context.getSharedPreferences(filename, Context.MODE_PRIVATE).edit{ clear() }
}