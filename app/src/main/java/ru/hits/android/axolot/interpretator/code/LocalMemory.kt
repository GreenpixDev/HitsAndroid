package ru.hits.android.axolot.interpretator.code

class LocalMemory : CodeObj() {
    init{
        this.type = "localMemory"
    }
    var memory = mutableMapOf<String, CodeObj>()
    var parentMemory : LocalMemory? = null

//    fun setParentMemory(parentMemory: LocalMemory){
//        this.parentMemory = parentMemory
//    }
}