package com.hangrycoder.videocompressor.compressvideo

import android.content.Context
import android.net.Uri

interface CompressVideoContract {

    interface CompressVideoViewModel {
        fun initMediaPlayer(videoUri: Uri?)
        fun compressVideo(videoUri: Uri?, bitrate: String)
        fun playVideo()
        fun stopVideo()
    }

    interface CompressVideoView {
        fun getViewContext(): Context?

        fun showLoader()
        fun hideLoader()

        fun showToast(message: String)

        fun openPlayCompressVideoActivity()
    }
}