package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.databinding.ActivityMainBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.FileNotFoundException
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    companion object {
        const val REQUEST_CODE_CAMERA = 13134
        const val REQUEST_CODE_GALLERY = 13135
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this@MainActivity))
        setContentView(binding.root)

        binding.buttonSelectPicture.setOnClickListener {
            AlertDialog.Builder(this@MainActivity)
                .setTitle("انتخاب عکس")
                .setItems(
                    arrayOf(
                        "انتخاب عکس از گالری",
                        "انتخاب عکس با دوربین"
                    )
                ) { dialog, option ->
                    when (option) {
                        0 -> {
                            choosePhotoFromGallery()
                        }
                        1 -> {
                            choosePhotoFromCamera()

                        }
                    }
                }.show()
        }


        binding.imageViewSettings.setOnClickListener {
            gotoSettingsActivity()
        }
    }


    private fun choosePhotoFromGallery() {
        Dexter.withContext(this@MainActivity)
            .withPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        capturePictureFromGallery()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(                                    //if in AlertDialog user clicked on 'Deny'
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    showRationalPermission()
                }

            })
            .check()
    }


    private fun capturePictureFromGallery() {
        var galleryIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY)
    }


    private fun choosePhotoFromCamera() {
        Dexter.withContext(this@MainActivity)
            .withPermissions(
                android.Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        capturePictureByCamera()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(                                    //if in AlertDialog user clicked on 'Deny'
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    showRationalPermission()
                }

            })
            .check()
    }


    private fun capturePictureByCamera() {
        val cameraIntent =
            Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA)
    }

    private fun showRationalPermission() {
        AlertDialog.Builder(this@MainActivity)
            .setTitle("اجازه دسترسی به دوربین")
            .setPositiveButton("برو به تنظیمات") { _, _ ->
                val settingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts(
                    "package",
                    packageName,
                    null
                )                                                                                   // goes to app's setting address
                settingsIntent.data = uri
                startActivity(settingsIntent)
            }
            .setNegativeButton("اجازه نمیدهم") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//camera:
        if (resultCode == Activity.RESULT_OK) {                                                     // operation succeeded
            if (requestCode == REQUEST_CODE_CAMERA) {                                                //Note *
                val bitmapImage = data?.extras?.get("data") as Bitmap
                binding.imageViewSelectedPicture.setImageBitmap(bitmapImage)
            }
        }
//gallery:
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_GALLERY) {
                if (data != null) {
                    var contentUri = data.data //uri : addres e aks
                    try {
                        var imageBitmap =
                            MediaStore.Images.Media.getBitmap(this.contentResolver, contentUri)
                        binding.imageViewSelectedPicture.setImageBitmap(imageBitmap)
                    } catch (ex: FileNotFoundException) {
                        Toast.makeText(this, "فایل شما پیدا نشد !", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        if (resultCode == Activity.RESULT_CANCELED) {
            if (requestCode == REQUEST_CODE_CAMERA) {
                Toast.makeText(this, "عکسی نگرفتید!", Toast.LENGTH_SHORT).show()
            }

            if (requestCode == REQUEST_CODE_GALLERY) {
                Toast.makeText(this, "فایل شما پیدا نشد !", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun gotoSettingsActivity() {
        val intent = Intent(this@MainActivity, SettingsActivity::class.java)
        startActivity(intent)
    }
}


// Note : we went to camera with ACTION_IMAGE_CAPTURE --> when we back to Activity we should check is requestCode same with REQUEST_CODE_CAMERA ???