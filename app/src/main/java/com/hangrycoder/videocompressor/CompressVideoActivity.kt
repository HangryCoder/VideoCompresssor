package com.hangrycoder.videocompressor

import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.FFmpegLoadBinaryResponseHandler
import kotlinx.android.synthetic.main.activity_play_video.*

class CompressVideoActivity : AppCompatActivity() {

    private var videoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_video)

        videoUri = Uri.parse(intent.extras?.getString(INTENT_VIDEO_URI))

        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
        videoView.setVideoURI(videoUri)
        videoView.requestFocus()
        videoView.start()

        compressVideoButton.setOnClickListener {

            val ffmpeg = FFmpeg.getInstance(this)
            ffmpeg.loadBinary(object : FFmpegLoadBinaryResponseHandler {
                override fun onFinish() {

                }

                override fun onSuccess() {
                }

                override fun onFailure() {
                }

                override fun onStart() {

                }
            })
        }
    }

    companion object {
        const val INTENT_VIDEO_URI = "video_uri"
    }
}