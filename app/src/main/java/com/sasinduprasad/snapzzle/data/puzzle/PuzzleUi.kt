package com.sasinduprasad.snapzzle.data.puzzle

import android.graphics.Bitmap
import com.sasinduprasad.snapzzle.data.Difficulty
import com.sasinduprasad.snapzzle.data.Bitmap.BitmapData

data class PuzzleUi(
    val pid: Long? = null,
    val createdAt: Long,
    val userId: Long,
    val reward: Int,
    val difficulty: Difficulty,
    val duration: Int,
    val isWin: Boolean,
    val originalBitmap: Bitmap,
    val bitmaps: List<BitmapData>,
    val positionMap: List<Position?>,
)
