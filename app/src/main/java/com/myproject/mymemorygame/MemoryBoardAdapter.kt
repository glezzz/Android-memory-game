package com.myproject.mymemorygame

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myproject.mymemorygame.databinding.MemoryCardBinding
import com.myproject.mymemorygame.models.BoardSize
import kotlin.math.min

class MemoryBoardAdapter(private val boardSize: BoardSize) :
    RecyclerView.Adapter<MemoryBoardAdapter.MemoryBoardViewHolder>() {
    // ViewHolder: Object which provides access to all the views of one rv element. In this case 1 memory card.

    companion object {
        private const val MARGIN_SIZE = 10
        private const val TAG = "MemoryBoardAdapter"
    }

    /**
     * Responsible for figuring out how to create one view of rv
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoryBoardViewHolder {
        val binding = MemoryCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        // parent: ViewGroup = rv, that's how we can get the width & height
        // parent.width / however many columns we have in the grid
        // Reduce it by the amount of margin on either side
        val cardWith = parent.width / boardSize.getWidth() - (2 * MARGIN_SIZE)
        val cardHeight = parent.height / boardSize.getHeight() - (2 * MARGIN_SIZE)

        // Mandate that every card is square
        val cardSideLength = min(cardWith, cardHeight)

        // This allows to change width & height     cast it to be able to set margins
        val layoutParams = binding.cvCard.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.width = cardSideLength
        layoutParams.height = cardSideLength

        // Set margin size all around card
        layoutParams.setMargins(MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE)

        return MemoryBoardViewHolder(binding)
    }

    /**
     * How many elements are in rv
     */
    override fun getItemCount() = boardSize.numCards

    /**
     * Responsible for taking the data at position at binding it to ViewHolder
     */
    override fun onBindViewHolder(holder: MemoryBoardViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class MemoryBoardViewHolder(private val binding: MemoryCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.ibImageCard.setOnClickListener {
                Log.i(TAG, "Clicked on position $position")
            }
        }
    }
}
