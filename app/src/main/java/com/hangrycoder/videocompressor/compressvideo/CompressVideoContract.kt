package com.hangrycoder.videocompressor.compressvideo

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.SimpleExoPlayer

interface CompressVideoContract {

    interface CompressVideoViewModel {
        fun initMediaPlayer(videoUri: Uri?)
        fun compressVideo(videoUri: Uri?, bitrate: String)
        fun getPlayer(): SimpleExoPlayer?
        fun playVideo()
        fun stopVideo()
    }

    interface CompressVideoView {
        fun getViewContext(): Context?

        fun showLoader()
        fun hideLoader()

        fun showToast(message: String)

        fun openPlayCompressVideoActivity(outputFilePath:String?)
    }
}