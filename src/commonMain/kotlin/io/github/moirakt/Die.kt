package io.github.moirakt

class Die(
    val sides: Int,
) : DiscreteRandomVariable(
        (1..sides).associateWith { 1.0 / sides },
    ) {
    override fun toString(): String = "D$sides"
}
