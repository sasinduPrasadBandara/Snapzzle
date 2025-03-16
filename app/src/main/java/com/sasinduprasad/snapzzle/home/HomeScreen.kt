package com.sasinduprasad.snapzzle.home

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sasinduprasad.snapzzle.data.Difficulty
import com.sasinduprasad.snapzzle.MainViewModel
import com.sasinduprasad.snapzzle.MainViewModelFactory
import com.sasinduprasad.snapzzle.R
import com.sasinduprasad.snapzzle.playpuzzle.formatTime
import kotlinx.coroutines.Dispatchers

@Composable
fun HomeScreen(
    onNavigateToCreate: () -> Unit,
    onNavigateToPlay: (puzzleId: Long) -> Unit,
) {

    val isDarkMode = isSystemInDarkTheme()
    val scrollState = rememberScrollState()

    val context = LocalContext.current

    val viewModel: MainViewModel = viewModel(
        factory = MainViewModelFactory(context.applicationContext)
    )

    val puzzleList by viewModel.puzzleList.collectAsState()

    LaunchedEffect(Dispatchers.IO) {
        viewModel.loadPuzzlesFromDb()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigateToCreate() },
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.tertiary
            ) {
                Icon(Icons.Filled.Add, "floating action button.")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(state = scrollState)
        ) {

            Image(
                painter = painterResource(
                    id = if (isDarkMode) R.drawable.snapzzle_logo else {
                        R.drawable.snapzzle_logo_light
                    }
                ),
                contentDescription = "snapzzle_logo"
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "My Puzzles",
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 16.sp
                )

                LazyRow {
                    items(puzzleList.size) { index ->
                        val puzzle = puzzleList[index]

                        ElevatedCard(
                            onClick = { puzzle.pid?.let { onNavigateToPlay(it) } },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 6.dp
                            ),
                            modifier = Modifier
                                .size(width = 200.dp, height = 280.dp)
                        ) {

                            val bitmaps = puzzle.bitmaps.mapNotNull { bitmapData ->
                                BitmapFactory.decodeByteArray(
                                    bitmapData.bitmap,
                                    0,
                                    bitmapData.bitmap.size
                                )
                            }.shuffled()



                            val columnCount = when (puzzle.difficulty) {
                                Difficulty.EASY -> 2
                                Difficulty.MEDIUM -> 3
                                Difficulty.HARD -> 4
                            }

                            val rowCount = when (puzzle.difficulty) {
                                Difficulty.EASY -> 4
                                Difficulty.MEDIUM -> 5
                                Difficulty.HARD -> 6
                            }

                            val gridSize = when (puzzle.difficulty) {
                                Difficulty.EASY -> 2
                                Difficulty.MEDIUM -> 3
                                Difficulty.HARD -> 4
                            }

                            if (puzzle.isWin) {
                                Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Image(
                                        bitmap = puzzle.originalBitmap.asImageBitmap(),
                                        contentDescription = null,
                                    )
                                    Text(
                                        text = formatTime(puzzle.duration),
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.tertiary,
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(32.dp))
                                }
                            } else {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(gridSize),
                                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                                    verticalArrangement = Arrangement.spacedBy(2.dp),
                                ) {
                                    items(bitmaps.size) { bitmapIndex ->
                                        Box(
                                            Modifier
                                                .width(200.dp / columnCount)
                                                .height(280.dp / rowCount)
                                        ) {
                                            Image(
                                                bitmap = bitmaps[bitmapIndex].asImageBitmap(),
                                                contentDescription = null,
                                            )

                                        }
                                    }
                                }
                            }

                        }
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }
            }

        }

        Spacer(modifier = Modifier.height(24.dp))


    }

}


