package com.sasinduprasad.snapzzle.data.Bitmap

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import com.sasinduprasad.snapzzle.data.puzzle.Puzzle

data class PuzzleWithBitmaps(
    @Embedded val puzzle: Puzzle,
    @Relation(
        parentColumn = "pid",
        entityColumn = "puzzle_id"
    )
    val bitmaps: List<BitmapData>
)
