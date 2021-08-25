package com.myproject.mymemorygame.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myproject.mymemorygame.databinding.CardImageBinding
import com.myproject.mymemorygame.models.BoardSize
import kotlin.math.min

class ImagePickerAdapter(
    private val context: Context,
    private val imageUris: List<Uri>,
    private val boardSize: BoardSize
) : RecyclerView.Adapter<ImagePickerAdapter.ImagePickerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagePickerViewHolder {
        val binding = CardImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val cardWidth = parent.width / boardSize.getWidth()
        // Max allowable card height: height / num of rows
        val cardHeight = parent.height / boardSize.getHeight()
        val cardSideLength = min(cardWidth, cardHeight)

        val layoutParams = binding.ivCustomImage.layoutParams
        layoutParams.width = cardSideLength
        layoutParams.height = cardHeight

        return ImagePickerViewHolder(binding)
    }

    override fun getItemCount() = boardSize.getNumPairs()

    override fun onBindViewHolder(holder: ImagePickerViewHolder, position: Int) {
        // If the position that we are binding is less than the size of imageUris, that means that
        // the user has actually picked an image for this position and we should show that image
        if (position < imageUris.size) {
            holder.bind(imageUris[position])

        } else {
            holder.bind()
        }
    }

    inner class ImagePickerViewHolder(binding: CardImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val ivCustomImage = binding.ivCustomImage

        fun bind(uri: Uri) {
            ivCustomImage.setImageURI(uri)
        }

        fun bind() {
            TODO("Not yet implemented")
        }

    }
}
