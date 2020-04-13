package com.hangrycoder.videocompressor.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.hangrycoder.videocompressor.R
import kotlinx.android.synthetic.main.activity_select_video.*

class SelectVideoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_video)

        selectVideoButton.setOnClickListener {
            //Check if storage permission is granted
            if (hasPermissions(this, *PERMISSIONS)) {
                fetchVideoFromGallery()
            } else {
                ActivityCompat.requestPermissions(this,
                    PERMISSIONS,
                    PERMISSION_ALL
                )
            }
        }
    }

    private fun fetchVideoFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "video/*"
        }
        startActivityForResult(
            Intent.createChooser(intent,
                SELECT_VIDEO
            ),
            REQUEST_GALLERY_VIDEO
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALLERY_VIDEO) {
                val selectedImageUri = data?.data

                startActivity(Intent(this, CompressVideoActivity::class.java).apply {
                    putExtra(CompressVideoActivity.INTENT_VIDEO_URI, selectedImageUri.toString())
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
                fetchVideoFromGallery()
            }
        }
    }

    companion object {
        private val PERMISSIONS = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        private const val PERMISSION_ALL = 1

        private const val SELECT_VIDEO = "Select Video"
        private const val REQUEST_GALLERY_VIDEO = 111
    }
}
