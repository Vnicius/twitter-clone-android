package io.github.vnicius.twitterclone.data.local.filemanage

import android.app.Application
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.lang.Exception

class FileManager(val myApp: Application) {

    private val appContext = myApp.applicationContext

    suspend fun readData(fileName: String): Any? = withContext(Dispatchers.IO) {
        try {
            val fis = appContext.openFileInput(fileName)
            val objectInputStream = ObjectInputStream(fis)
            val data = objectInputStream.readObject()
            objectInputStream.close()
            fis.close()
            data
        } catch (e: FileNotFoundException) {
            null
        } catch (e: EOFException) {
            null
        }
    }

    suspend fun saveData(fileName: String, data: Any): Boolean = withContext(Dispatchers.IO) {
        try {
            val fos: FileOutputStream = appContext.openFileOutput(fileName, Context.MODE_PRIVATE)
            val os = ObjectOutputStream(fos)
            os.writeObject(data)
            os.close()
            fos.close()
            true
        } catch (e: Exception) {
            false
        }
    }
}