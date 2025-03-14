package com.sasinduprasad.snapzzle.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sasinduprasad.snapzzle.data.puzzle.Puzzle
import com.sasinduprasad.snapzzle.data.puzzle.PuzzleDao
import com.sasinduprasad.snapzzle.data.user.User
import com.sasinduprasad.snapzzle.data.user.UserDao

@Database(
    entities = [Puzzle::class, User::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun puzzleDao(): PuzzleDao
}
