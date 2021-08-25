package com.myproject.mymemorygame

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
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
        adapter = MemoryBoardAdapter(
            this,
            boardSize,
            memoryGame.cards,
            object : MemoryBoardAdapter.CardClickListener {
                override fun onCardClicked(position: Int) {
                    updateGameWithFlip(position)
                }

            })

        // Performance optimization
        rvBoard.adapter = adapter
        rvBoard.setHasFixedSize(true)
        rvBoard.layoutManager = GridLayoutManager(this, boardSize.getWidth())
    }

    /**
     * Player attempts to flip card at position in recyclerView
     */
    private fun updateGameWithFlip(position: Int) {
        // Error checking
        if (memoryGame.haveWonGame()) {
            // Alert user of invalid move
            Snackbar.make(binding.clRoot, "You already won!", Snackbar.LENGTH_LONG).show()
            return
        }
        if (memoryGame.isCardFaceUp(position)) {
            // Alert user of invalid move
            Snackbar.make(binding.clRoot, "Invalid move!", Snackbar.LENGTH_SHORT).show()
            return
        }
        // Actually flipping over the card
        if (memoryGame.flipCard(position)) {
            Log.i(TAG, "Found match! Num pairs found: ${memoryGame.numPairsFound}")

            binding.tvNumPairs.text =
                "Pairs: ${memoryGame.numPairsFound} / ${boardSize.getNumPairs()}"
            if (memoryGame.haveWonGame()) {
                Snackbar.make(binding.clRoot, "You won! Congratulations.", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
        binding.tvNumMoves.text = "Moves: ${memoryGame.getNumMoves()}"
        adapter.notifyDataSetChanged()
    }
}