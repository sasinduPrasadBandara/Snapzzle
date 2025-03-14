package com.sasinduprasad.snapzzle

import android.content.ClipData
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.SwipeableState
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import kotlinx.coroutines.launch
import java.util.Collections.addAll
import kotlin.math.abs
import kotlin.math.roundToInt


@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun ImageView(bitmaps: List<Bitmap>) {
    val viewModel: MainViewModel = viewModel()

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        IconButton(onClick = { viewModel.cleanUp() }) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Switch camera"
            )
        }

        val squareSize = 100.dp
        val gridSize = 3
        val itemCount = 15

        // List with one empty slot (-1 represents empty)
        val items = remember { mutableStateListOf<Int>().apply {
            addAll((0 until itemCount - 1).toList())
            add(-1)
        }}

        LazyVerticalGrid(
            columns = GridCells.Fixed(gridSize),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items.size) { index ->
                val offsetX = remember { androidx.compose.animation.core.Animatable(0f) }
                val offsetY = remember { androidx.compose.animation.core.Animatable(0f) }
                val coroutineScope = rememberCoroutineScope()
                val sizePx = with(LocalDensity.current) { squareSize.toPx() }
                val maxSwipeDistance = sizePx *1.1

                Box(
                    Modifier
                        .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
                        .width(squareSize)
                        .height(squareSize)
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragEnd = {
                                    val emptyIndex = items.indexOf(-1)

                                    // Prevent crashes: Ensure valid index swapping
                                    if (index in items.indices && emptyIndex in items.indices) {
                                        coroutineScope.launch {
                                            when {
                                                // Swipe Right
                                                offsetX.value > sizePx / 6 && offsetX.value < maxSwipeDistance &&
                                                        index + 1 == emptyIndex && (index + 1) % gridSize != 0 -> {
                                                    swap(items, index, emptyIndex)
                                                    offsetX.animateTo(sizePx, tween(100, easing = FastOutSlowInEasing))
                                                }
                                                // Swipe Left
                                                offsetX.value < -sizePx / 6 && offsetX.value > -maxSwipeDistance &&
                                                        index - 1 == emptyIndex && index % gridSize != 0 -> {
                                                    swap(items, index, emptyIndex)
                                                    offsetX.animateTo(-sizePx, tween(100, easing = FastOutSlowInEasing))
                                                }
                                                // Swipe Down
                                                offsetY.value > sizePx / 6 && offsetY.value < maxSwipeDistance &&
                                                        index + gridSize == emptyIndex -> {
                                                    swap(items, index, emptyIndex)
                                                    offsetY.animateTo(sizePx, tween(100, easing = FastOutSlowInEasing))
                                                }
                                                // Swipe Up
                                                offsetY.value < -sizePx / 6 && offsetY.value > -maxSwipeDistance &&
                                                        index - gridSize == emptyIndex -> {
                                                    swap(items, index, emptyIndex)
                                                    offsetY.animateTo(-sizePx, tween(100, easing = FastOutSlowInEasing))
                                                }
                                            }
                                            // Reset animation
                                            offsetX.animateTo(0f, tween(100))
                                            offsetY.animateTo(0f, tween(100))
                                        }
                                    } else {
                                        // If out-of-bounds, reset position smoothly
                                        coroutineScope.launch {
                                            offsetX.animateTo(0f, tween(100))
                                            offsetY.animateTo(0f, tween(100))
                                        }
                                    }
                                }
                            ) { _, dragAmount ->
                                val emptyIndex = items.indexOf(-1)
                                val isHorizontal = abs(dragAmount.x) > abs(dragAmount.y)

                                coroutineScope.launch {
                                    if (isHorizontal) {
                                        // Only move left/right if empty box is in the correct spot
                                        if (index + 1 == emptyIndex || index - 1 == emptyIndex) {
                                            offsetX.snapTo((offsetX.value + dragAmount.x).coerceIn(
                                                (-maxSwipeDistance).toFloat(),
                                                maxSwipeDistance.toFloat()
                                            ))
                                        }
                                    } else {
                                        // Only move up/down if empty box is in the correct spot
                                        if (index + gridSize == emptyIndex || index - gridSize == emptyIndex) {
                                            offsetY.snapTo((offsetY.value + dragAmount.y).coerceIn((-maxSwipeDistance).toFloat(),
                                                maxSwipeDistance.toFloat()
                                            ))
                                        }
                                    }
                                }
                            }
                        }
                ) {
                    if (items[index] != -1 && items[index] < bitmaps.size) {
                        Image(
                            bitmap = bitmaps[items[index]].asImageBitmap(),
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}

// Safe Swap Function (Prevents Crashes)
fun swap(list: MutableList<Int>, first: Int, second: Int) {
    if (first in list.indices && second in list.indices) {
        list[first] = list[second].also { list[second] = list[first] }
    }
}









@OptIn(ExperimentalMaterial3Api::class, ExperimentalWearMaterialApi::class)
@Composable
private fun SwipeableSample() {
    val width = 200.dp
    val squareSize = 100.dp

    LazyVerticalStaggeredGrid(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalItemSpacing = 4.dp,
        modifier = Modifier.fillMaxSize(),

        columns = StaggeredGridCells.Fixed(3),
    ) {
        items(15) {
            repeat(15) {
                val swipeableState = rememberSwipeableState(0)
                val sizePx = with(LocalDensity.current) { squareSize.toPx() }
                val anchors = mapOf(0f to 0, sizePx to 1) // Maps anchor points (in px) to states
                Box(
                    Modifier
                        .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                        .width(squareSize)
                        .height(squareSize)
                        .background(Color.DarkGray)
                        .swipeable(
                            state = swipeableState,
                            anchors = anchors,
                            thresholds = { _, _ -> FractionalThreshold(0.3f) },
                            orientation = Orientation.Horizontal
                        )
                        .background(Color.LightGray),
                ) {
                    Box(modifier = Modifier
                        .offset {
                            IntOffset(
                                swipeableState.offset.value.roundToInt(),
                                0
                            )
                        }
                        .background(Color.DarkGray)
                        .fillMaxSize()
                    )
                }
            }
        }
    }
}
