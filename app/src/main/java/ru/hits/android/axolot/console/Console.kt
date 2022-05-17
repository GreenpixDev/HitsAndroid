package ru.hits.android.axolot.console

import java.util.*

class Console {

    val toUser : Queue<String> =  LinkedList<String>()
    val toApp : Queue<String> =  LinkedList<String>()

    fun getStringToApp():String{
        if(toApp.isEmpty())
        {
            return ""
        }
        return toApp.remove()
    }

    fun getStringToUser():String{
        if(toUser.isEmpty())
        {
            return ""
        }
        return toUser.remove()
    }

    fun sendStringToApp(str:String?){
        toApp.add(str)
    }

    fun sendStringToUser(str:String?){
        toUser.add(str)
    }

}