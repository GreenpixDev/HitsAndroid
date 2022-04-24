package ru.hits.android.axolot.interpretator

import ru.hits.android.axolot.interpretator.code.CodeObj
import ru.hits.android.axolot.interpretator.code.Data
import ru.hits.android.axolot.interpretator.code.LocalMemory

class Interpretator {

    private var code: Array<String> = arrayOf()
    var memory = LocalMemory()
    var workMemory = memory
    var dataTypeArray: MutableList<String> = mutableListOf();

    fun findCodeObj(memory: LocalMemory, name: String) : CodeObj? {
        var tempMem = memory
        while (true) {
            if(name in tempMem.memory.keys)
            {
                return memory.memory[name]
            }
            if(tempMem.parentMemory == null)
            {
                break;
            }
            else{
                tempMem;
            }
        }//TODO рек поиск
        return null;
    }

    fun init(){
        dataTypeArray.add("int")
        dataTypeArray.add("bool")
        dataTypeArray.add("double")
        dataTypeArray.add("char")
    }

    fun setCode(code : Array<String>){
        this.code = code

    }

    fun printError(error : String) {
        print("\n")
        print(error)
        print(" ")
        print(this.pointer)
        print(" ")
        print(code[pointer])
        print("\n")
    }

    fun arithmeticExpression(exp:String, answ:Data):Data{
        var parsExpr: MutableList<String> = mutableListOf()
        var i = 0
        while(i < exp.length){
            when(exp[i]){
                '+' -> {
                    parsExpr.add("+")
                }
                '-' -> {
                    parsExpr.add("-")
                }
                '%' -> {
                    parsExpr.add("%")
                }
                '(' -> {
                    parsExpr.add("(")
                }
                ')' -> {
                    parsExpr.add(")")
                }
                '[' -> {
                    parsExpr.add("[")
                }
                ']' -> {
                    parsExpr.add("]")
                }
                '/' -> {
                    if(exp.length > i + 1){
                        if(exp[i + 1] == '/'){
                            i++
                            parsExpr.add("//")
                        }
                        else{
                            parsExpr.add("/")
                        }
                    }
                    else {
                        printError("Math Exp Error.")
                    }
                }
                '*' -> {
                    parsExpr.add("*")
                }
                else -> {
                    if(parsExpr.isEmpty())
                    {
                        parsExpr.add("")
                    }
                    else if(parsExpr[parsExpr.lastIndex] in "+-*//%()[]")
                    {
                        parsExpr.add("")
                    }
                    parsExpr[parsExpr.lastIndex] = parsExpr[parsExpr.lastIndex] + exp[i].toString()
                }
            }
            i++
        }
        var parsExprUp: MutableList<String> = mutableListOf()

        i = 0
        while(i < parsExpr.size){
            if(parsExpr[i] in "+-//*%"){
                parsExprUp.add(parsExpr[i])
            }
            else if(parsExpr[i].toDoubleOrNull() != null){
                parsExprUp.add(parsExpr[i])
            }
            else if(parsExpr[i] in "[(") {
                var tempMass: MutableList<String> = mutableListOf()
                parsExprUp.add(parsExpr[i])
                tempMass.add(parsExpr[i])
                i++
                while(tempMass.isNotEmpty() && i < parsExpr.size) {
                    if (parsExpr[i] == "[") {
                        tempMass.add("[")
                    }
                    if (parsExpr[i] == "(") {
                        tempMass.add(")")
                    }
                    if (parsExpr[i] == "]") {
                        if (tempMass[tempMass.lastIndex] == "[") {
                            tempMass.removeAt(tempMass.lastIndex)
                        } else {
                            printError("MATH ERROR")
                        }
                    }
                    if (parsExpr[i] == ")") {
                        if (tempMass[tempMass.lastIndex] == "(") {
                            tempMass.removeAt(tempMass.lastIndex)
                        } else {
                            printError("MATH ERROR")
                        }
                    }
                    parsExprUp[parsExprUp.size - 1] = parsExprUp[parsExprUp.size - 1] + parsExpr[i]
                    i++;
                }
            }
            else if(i + 1 < parsExpr.size)
            {
                if(parsExpr[i + 1] in "([")
                {
                    var tempMass: MutableList<String> = mutableListOf()
                    parsExprUp.add(parsExpr[i])
                    i++
                    parsExprUp[parsExprUp.lastIndex] = parsExprUp[parsExprUp.lastIndex] + (parsExpr[i])
                    tempMass.add(parsExpr[i])
                    i++
                    while(tempMass.isNotEmpty() && i < parsExpr.size)
                    {
                        if(parsExpr[i] == "["){
                            tempMass.add("[")
                        }
                        if(parsExpr[i] == "("){
                            tempMass.add(")")
                        }
                        if(parsExpr[i] == "]"){
                            if(tempMass[tempMass.lastIndex] == "[")
                            {
                                tempMass.removeAt(tempMass.lastIndex)
                            }
                            else {
                                printError("MATH ERROR")
                            }
                        }
                        if(parsExpr[i] == ")"){
                            if(tempMass[tempMass.lastIndex] == "(")
                            {
                                tempMass.removeAt(tempMass.lastIndex)
                            }
                            else {
                                printError("MATH ERROR")
                            }
                        }
                        parsExprUp[parsExprUp.size - 1] = parsExprUp[parsExprUp.size - 1] + parsExpr[i]
                        i++;
                    }
                }
                else{
                    parsExprUp.add(parsExpr[i])
                }
            }

            else{
                parsExprUp.add(parsExpr[i])
            }
            i++
        }

        var doubleMath: MutableList<String> = mutableListOf()
        i = 0
        while(i < parsExprUp.size){
            when(parsExprUp[i]) {
                "(" -> {
                    doubleMath.add(parsExprUp[i])
                }
                ")" -> {
                    doubleMath.add(parsExprUp[i])
                }
                else -> {
                    if(parsExprUp[i].toDoubleOrNull() != null)
                    {
                        doubleMath.add(parsExprUp[i])
                    }
                    else if(!("." in parsExprUp[i]) and !("(" in parsExprUp[i]) and !(")" in parsExprUp[i]) and !("[" in parsExprUp[i]) and !("]" in parsExprUp[i]) and !("+" in parsExprUp[i]) and !("-" in parsExprUp[i]) and !("*" in parsExprUp[i]) and !("/" in parsExprUp[i]) and !("%" in parsExprUp[i])){
                        var temp = findCodeObj(workMemory, parsExprUp[i])
                        if (temp != null) {
                            if (temp is Data) {
                                var data = temp as Data

                                doubleMath.add(data.toDouble().toString())
                            }
                            else {
                                printError("math error")
                            }
                        }
                        else {
                            printError("math error")
                        }
                    }
                    else {

                        if(parsExprUp[i][0] == '(')
                        {
                            var dat = Data()
                            dat.dataType = "double"
                            doubleMath.add(this.arithmeticExpression( parsExprUp[i].substring(1, parsExprUp[i].lastIndex), Data()).toString());
                        }
                        else
                        {
                            doubleMath.add(parsExprUp[i])
                            //TODO логика массивов и функций
                        }
                    }
                }
            }
            i++
        }
        var polishStr: MutableList<String> = mutableListOf()//польская запись
        var stackOperathion: MutableList<String> = mutableListOf()
        i = 0
        while(i < doubleMath.size){
            if(!(doubleMath[i] in "+-*//%")) {
                polishStr.add(doubleMath[i])
            }
            else{
                while(stackOperathion.isNotEmpty() ) {
                    if(((doubleMath[i] in "*//%" && stackOperathion[stackOperathion.lastIndex] in "+-")))
                    {
                        break;
                    }
                    polishStr.add(stackOperathion[stackOperathion.lastIndex])
                    stackOperathion.removeAt(stackOperathion.lastIndex)
                }
                stackOperathion.add(doubleMath[i])
            }
            i++
        }
        while(stackOperathion.isNotEmpty()) {
            polishStr.add(stackOperathion[stackOperathion.lastIndex])
            stackOperathion.removeAt(stackOperathion.lastIndex)
        }
        i = 0
        var stack: MutableList<String> = mutableListOf()
        while(i < polishStr.size){
            if(polishStr[i] in "+-*//%"){
                var a = stack[stack.lastIndex].toDouble()
                stack.removeAt(stack.lastIndex)
                var b = stack[stack.lastIndex].toDouble()
                stack.removeAt(stack.lastIndex)
                when(polishStr[i]) {
                    "+" -> {
                        stack.add((b + a).toString())
                    }
                    "-" -> {
                        stack.add((b - a).toString())
                    }
                    "*" -> {
                        stack.add((b * a).toString())
                    }
                    "/" -> {
                        stack.add((b.toInt() / a.toInt()).toString())
                    }
                    "//" -> {
                        stack.add((b / a).toString())
                    }
                    "%" -> {
                        stack.add((b.toInt() % a.toInt()).toString())
                    }
                }
            }
            else{
                stack.add(polishStr[i])
            }
            i++
        }

        answ.toData(stack[stack.lastIndex].toDouble())
        return answ
    }

    private var pointer : Int = 0

    fun run(){
        while(this.pointer < this.code.size)
        {
            var parseString = this.code[pointer].split(" ")

            if(parseString.isNotEmpty()){
                when(parseString[0]){
                    "create" -> {
                        if(parseString.size > 1) {
                            when(parseString[1]) {
                                "data" -> {
                                    if(parseString.size > 3) {
                                        if(parseString[2] in this.dataTypeArray) {
                                            for(i in 3 until parseString.size)
                                            {
                                                var newData = Data()
                                                newData.dataType = parseString[2]
                                                if (parseString[i] in this.workMemory.memory.keys) {
                                                    printError("this param is exist")
                                                } else {
                                                    this.workMemory.memory.put(parseString[i], newData)
                                                }
                                            }
                                        }
                                        else{
                                            printError("unknown data type")
                                        }
                                    }
                                    else {
                                        printError("unknown data type or name")
                                    }
                                }
                                "class" -> {

                                }
                                "function" -> {

                                }
                                "obj" -> {

                                }
                                else -> {
                                    printError("unknown type create")
                                }
                            }
                        }
                        else {
                            printError("undef create")
                        }
                    }
                    "memDeb" ->{
                        for(i in workMemory.memory){
                            print(i.key.toString())
                            print(" : ")
                            print((workMemory.memory[i.key] as Data).toDouble())
                            print("---\n")
                        }
                        print("-----------------------MEME\n\n\n")
                    }
                    else -> {
                        var temp = findCodeObj(this.workMemory, parseString[0])

                        if(temp == null){
                            printError("error undef ")
                        }
                        else {
                            if(parseString.size == 3){
                                if(parseString[1] == "=" && temp is Data) {
                                    var tempArmf = parseString[2]
                                    var result = Data()
                                    result = arithmeticExpression(tempArmf, temp as Data)
                                    temp = temp as Data
                                    temp.data = result.data
                                }
                                else{
                                    printError("=")
                                }
                            }
                            else{
                                printError("= error")
                            }
                        }
                    }
                }
            }
            this.pointer++
        }

    }
}
/*
create data int a b c
b = 0
c = 0
a = b+c
a = 5/10
create data int b
b = 0
create data int c
c = 0
a = 5
memDeb
c = 2+2*2
memDeb
b = a+c
memDeb
log print a


* */