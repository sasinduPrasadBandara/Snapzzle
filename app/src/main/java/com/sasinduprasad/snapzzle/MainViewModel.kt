package com.sasinduprasad.snapzzle

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import androidx.room.withTransaction
import com.sasinduprasad.snapzzle.data.AppDatabase
import com.sasinduprasad.snapzzle.data.Bitmap.BitmapData
import com.sasinduprasad.snapzzle.data.Difficulty
import com.sasinduprasad.snapzzle.data.puzzle.Position
import com.sasinduprasad.snapzzle.data.puzzle.Puzzle
import com.sasinduprasad.snapzzle.data.puzzle.PuzzleUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class MainViewModel(context: Context) : ViewModel() {

    val db by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "snapzzle_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }


    val puzzleDao = db.puzzleDao()
    val bitmapDataDao = db.bitmapDataDao()

    private val _puzzleList = MutableStateFlow<List<PuzzleUi>>(emptyList())
    val puzzleList = _puzzleList.asStateFlow()

    private val _puzzle = MutableStateFlow<PuzzleUi?>(null)
    val puzzle = _puzzle.asStateFlow()

    private val _originalbitmap = MutableStateFlow<Bitmap?>(null)
    val originalbitmap = _originalbitmap.asStateFlow()

    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
    val bitmaps = _bitmaps.asStateFlow()


    fun savePuzzle(puzzle: Puzzle, originalBitmap: Bitmap, bitmaps: List<Bitmap>) {
        viewModelScope.launch(Dispatchers.IO) {

            val compressedOriginalBytes = compressBitmap(originalBitmap)


            val puzzleWithCompressedBitmap = puzzle.copy(
                originalBitmap = compressedOriginalBytes
            )

            db.withTransaction {
                val puzzleId = puzzleDao.insert(puzzleWithCompressedBitmap)

                bitmaps.chunked(5).forEach { bitmapChunk ->
                    val bitmapDataList = bitmapChunk.map { bitmap ->
                        BitmapData(
                            puzzleId = puzzleId,
                            bitmap = compressBitmap(bitmap)
                        )
                    }
                    bitmapDataDao.insertAll(bitmapDataList)
                }
            }

        }
    }

    private fun compressBitmap(bitmap: Bitmap): ByteArray {

        val maxDimension = 1024
        val scaledBitmap = if (bitmap.width > maxDimension || bitmap.height > maxDimension) {
            val scale = maxDimension.toFloat() / maxOf(bitmap.width, bitmap.height)
            val newWidth = (bitmap.width * scale).toInt()
            val newHeight = (bitmap.height * scale).toInt()
            Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
        } else {
            bitmap
        }

        // Compress with lower quality
        return ByteArrayOutputStream().use { stream ->
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 25, stream)
            stream.toByteArray()
        }
    }

    fun loadPuzzlesFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            val puzzlesWithBitmaps = puzzleDao.getPuzzlesWithBitmaps()

            val puzzleUiList = puzzlesWithBitmaps.map { puzzleWithBitmaps ->

                val originalBitmap = BitmapFactory.decodeByteArray(
                    puzzleWithBitmaps.puzzle.originalBitmap,
                    0,
                    puzzleWithBitmaps.puzzle.originalBitmap.size
                )


                val bitmaps = puzzleWithBitmaps.bitmaps.mapNotNull { bitmapData ->
                    BitmapFactory.decodeByteArray(bitmapData.bitmap, 0, bitmapData.bitmap.size)
                }

                val positionMap = puzzleWithBitmaps.bitmaps.mapIndexed { index, bitmapData ->
                    bitmapData.bid?.let {
                        Position(
                            bitmapId = it,
                            position = index
                        )
                    }
                }

                PuzzleUi(
                    pid = puzzleWithBitmaps.puzzle.pid,
                    createdAt = puzzleWithBitmaps.puzzle.createdAt,
                    userId = puzzleWithBitmaps.puzzle.userId,
                    reward = puzzleWithBitmaps.puzzle.reward,
                    isWin = puzzleWithBitmaps.puzzle.isWin,
                    originalBitmap = originalBitmap,
                    bitmaps = puzzleWithBitmaps.bitmaps,
                    positionMap = positionMap,
                    difficulty = puzzleWithBitmaps.puzzle.difficulty,
                    duration = 0
                )
            }


            _puzzleList.value = puzzleUiList
        }
    }

    fun deletePuzzle(puzzle: PuzzleUi) {
        viewModelScope.launch(Dispatchers.IO) {
            bitmapDataDao.deleteBitmapsByPuzzleId(puzzle.pid!!)
            puzzleDao.delete(puzzle.pid)
            loadPuzzlesFromDb()
        }
    }

    @SuppressLint("NewApi")
    fun onTakePhoto(bitmap: Bitmap) {
        _originalbitmap.value = bitmap
    }

    fun generatePuzzle(originalBitmap: Bitmap, level: Difficulty) {
        val columnCount = when (level) {
            Difficulty.EASY -> 2
            Difficulty.MEDIUM -> 3
            Difficulty.HARD -> 4
        }

        val rowCount = when (level) {
            Difficulty.EASY -> 4
            Difficulty.MEDIUM -> 5
            Difficulty.HARD -> 6
        }

        val width = originalBitmap.width / columnCount
        val height = originalBitmap.height / rowCount

        val bitmapList = mutableListOf<Bitmap>()

        for (y in 0 until rowCount) {
            for (x in 0 until columnCount) {  // Looping over columns inside row loop
                val resizedBmp = Bitmap.createBitmap(originalBitmap, width * x, height * y, width, height)
                bitmapList.add(resizedBmp)
            }
        }



        _bitmaps.value = bitmapList
    }


    fun loadPuzzleById(puzzleId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val puzzleWithBitmaps = puzzleDao.getPuzzleWithBitmapsById(puzzleId)

            puzzleWithBitmaps.let { puzzleData ->

                val originalBitmap = BitmapFactory.decodeByteArray(
                    puzzleData.puzzle.originalBitmap,
                    0,
                    puzzleData.puzzle.originalBitmap.size
                )


                val bitmaps = puzzleData.bitmaps.mapNotNull { bitmapData ->
                    BitmapFactory.decodeByteArray(bitmapData.bitmap, 0, bitmapData.bitmap.size)
                }

                val positionMap = puzzleWithBitmaps.bitmaps.mapIndexed { index, bitmapData ->
                    bitmapData.bid?.let {
                        Position(
                            bitmapId = it,
                            position = index
                        )
                    }
                }


                val puzzleUi = PuzzleUi(
                    pid = puzzleData.puzzle.pid,
                    createdAt = puzzleData.puzzle.createdAt,
                    userId = puzzleData.puzzle.userId,
                    reward = puzzleData.puzzle.reward,
                    isWin = puzzleData.puzzle.isWin,
                    originalBitmap = originalBitmap,
                    bitmaps = puzzleData.bitmaps,
                    positionMap = positionMap,
                    difficulty = puzzleWithBitmaps.puzzle.difficulty,
                    duration = 0
                )


                _puzzle.value = puzzleUi
            }
        }
    }

    fun updatePuzzle(duration:Int,puzzleId: Long){
        viewModelScope.launch {
            puzzleDao.update(duration,puzzleId)
        }
    }

    fun shuffle() {
        _bitmaps.value = _bitmaps.value.shuffled()
    }

    fun cleanUp() {
        _bitmaps.value = emptyList()
        _originalbitmap.value = null
    }


}