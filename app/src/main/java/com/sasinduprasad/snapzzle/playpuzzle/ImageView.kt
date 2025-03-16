package com.sasinduprasad.snapzzle.playpuzzle

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import com.sasinduprasad.snapzzle.data.Difficulty
import com.sasinduprasad.snapzzle.R
import com.sasinduprasad.snapzzle.data.Bitmap.BitmapData
import com.sasinduprasad.snapzzle.data.Bitmap.BitmapItem
import com.sasinduprasad.snapzzle.data.puzzle.Position
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt


@SuppressLint("MutableCollectionMutableState")
@Composable
fun ImageView(
    bitmapDataList: List<BitmapData>,
    difficulty: Difficulty,
    positionMap: List<Position?>?,
    onWin: (duration: Int) -> Unit,
    onBack: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    var isPlaying by remember { mutableStateOf(false) }
    val showExitDialog = remember { mutableStateOf(false) }
    var elapsedTime by remember { mutableStateOf(0) }


    val bitmaps = bitmapDataList.mapNotNull { bitmapData ->
        BitmapFactory.decodeByteArray(bitmapData.bitmap, 0, bitmapData.bitmap.size)
    }


    val bitmapItems = remember(positionMap) {
        val items = mutableListOf<BitmapItem>()
        positionMap?.forEachIndexed { index, position ->
            if (position != null) {
                val bid = position.bitmapId ?: 1
                items.add(BitmapItem(bid = bid.toLong(), position = position.position))
            }
        }
        items
    }


    LaunchedEffect(Unit) {
        println("BitmapItems: $bitmapItems")
        println("PositionMap: $positionMap")
    }

    var columnSize by remember { mutableStateOf(IntSize.Zero) }

    if (showExitDialog.value) {
        AlertDialog(containerColor = MaterialTheme.colorScheme.primary,
            onDismissRequest = { showExitDialog.value = false },
            title = { Text("Exit Game?") },
            text = { Text("Are you sure you want to leave the game? Your progress will be lost.") },
            confirmButton = {
                Button(colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                    onClick = {
                        showExitDialog.value = false
                        onBack()
                    }
                ) {
                    Text("Yes, Exit", color = MaterialTheme.colorScheme.tertiary)
                }
            },
            dismissButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    onClick = { showExitDialog.value = false }
                ) {
                    Text("Cancel", color = MaterialTheme.colorScheme.tertiary)
                }
            }
        )
    }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (true) {
                kotlinx.coroutines.delay(1000L)
                elapsedTime++
            }
        }
    }

    BackHandler(enabled = true) {
        showExitDialog.value = true
    }

    Scaffold() { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            if (!isPlaying) {
                Dialog(onDismissRequest = {}) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(32.dp)
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.play_illustration),
                            contentDescription = "Home",
                            modifier = Modifier.size(200.dp)
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Winning Points",
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.tertiary,
                                fontSize = 18.sp
                            )
                            Text(
                                text = "+100 Points",
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.surface,
                                fontSize = 24.sp
                            )
                        }

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            onClick = { isPlaying = true }
                        ) {
                            Text(
                                text = "Start Solving",
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.tertiary,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp)
            ) {
                IconButton(onClick = { showExitDialog.value = true }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "go back"
                    )
                }

                Text(
                    text = formatTime(elapsedTime),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 20.sp
                )
            }

            val squareSize = 100.dp
            val gridSize = when (difficulty) {
                Difficulty.EASY -> 2
                Difficulty.MEDIUM -> 3
                Difficulty.HARD -> 4
            }
            val itemCount = when (difficulty) {
                Difficulty.EASY -> 8
                Difficulty.MEDIUM -> 15
                Difficulty.HARD -> 24
            }
            val heightDivider = when (difficulty) {
                Difficulty.EASY -> 5
                Difficulty.MEDIUM -> 6
                Difficulty.HARD -> 7
            }


            val items = remember(bitmapItems, difficulty) {
                mutableStateListOf<Any>().apply {

                    val shuffledItems = bitmapItems.take(itemCount).shuffled()
                    addAll(shuffledItems)


                    val emptySpaces = when (difficulty) {
                        Difficulty.EASY -> 2
                        Difficulty.MEDIUM -> 3
                        Difficulty.HARD -> 4
                    }
                    for (i in 1..emptySpaces) {
                        add(-i)
                    }
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(gridSize),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .onGloballyPositioned { coordinates ->
                        columnSize = coordinates.size
                    }
            ) {
                items(items.size) { index ->
                    val offsetX = remember { Animatable(0f) }
                    val offsetY = remember { Animatable(0f) }
                    val coroutineScope = rememberCoroutineScope()
                    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
                    val maxSwipeDistance = sizePx * 1.1

                    Box(
                        Modifier
                            .offset {
                                IntOffset(
                                    offsetX.value.roundToInt(),
                                    offsetY.value.roundToInt()
                                )
                            }
                            .width(squareSize)
                            .height((configuration.screenHeightDp.dp - 150.dp) / heightDivider)
                            .background(
                                if (items.getOrNull(index) is Int && (items[index] as Int) < 0) {
                                    MaterialTheme.colorScheme.secondary
                                } else {
                                    Color.Transparent
                                }
                            )
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragEnd = {
                                        val rightEmpty = index + 1 < items.size &&
                                                (items[index + 1] is Int && (items[index + 1] as Int) < 0) &&
                                                (index + 1) % gridSize != 0

                                        val leftEmpty = index - 1 >= 0 &&
                                                (items[index - 1] is Int && (items[index - 1] as Int) < 0) &&
                                                index % gridSize != 0

                                        val downEmpty = index + gridSize < items.size &&
                                                (items[index + gridSize] is Int && (items[index + gridSize] as Int) < 0)

                                        val upEmpty = index - gridSize >= 0 &&
                                                (items[index - gridSize] is Int && (items[index - gridSize] as Int) < 0)

                                        coroutineScope.launch {
                                            when {
                                                rightEmpty && offsetX.value > sizePx / 6 && offsetX.value < maxSwipeDistance -> {
                                                    swap(items, index, index + 1)
                                                    offsetX.animateTo(
                                                        sizePx,
                                                        tween(
                                                            100,
                                                            easing = FastOutSlowInEasing
                                                        )
                                                    )
                                                }

                                                leftEmpty && offsetX.value < -sizePx / 6 && offsetX.value > -maxSwipeDistance -> {
                                                    swap(items, index, index - 1)
                                                    offsetX.animateTo(
                                                        -sizePx,
                                                        tween(
                                                            100,
                                                            easing = FastOutSlowInEasing
                                                        )
                                                    )
                                                }

                                                downEmpty && offsetY.value > sizePx / 6 && offsetY.value < maxSwipeDistance -> {
                                                    swap(items, index, index + gridSize)
                                                    offsetY.animateTo(
                                                        sizePx,
                                                        tween(
                                                            100,
                                                            easing = FastOutSlowInEasing
                                                        )
                                                    )
                                                }

                                                upEmpty && offsetY.value < -sizePx / 6 && offsetY.value > -maxSwipeDistance -> {
                                                    swap(items, index, index - gridSize)
                                                    offsetY.animateTo(
                                                        -sizePx,
                                                        tween(
                                                            100,
                                                            easing = FastOutSlowInEasing
                                                        )
                                                    )
                                                }

                                                else -> {
                                                    offsetX.animateTo(0f, tween(100))
                                                    offsetY.animateTo(0f, tween(100))
                                                }
                                            }

                                            offsetX.animateTo(0f, tween(100))
                                            offsetY.animateTo(0f, tween(100))
                                            checkWinWithBitmapItems(items, positionMap, onWin = {
                                                onWin(elapsedTime)
                                            })
                                        }
                                    }
                                ) { _, dragAmount ->
                                    val isHorizontal = abs(dragAmount.x) > abs(dragAmount.y)

                                    val rightEmpty = index + 1 < items.size &&
                                            (items[index + 1] is Int && (items[index + 1] as Int) < 0) &&
                                            (index + 1) % gridSize != 0

                                    val leftEmpty = index - 1 >= 0 &&
                                            (items[index - 1] is Int && (items[index - 1] as Int) < 0) &&
                                            index % gridSize != 0

                                    val downEmpty = index + gridSize < items.size &&
                                            (items[index + gridSize] is Int && (items[index + gridSize] as Int) < 0)

                                    val upEmpty = index - gridSize >= 0 &&
                                            (items[index - gridSize] is Int && (items[index - gridSize] as Int) < 0)

                                    coroutineScope.launch {
                                        if (isHorizontal) {
                                            if ((dragAmount.x > 0 && rightEmpty) || (dragAmount.x < 0 && leftEmpty)) {
                                                offsetX.snapTo(
                                                    (offsetX.value + dragAmount.x).coerceIn(
                                                        (-maxSwipeDistance).toFloat(),
                                                        maxSwipeDistance.toFloat()
                                                    )
                                                )
                                            }
                                        } else {
                                            if ((dragAmount.y > 0 && downEmpty) || (dragAmount.y < 0 && upEmpty)) {
                                                offsetY.snapTo(
                                                    (offsetY.value + dragAmount.y).coerceIn(
                                                        (-maxSwipeDistance).toFloat(),
                                                        maxSwipeDistance.toFloat()
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                    ) {

                        if (items.getOrNull(index) is BitmapItem) {
                            val bitmapItem = items[index] as BitmapItem
                            val bitmapIndex = (bitmapItem.bid.toInt() - 1).coerceIn(0, bitmaps.size - 1)

                            if (bitmapIndex >= 0 && bitmapIndex < bitmaps.size) {
                                Image(
                                    contentScale = ContentScale.FillBounds,
                                    bitmap = bitmaps[bitmapIndex].asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier.zIndex(50f).fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


fun checkWinWithBitmapItems(items: List<Any>, positionMap: List<Position?>?, onWin: () -> Unit) {
    if (positionMap.isNullOrEmpty()) {
        println("Position map is null or empty")
        return
    }

    val isWin = items.withIndex().all { (index, value) ->

        (value is Int && (value as Int) < 0) ||
                (value is BitmapItem &&
                        index < positionMap.size &&
                        positionMap[index] != null &&
                        positionMap[index]?.bitmapId.toString() == value.bid.toString() &&
                        positionMap[index]?.position == value.position)
    }

    if (isWin) {
        println("You won!")
        onWin()
    } else {
        println("Not yet - Current state doesn't match position map")
    }
}


fun swap(list: MutableList<Any>, first: Int, second: Int) {
    if (first in list.indices && second in list.indices) {
        list[first] = list[second].also { list[second] = list[first] }
    }
}

@SuppressLint("DefaultLocale")
fun formatTime(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    return String.format("%02dh:%02dm:%02ds", hours, minutes, secs)
}

