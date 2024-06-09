package com.capstone.nutrieasy.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.view.View
import androidx.core.net.toUri
import androidx.core.view.marginLeft
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

fun createCustomTempFile(context: Context): File {
    val filesDir = context.externalCacheDir
    return File.createTempFile(timeStamp, ".jpg", filesDir)
}

fun uriToFile(imageUri: Uri, context: Context): File {
    val myFile = createCustomTempFile(context)
    val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
    val outputStream = FileOutputStream(myFile)
    val buffer = ByteArray(1024)
    var length: Int
    while (inputStream.read(buffer).also { length = it } > 0){
        outputStream.write(buffer, 0, length)
    }

    inputStream.close()
    outputStream.close()
    return myFile
}

fun cropImage(bitmap: Bitmap, frame: View, reference: View): ByteArray {
//    val heightOriginal = frame.measuredHeight
//    val widthOriginal = frame.measuredWidth
//    val heightFrame = reference.measuredHeight
//    val widthFrame = reference.measuredWidth
//    val leftFrame = reference.left
//    val topFrame = reference.top
//    val heightReal = bitmap.height
//    val widthReal = bitmap.width
//    val widthFinal = widthFrame * widthReal / widthOriginal
//    val heightFinal = heightFrame * heightReal / heightOriginal
//    val leftFinal = leftFrame * widthReal / widthOriginal
//    val topFinal = topFrame * heightReal / heightOriginal
    val leftFinal = (bitmap.width / 2) - (reference.width / 2)
    val topFinal = (bitmap.height / 2) - (reference.height / 2)
    val widthFinal = reference.width
    val heightFinal = reference.height
    val bitmapFinal = Bitmap.createBitmap(
        bitmap,
        leftFinal, topFinal, widthFinal, heightFinal
    )
    val stream = ByteArrayOutputStream()
    bitmapFinal.compress(
        Bitmap.CompressFormat.JPEG,
        100,
        stream
    )
    return stream.toByteArray()
}

fun saveImage(bytes: ByteArray, context: Context) : Uri {
    val outStream: FileOutputStream
    val fileName = "SCAN" + System.currentTimeMillis() + ".jpg"
    val directoryName = createAppDirectoryInDCIM(context)
    val file = File(directoryName!!, fileName)

    if (!directoryName.exists()) {
        directoryName.mkdirs()
    }

    try {
        file.createNewFile()
        outStream = FileOutputStream(file)
        outStream.write(bytes)
        MediaScannerConnection.scanFile(
            context,
            arrayOf(file.path),
            arrayOf("image/jpeg"), null
        )
        outStream.close()
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return file.toUri()
}

fun createAppDirectoryInDCIM(context: Context): File? {
    val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
    val appDirectory = File(downloadsDirectory, "Nutrieasy")

    if (!appDirectory.exists()) {
        val directoryCreated = appDirectory.mkdir()
        if (!directoryCreated) {
            return null
        }
    }

    return appDirectory
}

fun Bitmap.rotate(degree:Int):Bitmap{
    val matrix = Matrix()

    matrix.postRotate(degree.toFloat())

    val scaledBitmap = Bitmap.createScaledBitmap(
        this,
        width,
        height,
        true
    )

    return Bitmap.createBitmap(
        scaledBitmap,
        0,
        0,
        scaledBitmap.width,
        scaledBitmap.height,
        matrix,
        true
    )
}