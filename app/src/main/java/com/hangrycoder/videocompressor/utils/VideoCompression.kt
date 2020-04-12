package com.hangrycoder.videocompressor.utils

import android.content.Context
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.FFmpegLoadBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException

class VideoCompression(context: Context, private val compressionCallbacks: CompressionCallbacks) {

    private var ffmpeg: FFmpeg = FFmpeg.getInstance(context)

    init {
        try {
            ffmpeg.loadBinary(object : FFmpegLoadBinaryResponseHandler {
                override fun onFinish() {
                    Util.showLogE(TAG, "FFMPEG onFinish")
                }

                override fun onSuccess() {
                    Util.showLogE(TAG, "FFMPEG onSuccess")
                }

                override fun onFailure() {
                    Util.showLogE(TAG, "FFMPEG onFailure")
                }

                override fun onStart() {

                }
            })
        } catch (exception: FFmpegCommandAlreadyRunningException) {
            Util.showLogE(TAG, "FFMPEG Exception ${exception.printStackTrace()}")
        }
    }

    fun compressVideo(bitrate: String, inputFilePath: String, outputFilePath: String) {
        val command = arrayOf(
            "-y",
            "-i",
            inputFilePath,
            "-b:v",
            bitrate + "k",
            outputFilePath
        )

        ffmpeg.execute(command, object : ExecuteBinaryResponseHandler() {
            override fun onFinish() {
                super.onFinish()
                compressionCallbacks.onFinish()
            }

            override fun onSuccess(message: String?) {
                super.onSuccess(message)
                compressionCallbacks.onSuccess()
            }

            override fun onFailure(message: String?) {
                super.onFailure(message)
                Util.showLogE(TAG, "Compression onFailure $message")
                compressionCallbacks.onFailure()
            }

            override fun onStart() {
                super.onStart()
                compressionCallbacks.onStart()
            }
        })
    }

    companion object {
        private var TAG = VideoCompression::class.java.simpleName
    }

    interface CompressionCallbacks {
        fun onStart()
        fun onFinish()
        fun onSuccess()
        fun onFailure()
    }
}