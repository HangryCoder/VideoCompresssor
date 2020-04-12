package com.hangrycoder.videocompressor

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.FFmpegLoadBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
import com.hangrycoder.videocompressor.databinding.ActivityCompressVideoBindingImpl
import com.hangrycoder.videocompressor.utils.UriUtils
import com.hangrycoder.videocompressor.utils.Util
import kotlinx.android.synthetic.main.activity_compress_video.*
import java.io.File

class CompressVideoActivity : AppCompatActivity() {

    private val TAG = CompressVideoActivity::class.java.simpleName
    private var videoUri: Uri? = null
    private lateinit var ffmpeg: FFmpeg
    private var compressedVideosFolder: File = File(
        Environment.getExternalStorageDirectory().path + File.separator.toString() + OUTPUT_FILE_DIRECTORY_NAME
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataBinding()
        setIntentParams()
        playVideo()
        setupFFMPEG()

        compressVideoButton.setOnClickListener {
            executeFFMPEGCommandToCompressVideo()
        }
    }

    private fun initDataBinding() {
        DataBindingUtil.setContentView<ActivityCompressVideoBindingImpl>(
            this, R.layout.activity_compress_video
        )
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

        if (!compressedVideosFolder.exists()) {
            compressedVideosFolder.mkdirs()
        }
        val outputFileAbsolutePath = compressedVideosFolder.absolutePath + File.separator.toString() + "abc.mp4"

        Log.e(TAG, "Output File $outputFileAbsolutePath")

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
                Util.showToast(applicationContext, "Video compressed successfully")
            }

            override fun onFailure(message: String?) {
                super.onFailure(message)
                Log.e(TAG, "Execute onFailure $message")
                Util.showToast(applicationContext, "Video compression failed")
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
        const val OUTPUT_FILE_DIRECTORY_NAME = "CompressedVideos"
        const val INTENT_VIDEO_URI = "video_uri"
    }
}