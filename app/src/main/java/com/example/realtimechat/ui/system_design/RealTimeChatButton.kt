package com.example.realtimechat.ui.system_design

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RealTimeChatButton(
    modifier: Modifier = Modifier,
    text: String,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
        contentPadding = PaddingValues(12.dp),
        enabled = !isLoading && enabled

    ) {
        AnimatedContent(
            targetState = isLoading,
            label = "Loading Animation"
        ) {
            isLoading ->
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .size(24.dp),
                    strokeWidth = 2.dp,
                )
            }else{
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    }

}