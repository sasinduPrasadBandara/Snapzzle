package com.sasinduprasad.snapzzle

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

class MainViewModel : ViewModel() {

    private val _originalbitmap = MutableStateFlow<Bitmap?>(null)
    val originalbitmap = _originalbitmap.asStateFlow()

    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
    val bitmaps = _bitmaps.asStateFlow()

    @SuppressLint("NewApi")
    fun onTakePhoto(bitmap: Bitmap) {
        _originalbitmap.value = bitmap
    }

    fun generatePuzzle(originalBitmap: Bitmap) {

        val width = originalBitmap.width / 3
        val height = originalBitmap.height / 5


        val bitmapList = mutableListOf<Bitmap>()

        for (y in 0 until 5) {
            for (x in 0 until 3) {
                val resizedBmp =
                    Bitmap.createBitmap(originalBitmap, width * x, height * y, width, height)
                bitmapList.add(resizedBmp)
            }
        }


        if (bitmapList.isNotEmpty()) {
            val removeIndex = Random.nextInt(bitmapList.size)
            bitmapList.removeAt(removeIndex)
        }


        _bitmaps.value = bitmapList.shuffled()
    }

    fun shuffle(){
        _bitmaps.value = _bitmaps.value.shuffled()
    }

    fun savePuzzle(){

    }

    fun cleanUp() {
        _bitmaps.value = emptyList()
        _originalbitmap.value = null
    }


}