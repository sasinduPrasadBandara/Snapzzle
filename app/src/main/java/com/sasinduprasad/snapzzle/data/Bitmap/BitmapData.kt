package com.sasinduprasad.snapzzle.data.Bitmap

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bitmap_data")
data class BitmapData(
    @PrimaryKey(autoGenerate = true) val bid: Long? = null,
    @ColumnInfo(name = "puzzle_id") val puzzleId: Long,
    @ColumnInfo(name = "bitmap") val bitmap: ByteArray,
)
