package com.sasinduprasad.snapzzle

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel: ViewModel() {

    private val _bitmaps = MutableStateFlow<Bitmap?>(null)
    val bitmaps = _bitmaps.asStateFlow()

    fun onTakePhoto(bitmap: Bitmap) {
        _bitmaps.value = bitmap
    }

    
}