package ru.hits.android.axolot.console

import android.os.Handler
import android.os.Looper
import ru.hits.android.axolot.blueprint.FrontendConsole
import java.util.*

class Console : FrontendConsole {

    private var listener: (String) -> Unit = {}

    private val toApp: Queue<String> = LinkedList<String>()

    private fun sendStringFromUser(str: String?) {
        toApp.add(str)
    }

    fun sendStringToUser(str: String?) {
        str?.let {
            Handler(Looper.getMainLooper()).post {
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