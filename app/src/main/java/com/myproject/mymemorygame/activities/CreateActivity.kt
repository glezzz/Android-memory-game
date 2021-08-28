package com.myproject.mymemorygame.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.myproject.mymemorygame.adapters.ImagePickerAdapter
import com.myproject.mymemorygame.databinding.ActivityCreateBinding
import com.myproject.mymemorygame.models.BoardSize
import com.myproject.mymemorygame.utils.EXTRA_BOARD_SIZE
import com.myproject.mymemorygame.utils.isPermissionGranted
import com.myproject.mymemorygame.utils.requestPermission

class CreateActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "CreateActivity"
        private const val PICK_PHOTO_CODE = 655
        private const val READ_EXTERNAL_PHOTOS_CODE = 248
        private const val READ_PHOTOS_PERMISSION = android.Manifest.permission.READ_EXTERNAL_STORAGE
    }

    private lateinit var binding: ActivityCreateBinding

    private lateinit var adapter: ImagePickerAdapter
    private lateinit var boardSize: BoardSize
    private var numImagesRequired = -1
    private val chosenImagesUris = mutableListOf<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Modify action bar to show back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Pull data out of intent
        boardSize = intent.getSerializableExtra(EXTRA_BOARD_SIZE) as BoardSize
        numImagesRequired = boardSize.getNumPairs()
        // Modify action bar title based on how many pictures user needs to select
        supportActionBar?.title = "Choose pics (0 / $numImagesRequired)"

        adapter = ImagePickerAdapter(
            this,
            chosenImagesUris,
            boardSize,
            object : ImagePickerAdapter.ImageClickListener {
                // User has tapped on one of imageView squares
                override fun onPlaceHolderClicked() {
                    if (isPermissionGranted(this@CreateActivity, READ_PHOTOS_PERMISSION)) {
                        launchIntentForPhotos()
                    } else {
                        requestPermission(
                            this@CreateActivity,
                            READ_PHOTOS_PERMISSION,
                            READ_EXTERNAL_PHOTOS_CODE
                        )
                    }
                }
            })

        binding.rvImagePicker.adapter = adapter
        // Guaranteed that the rv dimensions won't change
        binding.rvImagePicker.setHasFixedSize(true)
        binding.rvImagePicker.layoutManager = GridLayoutManager(this, boardSize.getWidth())
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == READ_EXTERNAL_PHOTOS_CODE) {
            // User has granted permission
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchIntentForPhotos()
                // User has denied access
            } else {
                Toast.makeText(
                    this,
                    "Please, provide access to your photos to create a custom game",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != PICK_PHOTO_CODE || resultCode != Activity.RESULT_OK || data == null) {
            Log.w(
                TAG,
                "Did not get data back from the launched activity, user likely canceled flow"
            )
        }
        // If photo app only allows one photo at a time
        val selectedUri = data?.data
        // If photo app allows multiple photos at a time
        val clipData = data?.clipData

        if (clipData != null) {
            Log.i(TAG, "clipData numImages ${clipData.itemCount}: $clipData")

            for (i in 0 until clipData.itemCount) {
                val clipItem = clipData.getItemAt(i)

                // If we get to this method onActivityResult(), that means that user has tapped on
                // the grey placeholder imageView. That means there is space for at least one more image
                // in chosenImagesUris. In the case of clipData user might hace picked an arbitraty number
                // of pics, but we only want the amount specified by numImagesRequired. e.g. User picks
                // 20 pics, but we only want 8. That's why in every it of the for loop we check how many images
                // are in chosenImagesUris. In the else case, no need to check, because there, it's
                // one image at a time

                // Check if chosenImagesUris still needs to be populated
                if (chosenImagesUris.size < numImagesRequired) {
                    // Add clipItem.uri to chosenImagesUris. chosenImagesUris is passed in to adapter
                    chosenImagesUris.add(clipItem.uri)
                }
            }

        } else if (selectedUri != null) {
            Log.i(TAG, "data: $selectedUri")

            chosenImagesUris.add(selectedUri)
        }
        // Notify adapter and change the grey placeholder imageView with preview of selected pics
        adapter.notifyDataSetChanged()
        supportActionBar?.title = "Choose pics (${chosenImagesUris.size}/$numImagesRequired)"
    }

    /**
     * Launches the flow for the user to pick a photo
     */
    private fun launchIntentForPhotos() {
        val intent = Intent(Intent.ACTION_PICK)
        // Only images. No videos, pdf files or other
        intent.type = "image/*"
        // If app that user opens up supports it, allow user to select multiple images at once
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(Intent.createChooser(intent, "Choose pics"), PICK_PHOTO_CODE)
    }
}