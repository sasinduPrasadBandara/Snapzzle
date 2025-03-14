package com.sasinduprasad.snapzzle.playpuzzle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.sasinduprasad.snapzzle.PlayPuzzleScreen

@Composable
fun PlayPuzzleScreen(
    onGameWin: () -> Unit,
    onGameGiveUp:()->Unit,
    navController:NavHostController
) {

    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val args = currentNavBackStackEntry?.toRoute<PlayPuzzleScreen>()

}