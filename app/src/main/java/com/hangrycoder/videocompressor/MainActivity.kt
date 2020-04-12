package com.hangrycoder.videocompressor

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hangrycoder.videocompressor.utils.UriUtils
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selectVideoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "video/*"
            }
            startActivityForResult(
                Intent.createChooser(intent, SELECT_VIDEO),
                REQUEST_GALLERY_VIDEO
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALLERY_VIDEO) {
                val selectedImageUri = data?.data

                val selectedImagePath = UriUtils.getRealPathFromUri(this, selectedImageUri)
                Log.e(TAG, "$selectedImagePath")

            }
        }
    }

    companion object {
        private const val SELECT_VIDEO = "Select Video"
        private const val REQUEST_GALLERY_VIDEO = 111
    }
}
