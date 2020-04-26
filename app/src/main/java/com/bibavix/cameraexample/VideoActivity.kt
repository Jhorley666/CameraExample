package com.bibavix.cameraexample

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.TextView
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_video.*
import org.w3c.dom.Text
import java.io.File

class VideoActivity : AppCompatActivity() {
    lateinit var videoUri: Uri
    val REQUEST_VIDEO_CAPTURE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        record_btn.setOnClickListener {
            recordVideo()
        }

        play_btn.setOnClickListener {
            if (videoUri != null){
                playVideo(videoUri)
            }
        }

    }

    private fun recordVideo() {
        val videoFile: File? = createVideoFile()
        if (videoFile != null){
            videoUri = FileProvider.getUriForFile(this, "com.bibavix.android.provider",videoFile)
        }
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri)
        startActivityForResult(intent, REQUEST_VIDEO_CAPTURE)
    }


    private fun playVideo(video_uri: Uri){
        video_view.setVideoURI(video_uri)
        video_view.start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == REQUEST_VIDEO_CAPTURE && resultCode == Activity.RESULT_OK){
            video_view.setVideoURI(videoUri)
            video_view.start()

        }
    }

    private fun createVideoFile() : File{
        val fileName = "MyVideo"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES)
        return File.createTempFile(fileName,".mp4",storageDir)
    }
}
