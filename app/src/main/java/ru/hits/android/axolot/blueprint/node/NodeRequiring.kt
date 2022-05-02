package ru.hits.android.axolot.blueprint.node

import ru.hits.android.axolot.util.Dependencies

interface NodeRequiring : Node {

    val dependencies: Dependencies

}