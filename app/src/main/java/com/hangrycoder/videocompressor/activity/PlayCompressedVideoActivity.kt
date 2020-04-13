package com.hangrycoder.videocompressor.activity

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.hangrycoder.videocompressor.R
import com.hangrycoder.videocompressor.databinding.ActivityPlayCompressedVideoBindingImpl
import com.hangrycoder.videocompressor.utils.SimpleMediaPlayer
import com.hangrycoder.videocompressor.utils.Util
import kotlinx.android.synthetic.main.activity_play_compressed_video.*

class PlayCompressedVideoActivity : AppCompatActivity() {

    private var videoPath: String? = null
    private var simpleMediaPlayer: SimpleMediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataBinding()
        setIntentParams()
        initializePlayerAndPlayVideo()
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

    private fun initializePlayerAndPlayVideo() {
        simpleMediaPlayer = SimpleMediaPlayer(this, Uri.parse(videoPath))
        playerView.player = simpleMediaPlayer?.getPlayer()
        simpleMediaPlayer?.play()
    }

    override fun onStop() {
        super.onStop()
        simpleMediaPlayer?.stop()
    }

    companion object {
        private val TAG = PlayCompressedVideoActivity::class.java.simpleName
        const val INTENT_COMPRESSED_VIDEO_PATH = "compressed_video_path"
    }
}