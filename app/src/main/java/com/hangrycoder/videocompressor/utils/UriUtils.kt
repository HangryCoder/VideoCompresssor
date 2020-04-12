package com.hangrycoder.videocompressor.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

object UriUtils {
    fun getRealPathFromUri(context: Context, contentUri: Uri?): String? {
        var cursor: Cursor? = null
        try {
            val projection = arrayOf(MediaStore.Video.Media.DATA)
            cursor = context.contentResolver.query(contentUri!!, projection, null, null, null)
            val columnIndex: Int? = cursor?.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor?.moveToFirst()
            return cursor?.getString(columnIndex!!)
        } finally {
            cursor?.close()
        }
    }
}