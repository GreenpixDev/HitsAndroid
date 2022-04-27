package ru.hits.android.axolot.executor

import ru.hits.android.axolot.executor.node.NodeExecutable

class Interpreter {

    fun execute(executable: NodeExecutable?) {
        var current = executable

        while(current != null) {
            current = current.invoke()
        }
    }
}