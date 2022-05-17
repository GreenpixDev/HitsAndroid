package ru.hits.android.axolot

class ConsoleLogic {
    fun input(inputString: String) {
        /*Этот метод будет вызываться из-за действий пользователя. То есть например пользователя ввел какую-либо команду в консоль
        * то у нас создастся TextView с этой командой, а после вызовется этот метод, чтобы как-то среагировать на команду*/
    }

    fun output(outputString: String) {
        /*По сути отсюда мы хотим выводить в консоль outputString, для этого нам нужно вызвать
        * ConsoleView.addTextViewToConsole*/
    }
}