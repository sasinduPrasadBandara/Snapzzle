package com.sasinduprasad.snapzzle.data.puzzle

import androidx.room.*
import com.sasinduprasad.snapzzle.data.Difficulty

@Entity(
    tableName = "puzzle",
)
data class Puzzle(
    @PrimaryKey(autoGenerate = true) val pid: Long? = null,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "reward") val reward: Int,
    @ColumnInfo(name = "duration") val duration: Int,
    @ColumnInfo(name = "difficulty") val difficulty: Difficulty,
    @ColumnInfo(name = "is_win") val isWin: Boolean,
    @ColumnInfo(name = "original_bitmap") val originalBitmap: ByteArray,
)
