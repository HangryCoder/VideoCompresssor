package com.hangrycoder.videocompressor.playcompressedvideo

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.SimpleExoPlayer

interface PlayCompressedVideoContract {

    interface PlayCompressedVideoViewModel {
        fun initMediaPlayer(videoUri: Uri?)
        fun getPlayer(): SimpleExoPlayer?
        fun playVideo()
        fun stopVideo()
    }

    interface PlayCompressedVideoView {
        fun getViewContext(): Context?
    }
}