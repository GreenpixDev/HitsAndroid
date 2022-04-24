package ru.hits.android.axolot.interpretator.code

class Data : CodeObj() {
    var dataType = ""
    var data = ""
    fun toDouble():Double{
        if(this.dataType == "double" || this.dataType == "int")
        {
            if(this.data.toDoubleOrNull() == null)
            {
                return 0.0;
            }
            return this.data.toDouble()
        }
        return 0.0;
    }
    fun toData(data:Double){
        if(this.dataType == "double"){
            this.data = data.toString()
        }
        if(this.dataType == "int"){
            this.data = data.toInt().toString()
        }
    }
}