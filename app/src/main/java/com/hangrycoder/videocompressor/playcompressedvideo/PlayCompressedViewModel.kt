package com.hangrycoder.videocompressor.playcompressedvideo

import android.net.Uri
import com.hangrycoder.videocompressor.utils.SimpleMediaPlayer

class PlayCompressedViewModel(private val playCompressedVideoView: PlayCompressedVideoContract.PlayCompressedVideoView) :
    PlayCompressedVideoContract.PlayCompressedVideoViewModel {

    private var simpleMediaPlayer: SimpleMediaPlayer? = null

    override fun initMediaPlayer(videoUri: Uri?) {
        simpleMediaPlayer = SimpleMediaPlayer(playCompressedVideoView.getViewContext()!!, videoUri)
    }

    override fun getPlayer() = simpleMediaPlayer?.getPlayer()

    override fun playVideo() {
        simpleMediaPlayer?.play()
    }

    override fun stopVideo() {
        simpleMediaPlayer?.stop()
    }
}