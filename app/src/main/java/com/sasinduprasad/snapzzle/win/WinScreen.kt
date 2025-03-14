package com.sasinduprasad.snapzzle.win

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.sasinduprasad.snapzzle.PlayPuzzleScreen
import com.sasinduprasad.snapzzle.WinScreen

@Composable
fun WinScreen(
    navController:NavHostController
) {

    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val args = currentNavBackStackEntry?.toRoute<WinScreen>()
}