package com.sasinduprasad.snapzzle

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
            HomeScreen(
                onNavigateToCreate = {
                    navController.navigate(CreateScreen)
                },
                onNavigateToPlay = {
                    navController.navigate(PlayPuzzleScreen(puzzleId = it))
                })
        }

        composable<CreateScreen> {
            CreateScreen(onBackPressed = {
                navController.popBackStack()
            })
        }

        composable<PlayPuzzleScreen> {

            PlayPuzzleScreen(
                onGameWin = {id , duration->
                    navController.navigate(
                        WinScreen(
                            puzzleId = id,
                            duration = duration
                        )
                    )

                },
                onGameGiveUp = {
                    navController.popBackStack()
                },
                navController
            )
        }

        composable<WinScreen> {
            WinScreen(
                navController,
                onNavigateToHome = {
                    navController.navigate(HomeScreen)
                },
                onReplay = {
                    WinScreen(
                        puzzleId = it,
                        duration = 0
                    )
                },
                onSaveToGallery ={}
            )
        }


    }

}


@Serializable
object HomeScreen

@Serializable
object CreateScreen

@Serializable
data class PlayPuzzleScreen(
    val puzzleId: Long,
)

@Serializable
data class WinScreen(
    val puzzleId: Long,
    val duration: Int,
)

