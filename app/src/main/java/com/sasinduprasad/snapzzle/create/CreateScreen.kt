package com.sasinduprasad.snapzzle.create

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sasinduprasad.snapzzle.CameraPreview
import com.sasinduprasad.snapzzle.ImageView
import com.sasinduprasad.snapzzle.MainViewModel
import com.sasinduprasad.snapzzle.R
import com.sasinduprasad.snapzzle.component.Header
import com.sasinduprasad.snapzzle.swap
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun CreateScreen(
    onBackPressed: () -> Unit,
) {

    val context = LocalContext.current
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE
            )
        }
    }

    val viewModel = viewModel<MainViewModel>()
    val bitmaps by viewModel.bitmaps.collectAsState()
    val originalBitmap by viewModel.originalbitmap.collectAsState()
    val scope = rememberCoroutineScope()

    Scaffold { padding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(padding)
                .padding(16.dp)
        ) {
            Header(isRevers = true,
                title = "Create Puzzle",
                icon = R.drawable.back_icon,
                onClick = { onBackPressed() })

            Spacer(modifier = Modifier.height(8.dp))



            if (originalBitmap != null) {
//                ImageView(bitmaps)

                if (bitmaps.isNotEmpty()) {
                    Text(
                        text = "Click save puzzle button to save puzzle in my puzzles",
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 16.sp    
                    )
                } else {

                    Text(
                        text = "Click generate puzzle button to turn image into puzzle",
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 16.sp
                    )
                }

                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box {
                        if (bitmaps.isNotEmpty()) {

                            val squareSize = 100.dp
                            val gridSize = 3
                            val itemCount = 15

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(gridSize),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                items(bitmaps.size) { index ->
                                    Box(
                                        Modifier
                                            .width(squareSize)
                                            .height(squareSize)
                                    ) {
                                        Image(
                                            bitmap = bitmaps[index].asImageBitmap(),
                                            contentDescription = null,
                                        )
                                    }
                                }
                            }

                        } else {
                            Image(
                                bitmap = originalBitmap!!.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                            )
                        }
                    }


                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(48.dp),
                    )

                    {
                        IconButton(
                            onClick = {
                                viewModel.cleanUp()
                            }) {
                            Image(
                                painter = painterResource(id = R.drawable.backtocam_icon),
                                contentDescription = "Back to camera",
                                modifier = Modifier.size(30.dp)
                            )
                        }

                        if (bitmaps.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    viewModel.shuffle()
                                }) {
                                Image(
                                    painter = painterResource(id = R.drawable.shuffle_icon),
                                    contentDescription = "Back to camera",
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.surface,
                                disabledContainerColor = MaterialTheme.colorScheme.surface,
                                disabledContentColor = MaterialTheme.colorScheme.surface,
                            ),
                            onClick = {
                                if (bitmaps.isNotEmpty()) {
                                    viewModel.savePuzzle()
                                } else {
                                    viewModel.generatePuzzle(originalBitmap!!)
                                }
                            },
                        ) {
                            Text(
                                text = if (bitmaps.isNotEmpty()) "Save Puzzle" else "Generate Puzzle",
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.tertiary,
                                fontSize = 18.sp
                            )
                        }
                    }
                }

            } else {

                Text(
                    text = "Take picture using camera to create a puzzle",
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 16.sp
                )

                Box(modifier = Modifier.fillMaxSize()) {
                    CameraPreview(
                        controller = controller, modifier = Modifier.fillMaxSize()
                    )

                    IconButton(
                        onClick = {
                            controller.cameraSelector =
                                if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                                    CameraSelector.DEFAULT_FRONT_CAMERA
                                } else CameraSelector.DEFAULT_BACK_CAMERA
                        }, modifier = Modifier.offset(0.dp, 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cameraswitch,
                            contentDescription = "Switch camera",
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(vertical = 48.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {

                        IconButton(modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(100))
                            .background(MaterialTheme.colorScheme.secondary),
                            onClick = {
                                takePhoto(
                                    controller = controller,
                                    onPhotoTaken = viewModel::onTakePhoto,
                                    context = context
                                )
                            }) {
                            Image(
                                painter = painterResource(id = R.drawable.cam_icon),
                                contentDescription = "Take photo",
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }

                }
            }
        }
    }


}

private fun takePhoto(
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit,
    context: Context,
) {
    controller.takePicture(ContextCompat.getMainExecutor(context),
        object : OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)

                val matrix = Matrix().apply {
                    postRotate(image.imageInfo.rotationDegrees.toFloat())
                }
                val rotatedBitmap = Bitmap.createBitmap(
                    image.toBitmap(), 0, 0, image.width, image.height, matrix, true
                )

                onPhotoTaken(rotatedBitmap)
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("Camera", "Couldn't take photo: ", exception)
            }
        })
}