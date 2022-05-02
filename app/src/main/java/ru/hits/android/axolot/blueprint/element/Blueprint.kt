package ru.hits.android.axolot.blueprint.element

class Blueprint {

    val blocks: Map<String, BlueprintBlock>

    init {
        blocks = mapOf(
            "for" to BlueprintMacros(),
            "while" to BlueprintMacros()
        )
    }
}