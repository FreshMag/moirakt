package io.github.moirakt

open class DiscreteRandomVariable(
    val probabilities: DiscreteDistribution,
) {
    constructor(probabilities: Map<Int, Double>) : this(DiscreteDistribution(probabilities))
    constructor(vararg probabilities: Pair<Int, Double>) : this(probabilities.toMap())
    constructor(vararg probabilities: Double) : this(probabilities.mapIndexed { index, p -> index to p }.toMap())

    /**
     * Convolves this discrete random variable with another,
     * returning the probability distribution of their sum.
     */
    operator fun plus(other: DiscreteRandomVariable): DiscreteRandomVariable =
        DiscreteRandomVariable(probabilities convolve other.probabilities)

    /**
     * Prints the probability distribution in a readable format.
     */
    fun printDistribution() {
        probabilities.print()
    }
}
