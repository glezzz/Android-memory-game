package com.myproject.mymemorygame

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.myproject.mymemorygame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val rvBoard by lazy { binding.rvBoard }
    private val tvNumMoves by lazy { binding.tvNumMoves }
    private val tvNumPairs by lazy { binding.tvNumPairs }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvBoard.adapter = MemoryBoardAdapter(this, 8)

        // Performance optimization
        rvBoard.setHasFixedSize(true)
        rvBoard.layoutManager = GridLayoutManager(this, 2)

    }
}