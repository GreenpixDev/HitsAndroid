package ru.hits.android.axolot.console

import ru.hits.android.axolot.blueprint.FrontendConsole
import java.util.*

class Console(private val executor: (() -> Unit) -> Unit) : FrontendConsole {

    private var listener: (String) -> Unit = {}

    private val toApp: Queue<String> = LinkedList()

    private fun sendStringFromUser(str: String?) {
        toApp.add(str)
    }

    fun sendStringToUser(str: String?) {
        str?.let {
            executor.invoke {
                listener.invoke(it)
            }
        }
    }

    override fun send(inputString: String) {
        sendStringFromUser(inputString)
    }

    override fun setOnReceive(listener: (String) -> Unit) {
        this.listener = listener
    }

}