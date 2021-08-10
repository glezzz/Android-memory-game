package com.myproject.mymemorygame.models

import com.myproject.mymemorygame.utils.DEFAULT_ICONS

class MemoryGame(private val boardSize: BoardSize) {

    val cards: List<MemoryCard>
    val numPairsFound = 0

    init {
        // Get 4 distinct images
        val chosenImages = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())

        // Double up those images & randomize it
        val randomizedImages = (chosenImages + chosenImages).shuffled()
        cards = randomizedImages.map { MemoryCard(it) }
    }
}