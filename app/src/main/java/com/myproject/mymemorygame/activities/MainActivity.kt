package com.myproject.mymemorygame.activities

import android.animation.ArgbEvaluator
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.myproject.mymemorygame.R
import com.myproject.mymemorygame.adapter.MemoryBoardAdapter
import com.myproject.mymemorygame.databinding.ActivityMainBinding
import com.myproject.mymemorygame.models.BoardSize
import com.myproject.mymemorygame.models.MemoryGame

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: MemoryBoardAdapter
    private lateinit var memoryGame: MemoryGame

    // Initially boardSize will be EASY
    private var boardSize: BoardSize = BoardSize.EASY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBoard()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miRefresh -> {
                //Show alert message when trying to reset a game in progress
                if (memoryGame.getNumMoves() > 0 && !memoryGame.haveWonGame()) {
                    showAlertDialog("Quit your current game?", null, View.OnClickListener {
                        setupBoard()
                    })

                } else {
                    // Reset game
                    setupBoard()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAlertDialog(
        title: String,
        view: View?,
        positiveClickListener: View.OnClickListener
    ) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            // Null means dismiss this alert dialog if player presses cancel
            .setNegativeButton("Cancel", null)
            .setPositiveButton("OK") { _, _ ->
                positiveClickListener.onClick(null)
            }.show()
    }

    private fun setupBoard() {
        when(boardSize) {
            BoardSize.EASY ->

            BoardSize.MEDIUM -> TODO()
            BoardSize.HARD -> TODO()
        }
        // Set initial color to no progress
        binding.tvNumPairs.setTextColor(ContextCompat.getColor(this, R.color.color_progress_none))
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
        binding.rvBoard.adapter = adapter
        binding.rvBoard.setHasFixedSize(true)
        binding.rvBoard.layoutManager = GridLayoutManager(this, boardSize.getWidth())
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

            // Using color interpolation here
            val color = ArgbEvaluator().evaluate(
                memoryGame.numPairsFound.toFloat() / boardSize.getNumPairs(),
                ContextCompat.getColor(this, R.color.color_progress_none),
                ContextCompat.getColor(this, R.color.color_progress_full)
            ) as Int
            binding.tvNumPairs.setTextColor(color)

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