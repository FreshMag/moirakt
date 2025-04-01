package io.github.moirakt

import kotlin.math.abs
import kotlin.math.pow

class DiscreteDistribution(
    val probabilities: Map<Int, Double>,
) {
    init {
        val sum = probabilities.values.sum()
        require(abs(sum - 1.0) < TOLERANCE) {
            "Probabilities must sum to 1.0, but currently sum to $sum"
        }
    }

    infix fun convolve(other: DiscreteDistribution): DiscreteDistribution {
        val newProbabilities = mutableMapOf<Int, Double>()

        for ((x1, p1) in probabilities) {
            for ((x2, p2) in other.probabilities) {
                val key = x1 + x2
                newProbabilities[key] = (newProbabilities[key] ?: 0.0) + (p1 * p2)
            }
        }

        val total = newProbabilities.values.sum()
        val normalizedProbabilities = newProbabilities.mapValues { it.value / total }

        return DiscreteDistribution(normalizedProbabilities)
    }

    fun print() {
        probabilities.entries.sortedBy { it.key }.forEach { (value, probability) ->
            println("P(X = $value) = ${probability.format(FORMAT_DECIMALS)}")
        }
    }

    /**
     * Extension function to format a double to a string with `decimals` decimal places.
     */
    private fun Double.format(decimals: Int): String {
        val factor = 10.0.pow(decimals)
        return (kotlin.math.round(this * factor) / factor).toString()
    }

    companion object {
        private const val TOLERANCE = 1e-9

        private const val FORMAT_DECIMALS = 6
    }
}
