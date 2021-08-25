package com.myproject.mymemorygame.activities

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.myproject.mymemorygame.adapters.ImagePickerAdapter
import com.myproject.mymemorygame.databinding.ActivityCreateBinding
import com.myproject.mymemorygame.models.BoardSize
import com.myproject.mymemorygame.utils.EXTRA_BOARD_SIZE

class CreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateBinding

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

        binding.rvImagePicker.adapter = ImagePickerAdapter(this, chosenImagesUris, boardSize)
        // Guaranteed that the rv dimensions won't change
        binding.rvImagePicker.setHasFixedSize(true)
        binding.rvImagePicker.layoutManager = GridLayoutManager(this, boardSize.getWidth())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}