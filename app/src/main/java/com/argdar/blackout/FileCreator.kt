package com.argdar.blackout

import java.io.File

class FileCreator(private val fileDir: String) {
    fun create(id: Long): File {
        return File("$fileDir/$id")
    }
}