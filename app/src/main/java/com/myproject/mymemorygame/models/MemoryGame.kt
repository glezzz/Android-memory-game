package com.myproject.mymemorygame.models

import com.myproject.mymemorygame.utils.DEFAULT_ICONS

class MemoryGame(private val boardSize: BoardSize) {
    val cards: List<MemoryCard>
    var numPairsFound = 0

    private var numCardFlips = 0
    private var indexOfSingleSelectedCard: Int? = null

    init {
        // Get 4 distinct images
        val chosenImages = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())

        // Double up those images & randomize it
        val randomizedImages = (chosenImages + chosenImages).shuffled()
        cards = randomizedImages.map { MemoryCard(it) }
    }

    /**
     * Handles what happens in the state of the game when the card at position is flipped
     */
    fun flipCard(position: Int): Boolean {
        numCardFlips++
        val card = cards[position]

        // 3 cases:
        // 0 cards previously flipped over => restore cards + flip over the selected card
        // 1 card previously flipped over => flip over the selected card + check if the images match
        // 2 cards previously flipped over => restore cards + flip over the selected card
        var foundMatch = false
        if (indexOfSingleSelectedCard == null) {
            // 0 or 2 cards previously flipped over
            restoreCards()
            indexOfSingleSelectedCard = position

        } else {
            // exactly 1 card flipped over
            foundMatch = checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }

        // Opposite of what it was
        card.isFaceUp = !card.isFaceUp

        return foundMatch
    }

    /**
     * Returns boolean on whether those 2 positions on the board to identical images or not
     */
    private fun checkForMatch(position1: Int, position2: Int): Boolean {
        if (cards[position1].identifier != cards[position2].identifier) {
            return false
        }
        cards[position1].isMatched = true
        cards[position2].isMatched = true
        numPairsFound++

        return true
    }

    /**
     * Restores cards face down when the card is not matched
     */
    private fun restoreCards() {
        for (card in cards) {
            if (!card.isMatched) {
                card.isFaceUp = false
            }
        }
    }

    /**
     * Player has won the game
     */
    fun haveWonGame(): Boolean {
        return numPairsFound == boardSize.getNumPairs()
    }

    /**
     * Card is already face up, thus cannot be clicked again
     */
    fun isCardFaceUp(position: Int): Boolean {
        return cards[position].isFaceUp
    }

    /**
     * Get number of moves player has made. Doing integer truncation here.
     * 1 move == 2 card flips
     * e.g. If numCardFlips is 5, divided by 2, result will be 2
     */
    fun getNumMoves(): Int {
        return numCardFlips / 2
    }
}
