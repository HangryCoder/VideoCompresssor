package com.hangrycoder.videocompressor.activities

import android.os.Bundle
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.hangrycoder.videocompressor.R
import com.hangrycoder.videocompressor.databinding.ActivityPlayCompressedVideoBindingImpl
import com.hangrycoder.videocompressor.utils.Util
import kotlinx.android.synthetic.main.activity_play_compressed_video.*

class PlayCompressedVideoActivity : AppCompatActivity() {

    private var videoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataBinding()
        setIntentParams()
        initVideoAndPlay()
    }

    private fun initDataBinding() {
        DataBindingUtil.setContentView<ActivityPlayCompressedVideoBindingImpl>(
            this,
            R.layout.activity_play_compressed_video
        )
    }

    private fun setIntentParams() {
        videoPath = intent.extras?.getString(INTENT_COMPRESSED_VIDEO_PATH)
        Util.showLogE(TAG, "CompressedVideoPath $videoPath")
    }

    private fun initVideoAndPlay() {
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
        //This is the make the media controller appear above the video view
        videoView.setOnPreparedListener {
            it.setOnVideoSizeChangedListener { _, _, _ ->
                videoView.setMediaController(mediaController)
                mediaController.setAnchorView(videoView)
                mediaController.show()
            }
        }
        videoView.setVideoPath(videoPath)
        videoView.requestFocus()
        videoView.start()
    }

    companion object {
        private val TAG = PlayCompressedVideoActivity::class.java.simpleName
        const val INTENT_COMPRESSED_VIDEO_PATH = "compressed_video_path"
    }
}