package com.sasinduprasad.snapzzle.data.puzzle

import android.graphics.Bitmap
import androidx.room.*
import com.sasinduprasad.snapzzle.data.user.User

@Entity(
    tableName = "puzzle",
)
data class Puzzle(
    @PrimaryKey(autoGenerate = true) val pid: Long? = null,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "reward") val reward: Int,
    @ColumnInfo(name = "is_published") val isPublished: Boolean,
    @ColumnInfo(name = "bitmaps") val bitmaps: List<ByteArray>,
    @ColumnInfo(name = "original_bitmap") val originalBitmap: ByteArray,
)
