package ru.hits.android.axolot.blueprint

class FrontendConsoleMock : FrontendConsole {
    override fun send(inputString: String) {
        println("send $inputString")
    }

    override fun setOnReceive(listener: (String) -> Unit) {
    }


}