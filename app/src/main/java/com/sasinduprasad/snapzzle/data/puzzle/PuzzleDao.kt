package com.sasinduprasad.snapzzle.data.puzzle

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PuzzleDao {

    @Query("SELECT * FROM puzzle")
    fun getAll(): List<Puzzle>

    @Query("SELECT * FROM puzzle WHERE pid = :puzzleId")
    suspend fun getById(puzzleId:Long):Puzzle

    @Insert
    suspend fun insert(puzzle: Puzzle)

    @Delete
    suspend fun delete(puzzle: Puzzle)
}