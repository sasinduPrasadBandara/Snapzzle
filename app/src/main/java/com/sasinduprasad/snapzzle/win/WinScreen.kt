package com.sasinduprasad.snapzzle.win

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sasinduprasad.snapzzle.R
import com.sasinduprasad.snapzzle.playpuzzle.formatTime

@Composable
fun WinScreen(
    navController: NavHostController,
    onNavigateToHome: () -> Unit,
    onReplay: (puzzleId:Long) -> Unit,
    onSaveToGallery: () -> Unit,
) {

    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val puzzleId = currentNavBackStackEntry?.arguments?.getLong("puzzleId")
    val duration = currentNavBackStackEntry?.arguments?.getInt("duration")


    Scaffold { padding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(padding)
                .padding(16.dp)
        ) {

            Image(
                modifier = Modifier.size(200.dp),
                painter = painterResource(
                    id = R.drawable.win_illustration
                ),
                contentDescription = "win illustration"
            )

            Text(
                text = "Congratulation",
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 42.sp
            )
            Text(
                text = "You solve the puzzle",
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 18.sp
            )

            if(duration != null){
                Text(
                    text = formatTime(duration),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 24.sp
                )
            }

            Text(
                text = "+100 Coins",
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.surface,
                fontSize = 32.sp
            )
        }
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp),
        )

        {
            IconButton(
                onClick = {
                    onNavigateToHome()
                }) {
                Image(
                    painter = painterResource(id = R.drawable.home_icon),
                    contentDescription = "Home",
                    modifier = Modifier.size(30.dp)
                )
            }

            IconButton(
                onClick = {
                    if (puzzleId != null) {
                        onReplay(puzzleId)
                    }
                }) {
                Image(
                    painter = painterResource(id = R.drawable.replay_icon),
                    contentDescription = "Retry",
                    modifier = Modifier.size(30.dp)
                )
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
                    onSaveToGallery()
                },
            ) {
                Text(
                    text = "Save to gallery",
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 18.sp
                )
            }
        }
    }
}