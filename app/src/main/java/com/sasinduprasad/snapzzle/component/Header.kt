package com.sasinduprasad.snapzzle.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp

@Composable
fun Header(
    title: String,
    icon: Int,
    onClick: () -> Unit,
    isRevers: Boolean = false,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isRevers) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.clickable(onClick = onClick)
            )
            Text(
                text = title,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.tertiary,
                fontFamily = MaterialTheme.typography.bodySmall.fontFamily
            )
        } else {
            Text(
                text = title,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.tertiary,
                fontFamily = MaterialTheme.typography.bodySmall.fontFamily
            )
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.clickable(onClick = onClick)
            )
        }
    }
}
