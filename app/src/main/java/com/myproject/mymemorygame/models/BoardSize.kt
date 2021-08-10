package com.myproject.mymemorygame.models

enum class BoardSize(val numCards: Int) {
    EASY(8),
    MEDIUM(18),
    HARD(24);

    fun getWidth(): Int {
        return when (this) {
            EASY -> 2
            MEDIUM -> 3
            HARD -> 4
        }
    }

    fun getHeight(): Int {

        // Height is only determined once we know numCards & width
        return numCards / getWidth()
    }

    /**
     * How many pairs are there?
     */
    fun getNumPairs(): Int {
        return numCards / 2
    }
}