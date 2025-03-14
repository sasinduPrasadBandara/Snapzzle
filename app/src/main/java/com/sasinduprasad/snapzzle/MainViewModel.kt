package com.sasinduprasad.snapzzle

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

class MainViewModel : ViewModel() {

    private val _originalbitmaps = MutableStateFlow<Bitmap?>(null)
    val originalbitmaps = _originalbitmaps.asStateFlow()

    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
    val bitmaps = _bitmaps.asStateFlow()

    @SuppressLint("NewApi")
    fun onTakePhoto(bitmap: Bitmap) {
        Log.i("bitmap_width", bitmap.width.toString())
        Log.i("bitmap_height", bitmap.height.toString())
        Log.i("bitmap_byte_count", bitmap.byteCount.toString())
        Log.i("bitmap_allocationByteCount", bitmap.allocationByteCount.toString())
        Log.i("bitmap_colorSpace", bitmap.colorSpace.toString())
        Log.i("bitmap_config", bitmap.config.toString())
        Log.i("bitmap_generationId", bitmap.generationId.toString())
        Log.i("bitmap_density", bitmap.density.toString())
        Log.i("bitmap_rowBytes", bitmap.rowBytes.toString())

        _originalbitmaps.value = bitmap

        val width = bitmap.width / 3  // Each cell's width (3 columns)
        val height = bitmap.height / 5 // Each cell's height (5 rows)


        val bitmapList = mutableListOf<Bitmap>()

        for (y in 0 until 5) {  // Loop for rows (0,1,2,3,4)
            for (x in 0 until 3) {  // Loop for columns (0,1,2)
                val resizedBmp = Bitmap.createBitmap(bitmap, width * x, height * y, width, height)
                bitmapList.add(resizedBmp)  // Add to list
            }
        }


        if (bitmapList.isNotEmpty()) {
            val removeIndex = Random.nextInt(bitmapList.size)
            bitmapList.removeAt(removeIndex)
        }


        _bitmaps.value = bitmapList.shuffled()
    }


    fun cleanUp() {
        _bitmaps.value = emptyList()
    }


}