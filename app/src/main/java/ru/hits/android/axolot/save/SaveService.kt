package ru.hits.android.axolot.save

import ru.hits.android.axolot.blueprint.project.AxolotProgram
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class SaveService {

    fun saveProject(project: AxolotProgram, name: String){
        val objectOutputStream = ObjectOutputStream(FileOutputStream(name))
        objectOutputStream.writeObject(project);
        objectOutputStream.close()
    }

    fun loadProject(name: String): AxolotProgram{
        val objectInputStream = ObjectInputStream(FileInputStream(name))
        val project: AxolotProgram = objectInputStream.readObject() as AxolotProgram
        objectInputStream.close()
        return project
    }
}