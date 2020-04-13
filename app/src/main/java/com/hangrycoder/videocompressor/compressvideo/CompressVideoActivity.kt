package com.hangrycoder.videocompressor.compressvideo

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.hangrycoder.videocompressor.R
import com.hangrycoder.videocompressor.activity.PlayCompressedVideoActivity
import com.hangrycoder.videocompressor.databinding.ActivityCompressVideoBindingImpl
import com.hangrycoder.videocompressor.utils.*
import kotlinx.android.synthetic.main.activity_compress_video.*
import java.io.File

class CompressVideoActivity : AppCompatActivity(), CompressVideoContract.CompressVideoView {

   // private var simpleMediaPlayer: SimpleMediaPlayer? = null
    private val TAG = CompressVideoActivity::class.java.simpleName

    private var videoUri: Uri? = null
  //  private var compressedVideosFolder: File? = null
   // private var outputFileAbsolutePath: String? = null

    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog(this).apply {
            setMessage("Video compression is in progress. Please wait :)")
            setCanceledOnTouchOutside(false)
            setCancelable(false)
        }
    }

  /*  private val videoCompression: VideoCompression by lazy {
        VideoCompression(this, object : VideoCompression.CompressionCallbacks {
            override fun onStart() {
                showLoader()
            }

            override fun onFinish() {
                hideLoader()
            }

            override fun onSuccess() {
                Util.showToast(applicationContext, "Video compressed successfully")
                val intent = Intent(
                    this@CompressVideoActivity,
                    PlayCompressedVideoActivity::class.java
                ).apply {
                    putExtra(
                        PlayCompressedVideoActivity.INTENT_COMPRESSED_VIDEO_PATH,
                        outputFileAbsolutePath
                    )
                }
                startActivity(intent)
                finish()
            }

            override fun onFailure() {
                Util.showToast(applicationContext, "Video compression failed")
            }

        })
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataBinding()
        setIntentParams()
        initViewModel()
        initializePlayerAndPlayVideo()
    }

    private lateinit var compressVideoViewModel: CompressVideoContract.CompressVideoViewModel
    private fun initViewModel() {
        compressVideoViewModel = CompressVideoViewModel(this)
    }

    private fun initDataBinding() {
        val binding = DataBindingUtil.setContentView<ActivityCompressVideoBindingImpl>(
            this, R.layout.activity_compress_video
        )
        binding.setCompressVideoClick {
            createOutputFolderIfDoesntExistAndCompressVideo()
        }
    }

    private fun createOutputFolderIfDoesntExistAndCompressVideo() {
        /* val inputFilePath = UriUtils.getImageFilePath(this, videoUri)
         inputFilePath ?: return

         compressedVideosFolder = File(
             getExternalFilesDir(null)?.path + File.separator.toString() + OUTPUT_FILE_DIRECTORY_NAME
         )
         compressedVideosFolder?.createFolderIfDoesntExist()

         outputFileAbsolutePath = compressedVideosFolder?.absolutePath +
                 File.separator.toString() +
                 System.currentTimeMillis() + FILE_EXTENTION
         outputFileAbsolutePath ?: return

         videoCompression.compressVideo(
             bitrate = inputBitrate.text.toString(),
             inputFilePath = inputFilePath,
             outputFilePath = outputFileAbsolutePath
         )*/
        compressVideoViewModel.compressVideo(
            videoUri = videoUri,
            bitrate = inputBitrate.text.toString()
        )
    }

    private fun setIntentParams() {
        videoUri = Uri.parse(intent.extras?.getString(INTENT_VIDEO_URI))
        Util.showLogE(TAG, "VideoUri ${UriUtils.getImageFilePath(this, videoUri)}")
    }

    private fun initializePlayerAndPlayVideo() {
        /* simpleMediaPlayer = SimpleMediaPlayer(this, videoUri)
         playerView.player = simpleMediaPlayer?.getPlayer()
         simpleMediaPlayer?.play()*/
        compressVideoViewModel.initMediaPlayer(videoUri)
        playerView.player = compressVideoViewModel.getPlayer()
        compressVideoViewModel.playVideo()
    }

    override fun onStop() {
        super.onStop()
        compressVideoViewModel.stopVideo()
    }

    override fun hideLoader() {
        progressDialog.dismiss()
    }

    override fun showToast(message: String) {
        Util.showToast(this, message)
    }

    override fun openPlayCompressVideoActivity(outputFilePath: String?) {
        val intent = Intent(
            this@CompressVideoActivity,
            PlayCompressedVideoActivity::class.java
        ).apply {
            putExtra(
                PlayCompressedVideoActivity.INTENT_COMPRESSED_VIDEO_PATH,
                outputFilePath
            )
        }
        startActivity(intent)
        finish()
    }

    override fun getViewContext(): Context? = this

    override fun showLoader() {
        progressDialog.show()
    }

    companion object {
       /* private const val FILE_EXTENTION = ".mp4"
        const val OUTPUT_FILE_DIRECTORY_NAME = "CompressedVideos"*/
        const val INTENT_VIDEO_URI = "video_uri"
    }
}