package ru.hits.android.axolot.blueprint

/**
 * Интерфейс по взаимодействию с консолью для UI
 * То есть тут будут приколы для меня.
 * @author Kostya
 */
interface FrontendConsole {
    /**
     * Отправляем в консоль, нажимаем Enter. То есть [inputString] - входные данные от пользователя
     */
    fun send(inputString: String)

    /**
     * Задать прослушку [listener] на получение данных от интерпретатора
     */
    fun setOnReceive(listener: (String) -> Unit)
}