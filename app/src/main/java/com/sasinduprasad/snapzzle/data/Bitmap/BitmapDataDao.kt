package com.sasinduprasad.snapzzle.data.Bitmap

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BitmapDataDao {

    @Insert
    suspend fun insertAll(bitmapData: List<BitmapData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bitmapData: BitmapData)

    @Query("SELECT * FROM bitmap_data WHERE puzzle_id = :puzzleId")
    suspend fun getBitmapsByPuzzleId(puzzleId: Long): List<BitmapData>

    @Query("DELETE FROM bitmap_data WHERE puzzle_id =:puzzleId")
    suspend fun deleteBitmapsByPuzzleId(puzzleId: Long)
}
