package com.sasinduprasad.snapzzle.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class Converters {

    // Convert Bitmap to ByteArray
    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    // Convert ByteArray to Bitmap
    @TypeConverter
    fun toBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    // Convert List<Bitmap> to List<ByteArray>
    @TypeConverter
    fun fromBitmapList(bitmapList: List<Bitmap>): List<ByteArray> {
        return bitmapList.map { fromBitmap(it) }
    }

    // Convert List<ByteArray> to List<Bitmap>
    @TypeConverter
    fun toBitmapList(byteArrayList: List<ByteArray>): List<Bitmap> {
        return byteArrayList.map { toBitmap(it) }
    }

    @TypeConverter
    fun fromByteArrayList(byteArrayList: List<ByteArray>): String {
        return byteArrayList.joinToString(separator = ",") { Base64.encodeToString(it, Base64.DEFAULT) }
    }

    @TypeConverter
    fun toByteArrayList(data: String): List<ByteArray> {
        return data.split(",").map { Base64.decode(it, Base64.DEFAULT) }
    }
}
