package com.marijannovak.autismhelper.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream

class ImageHelper {
    companion object {
        fun getImagePath(activity: Context?, uri: Uri): String {
            val cursor = activity?.contentResolver?.query(uri, null, null, null, null);
            cursor?.let {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                val result = cursor.getString(idx)
                cursor.close()
                return result
            }
            return uri.path
        }

        fun saveBitmap(activity: Context?, loadedBitmap: Bitmap?, filename: String): File {
            val file = File(activity!!.filesDir, filename)
            if (!file.exists() && loadedBitmap != null) {
                try {
                    val scaledBitmap = scaleBitmap(loadedBitmap)
                    val outStream = FileOutputStream(file)
                    scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
                    outStream.flush()
                    outStream.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return file
        }


       fun scaleBitmap(loadedBitmap: Bitmap?): Bitmap {
            var bitmap = loadedBitmap
            var width = bitmap!!.width
            var height = bitmap.height
            val maxSize = 96
            when {
                width > height -> {
                    // landscape
                    val ratio = width.toFloat() / maxSize
                    width = maxSize
                    height = (height / ratio).toInt()
                }
                height > width -> {
                    // portrait
                    val ratio = height.toFloat() / maxSize
                    height = maxSize
                    width = (width / ratio).toInt()
                }
                else -> {
                    // square
                    height = maxSize
                    width = maxSize
                }
            }

            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)
            return bitmap
        }
    }
}