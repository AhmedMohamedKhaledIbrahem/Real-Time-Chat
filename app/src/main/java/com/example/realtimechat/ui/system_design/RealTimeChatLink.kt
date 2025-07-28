package com.example.realtimechat.ui.system_design

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun RealTimeChatLink(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    Text(
        modifier = modifier.clickable(onClick = onClick),
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center
    )

}