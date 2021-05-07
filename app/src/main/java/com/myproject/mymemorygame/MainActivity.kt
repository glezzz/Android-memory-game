package com.myproject.mymemorygame

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

    }
}