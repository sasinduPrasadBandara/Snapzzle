package com.sasinduprasad.snapzzle.data.puzzle

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.sasinduprasad.snapzzle.data.Bitmap.PuzzleWithBitmaps

@Dao
interface PuzzleDao {

    @Query("SELECT * FROM puzzle")
    fun getAll(): List<Puzzle>

    @Transaction
    @Query("SELECT * FROM puzzle WHERE pid = :puzzleId")
    suspend fun getPuzzleWithBitmapsById(puzzleId: Long): PuzzleWithBitmaps

    @Transaction
    @Query("SELECT * FROM puzzle")
    suspend fun getPuzzlesWithBitmaps(): List<PuzzleWithBitmaps>

    @Insert
    suspend fun insert(puzzle: Puzzle): Long

    @Query("UPDATE puzzle SET duration = :duration, is_win = 1 WHERE pid = :puzzleId")
    suspend fun update(duration: Int, puzzleId: Long)

    @Query("DELETE FROM puzzle WHERE pid =:puzzleId")
    suspend fun delete(puzzleId: Long)
}