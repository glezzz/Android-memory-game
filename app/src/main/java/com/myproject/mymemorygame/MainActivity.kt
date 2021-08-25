package com.myproject.mymemorygame

import android.os.Bundle
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

    private lateinit var adapter: MemoryBoardAdapter
    private lateinit var memoryGame: MemoryGame
    // Initially boardSize will be EASY
    private var boardSize: BoardSize = BoardSize.EASY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Construct game
        memoryGame = MemoryGame(boardSize)

        // Set up adapter
        adapter = MemoryBoardAdapter(boardSize, memoryGame.cards, object: MemoryBoardAdapter.CardClickListener {
            override fun onCardClicked(position: Int) {
                updateGameWithFlip(position)
            }

        })

        // Performance optimization
        rvBoard.adapter = adapter
        rvBoard.setHasFixedSize(true)
        rvBoard.layoutManager = GridLayoutManager(this, boardSize.getWidth())
    }

    private fun updateGameWithFlip(position: Int) {
        memoryGame.flipCard(position)
        adapter.notifyDataSetChanged()
    }

    /*private fun setUpRecyclerView(randomizedImages: List<Int>) {
        rvBoard.adapter = MemoryBoardAdapter(boardSize, randomizedImages)

        // Performance optimization
        rvBoard.setHasFixedSize(true)
        rvBoard.layoutManager = GridLayoutManager(this, boardSize.getWidth())
    }*/
}