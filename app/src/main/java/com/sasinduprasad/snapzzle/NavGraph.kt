package com.sasinduprasad.snapzzle

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.sasinduprasad.snapzzle.create.CreateScreen
import com.sasinduprasad.snapzzle.home.HomeScreen
import com.sasinduprasad.snapzzle.playpuzzle.PlayPuzzleScreen
import com.sasinduprasad.snapzzle.win.WinScreen
import kotlinx.serialization.Serializable

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {

    NavHost(
        navController = navController,
        startDestination = HomeScreen
    ) {
        composable<HomeScreen> {
            HomeScreen(onNavigateToCreate = {
                navController.navigate(CreateScreen)
            })
        }

        composable<CreateScreen> {
            CreateScreen(onBackPressed = {
                navController.popBackStack()
            })
        }

        composable<PlayPuzzleScreen> {

            PlayPuzzleScreen(
                onGameWin = {
                    navController.navigate(WinScreen(
                        puzzleId = "",
                        duration = 0
                    ))
                },
                onGameGiveUp = {
                    navController.popBackStack()
                },
                navController
            )
        }

        composable<WinScreen> {
            WinScreen(navController)
        }


    }

}


@Serializable
object HomeScreen

@Serializable
object CreateScreen

@Serializable
data class PlayPuzzleScreen(
    val puzzleId: String,
)

@Serializable
data class WinScreen(
    val puzzleId: String,
    val duration:Long,
)

