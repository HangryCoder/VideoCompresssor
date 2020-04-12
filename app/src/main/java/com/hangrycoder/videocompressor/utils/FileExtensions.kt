package com.hangrycoder.videocompressor.utils

import java.io.File

fun File.createFolderIfDoesntExist() {
    if (!exists()) {
        mkdirs()
    }
}