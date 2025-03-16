package com.sasinduprasad.snapzzle.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sasinduprasad.snapzzle.data.Bitmap.BitmapData
import com.sasinduprasad.snapzzle.data.Bitmap.BitmapDataDao
import com.sasinduprasad.snapzzle.data.Bitmap.PuzzleWithBitmaps
import com.sasinduprasad.snapzzle.data.puzzle.Puzzle
import com.sasinduprasad.snapzzle.data.puzzle.PuzzleDao
import com.sasinduprasad.snapzzle.data.user.User
import com.sasinduprasad.snapzzle.data.user.UserDao

@Database(
    entities = [Puzzle::class, User::class,BitmapData::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun puzzleDao(): PuzzleDao
    abstract fun bitmapDataDao(): BitmapDataDao

}
