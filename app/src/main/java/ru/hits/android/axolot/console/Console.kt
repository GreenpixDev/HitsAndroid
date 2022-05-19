package ru.hits.android.axolot.console

import ru.hits.android.axolot.blueprint.FrontendConsole
import java.util.*

class Console : FrontendConsole {

    private var listener: (String) -> Unit = {}

    private val toApp: Queue<String> = LinkedList<String>()

    private fun sendStringFromUser(str: String?) {
        toApp.add(str)
    }

    fun sendStringToUser(str: String?) {
        str?.let { listener.invoke(it) }
    }


    fun getInApp(): String? {
        if (toApp.isEmpty()) {
            return null;
        }
        return toApp.remove()
    }

    override fun send(inputString: String) {
        sendStringFromUser(inputString)
    }

    override fun setOnReceive(listener: (String) -> Unit) {
        this.listener = listener
    }

}