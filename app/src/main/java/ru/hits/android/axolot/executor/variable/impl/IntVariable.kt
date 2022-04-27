package ru.hits.android.axolot.executor.variable.impl

import ru.hits.android.axolot.executor.variable.NumberVariable

class IntVariable : NumberVariable<Int> {

    override val type: VariableType
        get() = VariableType.INT

    override var value: Int? = 0
}