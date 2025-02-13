package com.example.utampa

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/**
 * Recursively find the Activity from a given Context.
 */
fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}