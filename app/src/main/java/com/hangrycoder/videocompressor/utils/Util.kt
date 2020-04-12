package com.hangrycoder.videocompressor.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.hangrycoder.videocompressor.BuildConfig

object Util {

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).apply { show() }
    }

    fun showLogE(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message)
        }
    }
}