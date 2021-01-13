package com.example.myportfolio.room

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class Converters {

    @TypeConverter
    fun fromBitmap(bitmap : Bitmap) : ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100 , stream)
        return stream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray) : Bitmap = BitmapFactory.decodeByteArray(byteArray, 0 , byteArray.size)
}