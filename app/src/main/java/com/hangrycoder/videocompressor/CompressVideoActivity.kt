package com.hangrycoder.videocompressor

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.FFmpegLoadBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
import com.hangrycoder.videocompressor.utils.UriUtils
import kotlinx.android.synthetic.main.activity_play_video.*

class CompressVideoActivity : AppCompatActivity() {

    private val TAG = CompressVideoActivity::class.java.simpleName
    private var videoUri: Uri? = null
    private lateinit var ffmpeg: FFmpeg
    private val outputFileAbsolutePath = "/storage/emulated/0/abc.mp4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_video)

        setIntentParams()
        playVideo()
        setupFFMPEG()

        compressVideoButton.setOnClickListener {
            executeFFMPEGCommandToCompressVideo()
        }
    }

    private fun setIntentParams() {
        videoUri = Uri.parse(intent.extras?.getString(INTENT_VIDEO_URI))
        Log.e(TAG, "VideoUri ${UriUtils.getImageFilePath(this, videoUri)}")
    }

    private fun playVideo() {
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
        videoView.setVideoURI(videoUri)
        videoView.requestFocus()
        videoView.start()
    }

    private fun setupFFMPEG() {
        ffmpeg = FFmpeg.getInstance(this)
        try {
            ffmpeg.loadBinary(object : FFmpegLoadBinaryResponseHandler {
                override fun onFinish() {
                    Log.e(TAG, "FFMPEG onFinish")
                }

                override fun onSuccess() {
                    Log.e(TAG, "FFMPEG onSuccess")
                }

                override fun onFailure() {
                    Log.e(TAG, "FFMPEG onFailure")
                }

                override fun onStart() {

                }
            })
        } catch (exception: FFmpegCommandAlreadyRunningException) {
            Log.e(TAG, "FFMPEG Exception ${exception.printStackTrace()}")
        }
    }

    private fun executeFFMPEGCommandToCompressVideo() {

        val inputFilePath = UriUtils.getImageFilePath(this, videoUri)
        val bitrate = inputBitrate.text.toString() + "k"
        val command = arrayOf(
            "-y",
            "-i",
            inputFilePath,
            "-s",
            "160x120",
            "-r",
            "25",
            "-vcodec",
            "mpeg4",
            "-b:v",
            bitrate,
            //"150k",
            "-b:a",
            "48000",
            "-ac",
            "2",
            "-ar",
            "22050",
            outputFileAbsolutePath
        )

        ffmpeg.execute(command, object : ExecuteBinaryResponseHandler() {
            override fun onFinish() {
                super.onFinish()
                Log.e(TAG, "Execute onFinish")
            }

            override fun onSuccess(message: String?) {
                super.onSuccess(message)
                Log.e(TAG, "Execute onSuccess")
            }

            override fun onFailure(message: String?) {
                super.onFailure(message)
                Log.e(TAG, "Execute onFailure $message")
            }

            override fun onProgress(message: String?) {
                super.onProgress(message)
            }

            override fun onStart() {
                super.onStart()
            }
        })
    }

    companion object {
        const val INTENT_VIDEO_URI = "video_uri"
    }
}