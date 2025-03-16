package com.sasinduprasad.snapzzle.playpuzzle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sasinduprasad.snapzzle.MainViewModel
import com.sasinduprasad.snapzzle.MainViewModelFactory

@Composable
fun PlayPuzzleScreen(
    onGameWin: (puzzleId: Long,duration:Int) -> Unit,
    onGameGiveUp: () -> Unit,
    navController: NavHostController,
) {
    val context = LocalContext.current

    val viewModel: MainViewModel = viewModel(
        factory = MainViewModelFactory(context.applicationContext)
    )

    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val puzzleId = currentNavBackStackEntry?.arguments?.getLong("puzzleId")


    LaunchedEffect(puzzleId) {
        if (puzzleId != null) {
            viewModel.loadPuzzleById(puzzleId)
        }
    }


    val puzzle by viewModel.puzzle.collectAsState()
    val bitmaps = puzzle?.bitmaps

    val positionMap = puzzle?.positionMap

    if (bitmaps != null) {
        if (bitmaps.isNotEmpty()) {
            puzzle?.let { puzzleUi ->
                ImageView(
                    bitmapDataList = bitmaps, positionMap = positionMap, onWin = {
                        duration -> puzzle?.pid?.let { onGameWin(it, duration) }
                        puzzle!!.pid?.let { pid -> viewModel.updatePuzzle(duration, pid) }
                    },
                    onBack = {
                        navController.popBackStack()
                    },
                    difficulty = puzzleUi.difficulty
                )
            }
        }
    }

}
