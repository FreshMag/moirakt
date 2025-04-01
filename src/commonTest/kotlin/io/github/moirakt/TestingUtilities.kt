package io.github.moirakt

import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe

object TestingUtilities {
    fun DiscreteDistribution.shouldBeAboutTheSameAs(
        other: Map<Int, Double>,
        tolerance: Double = 1e-9,
    ) {
        probabilities.forEach { (key, value) ->
            value shouldBe (other[key]!! plusOrMinus tolerance)
        }
    }

    infix fun DiscreteDistribution.shouldBeAboutTheSameAs(other: Map<Int, Double>) {
        shouldBeAboutTheSameAs(other, 1e-9)
    }
}
