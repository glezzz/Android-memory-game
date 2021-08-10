package com.myproject.mymemorygame

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MemoryBoardAdapter(private val context: Context, private val numPieces: Int) :
    RecyclerView.Adapter<MemoryBoardAdapter.ViewHolder>() {
    // ViewHolder: Object which provides access to all the views of one rv element. In this case 1 memory card.

    /**
     * Responsible for figuring out how to create one view of rv
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // This is the view that is created
        val view = LayoutInflater.from(context).inflate(R.layout.memory_card, parent, false)
        // return the view wrapped inside of a viewHolder
        return ViewHolder(view)
    }

    /**
     * How many elements are in rv
     */
    override fun getItemCount() = numPieces

    /**
     * Responsible for taking the data at position at binding it to ViewHolder
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            // No-op
        }
    }
}
