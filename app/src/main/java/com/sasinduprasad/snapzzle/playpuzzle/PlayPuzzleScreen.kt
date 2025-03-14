package com.sasinduprasad.snapzzle.playpuzzle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sasinduprasad.snapzzle.ImageView
import com.sasinduprasad.snapzzle.MainViewModel
import com.sasinduprasad.snapzzle.MainViewModelFactory
import com.sasinduprasad.snapzzle.home.byteArrayToBitmap

@Composable
fun PlayPuzzleScreen(
    onGameWin: () -> Unit,
    onGameGiveUp: () -> Unit,
    navController: NavHostController
) {
    val context = LocalContext.current

    val viewModel: MainViewModel = viewModel(
        factory = MainViewModelFactory(context.applicationContext)
    )

    // Get the current backstack entry and extract arguments
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val puzzleId = currentNavBackStackEntry?.arguments?.getLong("puzzleId")

    // Load puzzle by ID when available
    LaunchedEffect(puzzleId) {
        if (puzzleId != null) {
            viewModel.loadPuzzleById(puzzleId)
        }
    }

    // Observe puzzle state from ViewModel
    val puzzle by viewModel.puzzle.collectAsState()
    val bitmaps = puzzle?.bitmaps?.map { byteArrayToBitmap(it) } ?: emptyList()

    // Display images once bitmaps are available
    if (bitmaps.isNotEmpty()) {
        ImageView(bitmaps)
    }
}
