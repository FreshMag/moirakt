package io.github.moirakt

import io.github.moirakt.TestingUtilities.shouldBeAboutTheSameAs
import io.kotest.core.spec.style.StringSpec

class SimpleDieSpec : StringSpec({

    "Simple die should have uniform distribution" {
        val die = Die(6)
        val expectedProbabilities = (1..6).associateWith { 1.0 / 6 }
        die.probabilities shouldBeAboutTheSameAs expectedProbabilities
    }

    "Sum of two simple dice should have correct distribution" {
        val d6 = Die(6)
        val dd6 = Die(6)
        val sumDices = d6 + dd6
        val expectedProbabilities =
            mapOf(
                2 to 1.0 / 36,
                3 to 2.0 / 36,
                4 to 3.0 / 36,
                5 to 4.0 / 36,
                6 to 5.0 / 36,
                7 to 6.0 / 36,
                8 to 5.0 / 36,
                9 to 4.0 / 36,
                10 to 3.0 / 36,
                11 to 2.0 / 36,
                12 to 1.0 / 36,
            )

        sumDices.probabilities shouldBeAboutTheSameAs expectedProbabilities
    }
})
