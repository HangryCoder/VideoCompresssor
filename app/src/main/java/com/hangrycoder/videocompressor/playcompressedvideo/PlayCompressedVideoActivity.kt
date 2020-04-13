package com.hangrycoder.videocompressor.playcompressedvideo

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.hangrycoder.videocompressor.R
import com.hangrycoder.videocompressor.databinding.ActivityPlayCompressedVideoBindingImpl
import com.hangrycoder.videocompressor.utils.Util
import kotlinx.android.synthetic.main.activity_play_compressed_video.*

class PlayCompressedVideoActivity : AppCompatActivity(),
    PlayCompressedVideoContract.PlayCompressedVideoView {

    private var videoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataBinding()
        setIntentParams()
        initViewModel()
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

    private lateinit var playCompressedViewModel: PlayCompressedViewModel
    private fun initViewModel() {
        playCompressedViewModel = PlayCompressedViewModel(this)
    }

    private fun initializePlayerAndPlayVideo() {
        playCompressedViewModel.initMediaPlayer(Uri.parse(videoPath))
        playerView.player = playCompressedViewModel.getPlayer()
        playCompressedViewModel.playVideo()
    }

    override fun onStop() {
        super.onStop()
        playCompressedViewModel.stopVideo()
    }

    override fun getViewContext(): Context? = this

    companion object {
        private val TAG = PlayCompressedVideoActivity::class.java.simpleName
        const val INTENT_COMPRESSED_VIDEO_PATH = "compressed_video_path"
    }
}