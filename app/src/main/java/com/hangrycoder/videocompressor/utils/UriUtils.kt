package com.hangrycoder.videocompressor.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

object UriUtils {
    fun getImageFilePath(context: Context, uri: Uri?): String? {
        var path: String? = null
        var videoId: String? = null
        var cursor: Cursor? = context.contentResolver.query(uri!!, null, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            videoId = cursor.getString(0)
            videoId = videoId.substring(videoId.lastIndexOf(":") + 1)
            cursor.close()
        }
        cursor = context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            null,
            MediaStore.Video.Media._ID + " = ? ",
            arrayOf(videoId),
            null
        )
        if (cursor != null) {
            cursor.moveToFirst()
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
            cursor.close()
        }
        return path
    }
}