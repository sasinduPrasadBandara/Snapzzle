package com.sasinduprasad.snapzzle

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.sasinduprasad.snapzzle.data.AppDatabase
import com.sasinduprasad.snapzzle.data.puzzle.Puzzle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

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

    private val _puzzleList = MutableStateFlow<List<Puzzle>>(emptyList())
    val puzzleList = _puzzleList.asStateFlow()

    private val _puzzle = MutableStateFlow<Puzzle?>(null)
    val puzzle = _puzzle.asStateFlow()

    private val _originalbitmap = MutableStateFlow<Bitmap?>(null)
    val originalbitmap = _originalbitmap.asStateFlow()

    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
    val bitmaps = _bitmaps.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loadPuzzlesFromDb()
        }
    }

    fun savePuzzle(puzzle: Puzzle) {
        viewModelScope.launch(Dispatchers.IO) {
            puzzleDao.insert(puzzle)
            loadPuzzlesFromDb()
        }
    }


    private fun loadPuzzlesFromDb() {
        _puzzleList.value = puzzleDao.getAll()
    }


    fun deletePuzzle(puzzle: Puzzle) {
        viewModelScope.launch(Dispatchers.IO) {
            puzzleDao.delete(puzzle)
        }
    }


    @SuppressLint("NewApi")
    fun onTakePhoto(bitmap: Bitmap) {
        _originalbitmap.value = bitmap
    }

    fun generatePuzzle(originalBitmap: Bitmap) {

        val width = originalBitmap.width / 3
        val height = originalBitmap.height / 5


        val bitmapList = mutableListOf<Bitmap>()

        for (y in 0 until 5) {
            for (x in 0 until 3) {
                val resizedBmp =
                    Bitmap.createBitmap(originalBitmap, width * x, height * y, width, height)
                bitmapList.add(resizedBmp)
            }
        }


        if (bitmapList.isNotEmpty()) {
            val removeIndex = Random.nextInt(bitmapList.size)
            bitmapList.removeAt(removeIndex)
        }


        _bitmaps.value = bitmapList.shuffled()
    }

    fun loadPuzzleById(puzzleId:Long){
        viewModelScope.launch {
            _puzzle.value = puzzleDao.getById(puzzleId)
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