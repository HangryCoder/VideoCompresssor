package com.hangrycoder.videocompressor.compressvideo

import android.net.Uri
import com.hangrycoder.videocompressor.utils.SimpleMediaPlayer
import com.hangrycoder.videocompressor.utils.UriUtils
import com.hangrycoder.videocompressor.utils.VideoCompression
import com.hangrycoder.videocompressor.utils.createFolderIfDoesntExist
import java.io.File

class CompressVideoViewModel(private var compressVideoView: CompressVideoContract.CompressVideoView) :
    CompressVideoContract.CompressVideoViewModel {

    private var compressedVideosFolder: File? = null
    private var outputFileAbsolutePath: String? = null
    var simpleMediaPlayer: SimpleMediaPlayer? = null

    override fun initMediaPlayer(videoUri: Uri?) {
        simpleMediaPlayer = SimpleMediaPlayer(compressVideoView.getViewContext()!!, videoUri)
    }

    override fun getPlayer() = simpleMediaPlayer?.getPlayer()

    override fun compressVideo(videoUri: Uri?, bitrate: String) {
        val inputFilePath =
            UriUtils.getImageFilePath(compressVideoView.getViewContext()!!, videoUri)
        inputFilePath ?: return

        compressedVideosFolder = File(
            compressVideoView.getViewContext()!!.getExternalFilesDir(null)?.path +
                    File.separator.toString() + OUTPUT_FILE_DIRECTORY_NAME
        )
        compressedVideosFolder?.createFolderIfDoesntExist()

        outputFileAbsolutePath = compressedVideosFolder?.absolutePath +
                File.separator.toString() +
                System.currentTimeMillis() + FILE_EXTENTION
        outputFileAbsolutePath ?: return

        videoCompression.compressVideo(
            bitrate = bitrate,
            inputFilePath = inputFilePath,
            outputFilePath = outputFileAbsolutePath
        )
    }

    override fun playVideo() {
        simpleMediaPlayer?.play()
    }

    override fun stopVideo() {
        simpleMediaPlayer?.stop()
    }

    private val videoCompression: VideoCompression by lazy {
        VideoCompression(
            compressVideoView.getViewContext()!!,
            object : VideoCompression.CompressionCallbacks {
                override fun onStart() {
                    compressVideoView.showLoader()
                }

                override fun onFinish() {
                    compressVideoView.hideLoader()
                }

                override fun onSuccess() {
                    compressVideoView.showToast("Video compressed successfully")
                    compressVideoView.openPlayCompressVideoActivity(outputFileAbsolutePath)
                }

                override fun onFailure() {
                    compressVideoView.showToast("Video compression failed")
                }
            })
    }

    companion object {
        private const val FILE_EXTENTION = ".mp4"
        const val OUTPUT_FILE_DIRECTORY_NAME = "CompressedVideos"
    }
}