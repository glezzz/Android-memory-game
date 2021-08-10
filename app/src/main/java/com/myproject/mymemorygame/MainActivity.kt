package com.myproject.mymemorygame

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.myproject.mymemorygame.adapter.MemoryBoardAdapter
import com.myproject.mymemorygame.databinding.ActivityMainBinding
import com.myproject.mymemorygame.models.BoardSize
import com.myproject.mymemorygame.models.MemoryGame

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding

    private val rvBoard by lazy { binding.rvBoard }
    private val tvNumMoves by lazy { binding.tvNumMoves }
    private val tvNumPairs by lazy { binding.tvNumPairs }

    // Initially boardSize will be EASY
    private var boardSize: BoardSize = BoardSize.HARD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Construct game
        val memoryGame = MemoryGame(boardSize)

        // Set up adapter
        rvBoard.adapter = MemoryBoardAdapter(boardSize, memoryGame.cards, object: MemoryBoardAdapter.CardClickListener {
            override fun onCardClicked(position: Int) {
                Log.i(TAG, "Card clicked at $position")
            }

        })

        // Performance optimization
        rvBoard.setHasFixedSize(true)
        rvBoard.layoutManager = GridLayoutManager(this, boardSize.getWidth())
    }

    /*private fun setUpRecyclerView(randomizedImages: List<Int>) {
        rvBoard.adapter = MemoryBoardAdapter(boardSize, randomizedImages)

        // Performance optimization
        rvBoard.setHasFixedSize(true)
        rvBoard.layoutManager = GridLayoutManager(this, boardSize.getWidth())
    }*/
}