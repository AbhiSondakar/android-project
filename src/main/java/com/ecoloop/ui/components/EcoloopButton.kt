package com.ecoloop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EcoloopButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    fullWidth: Boolean = false,
    variant: ButtonVariant = ButtonVariant.Primary,
    contentPadding: PaddingValues = PaddingValues(vertical = 14.dp, horizontal = 20.dp),
    shape: Shape = RoundedCornerShape(12.dp),
    content: @Composable () -> Unit
) {
    val colors = when (variant) {
        ButtonVariant.Primary -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
        )
        ButtonVariant.Secondary -> ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
        )
        ButtonVariant.Danger -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.statusDanger,
            contentColor = Color.White,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
        )
        ButtonVariant.Ghost -> ButtonDefaults.textButtonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
        )
    }

    val borderColor = when (variant) {
        ButtonVariant.Secondary, ButtonVariant.Danger -> Color.Transparent
        else -> Color.Transparent
    }

    val borderWidth = when (variant) {
        ButtonVariant.Secondary, ButtonVariant.Danger -> 0.dp
        else -> 0.dp
    }

    if (variant == ButtonVariant.Secondary) {
        androidx.compose.material3.OutlinedButton(
            onClick = onClick,
            modifier = if (fullWidth) modifier.fillMaxWidth() else modifier,
            enabled = enabled,
            colors = colors,
            shape = shape,
            border = null,
            contentPadding = contentPadding
        ) {
            content()
        }
    } else if (variant == ButtonVariant.Ghost) {
        TextButton(
            onClick = onClick,
            modifier = if (fullWidth) modifier.fillMaxWidth() else modifier,
            enabled = enabled,
            colors = colors,
            shape = shape,
            contentPadding = contentPadding
        ) {
            content()
        }
    } else {
        val modifierWithWidth = if (fullWidth) modifier.fillMaxWidth() else modifier
        Button(
            onClick = onClick,
            modifier = modifierWithWidth,
            enabled = enabled,
            colors = colors,
            shape = shape,
            contentPadding = contentPadding
        ) {
            content()
        }
    }
}

enum class ButtonVariant { Primary, Secondary, Danger, Ghost }

@Composable
fun EcoloopButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    fullWidth: Boolean = false,
    variant: ButtonVariant = ButtonVariant.Primary,
    isLoading: Boolean = false
) {
    EcoloopButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled && !isLoading,
        fullWidth = fullWidth,
        variant = variant,
        contentPadding = PaddingValues(vertical = 14.dp, horizontal = 20.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = if (variant == ButtonVariant.Primary || variant == ButtonVariant.Danger)
                    Color.White else MaterialTheme.colorScheme.onSurface
                ,
                strokeWidth = 2.dp,
                strokeCap = StrokeCap.Round,
                modifier = Modifier.size(16.dp)
            )
        } else {
            Text(
                text = text,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
