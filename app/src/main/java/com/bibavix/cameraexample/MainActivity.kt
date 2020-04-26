package com.bibavix.cameraexample

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.URI
import java.util.*

class MainActivity : AppCompatActivity() {

    private  val PERMISSION_CODE = 1000
    private  var image_uri: Uri? = null
    private  val REQUEST_TAKE_PHOTO= 1
    lateinit var photoPath : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fab_btn.setOnClickListener {
            val intent: Intent = Intent(this, VideoActivity::class.java)
            startActivity(intent)
        }
        capture_btn.setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    //Permission was not enabled
                    val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permission, PERMISSION_CODE)
                }else{
                    //Permission was granted
                    openCamera()
                }
            }else{
                    openCamera()
            }
        }


    }

    private fun openCamera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) !=null){
            var photoFile: File? = null
            try {
                photoFile = pictureFile()
            }catch (e: IOException){
                e.printStackTrace()
            }
            if (photoFile!=null){
                val photoUri = FileProvider.getUriForFile(this,
                    "com.bibavix.android.provider",photoFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(intent, REQUEST_TAKE_PHOTO)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK)
        {
            image_view.rotation = 90f;
            image_view.setImageURI(Uri.parse(photoPath))
        }
    }

    private fun pictureFile(): File?
    {
        val fileName = "My picture"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(fileName,".jpg",storageDir)
        photoPath = image.absolutePath
        return image
    }

}
