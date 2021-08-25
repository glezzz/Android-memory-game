package com.myproject.mymemorygame.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.myproject.mymemorygame.R
import com.myproject.mymemorygame.databinding.MemoryCardBinding
import com.myproject.mymemorygame.models.BoardSize
import com.myproject.mymemorygame.models.MemoryCard
import kotlin.math.min

class MemoryBoardAdapter(
    private val context: Context,
    private val boardSize: BoardSize,
    private val cards: List<MemoryCard>,
    private val cardClickListener: CardClickListener
) :
    RecyclerView.Adapter<MemoryBoardAdapter.MemoryBoardViewHolder>() {
    // ViewHolder: Object which provides access to all the views of one rv element. In this case 1 memory card.

    companion object {
        private const val MARGIN_SIZE = 10
        private const val TAG = "MemoryBoardAdapter"
    }

    interface CardClickListener {
        fun onCardClicked(position: Int)
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

    inner class MemoryBoardViewHolder(binding: MemoryCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val imageButton = binding.ibImageCard

        fun bind(position: Int) {
            val memoryCard = cards[position]

            // If card isFaceUp then we use that as an image, otherwise we use the bg
            imageButton.setImageResource(if (memoryCard.isFaceUp) memoryCard.identifier else R.drawable.ic_launcher_background)

            // Change opacity of matched cards
            imageButton.alpha = if (memoryCard.isMatched) .4f else 1.0f
            // Change background of matched cards
            val colorStateList = if (memoryCard.isMatched) ContextCompat.getColorStateList(context, R.color.color_gray) else null
            ViewCompat.setBackgroundTintList(imageButton, colorStateList)

            imageButton.setOnClickListener {
                Log.i(TAG, "Clicked on position $position")
                cardClickListener.onCardClicked(position)
            }
        }
    }
}
