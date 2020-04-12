package com.hangrycoder.videocompressor

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.hangrycoder.videocompressor.utils.UriUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selectVideoButton.setOnClickListener {
            //Check if storage permission is granted
            if (hasPermissions(this, *PERMISSIONS)) {
                fetchVideoFromGallery()
            } else {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)
            }
        }
    }

    private fun fetchVideoFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "video/*"
        }
        startActivityForResult(
            Intent.createChooser(intent, SELECT_VIDEO),
            REQUEST_GALLERY_VIDEO
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALLERY_VIDEO) {
                val selectedImageUri = data?.data

                startActivity(Intent(this, PlayVideoScreen::class.java).apply {
                    putExtra(PlayVideoScreen.INTENT_VIDEO_URI, selectedImageUri.toString())
                })
            }
        }
    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ALL) {
            for (i in permissions.indices) {
                Log.v(TAG, "Permission: " + permissions[i] + " was " + grantResults[i])
                fetchVideoFromGallery()
            }
        }
    }

    companion object {

        private var TAG = MainActivity::class.java.simpleName
        private val PERMISSIONS = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        private const val PERMISSION_ALL = 1

        private const val SELECT_VIDEO = "Select Video"
        private const val REQUEST_GALLERY_VIDEO = 111
    }
}
