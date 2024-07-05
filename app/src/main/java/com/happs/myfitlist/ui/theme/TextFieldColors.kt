package com.happs.myfitlist.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object TextFieldColors {
    @Composable
    fun colorsTextFields() = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = MaterialTheme.colorScheme.tertiary.copy(0.25f),
        focusedContainerColor = MaterialTheme.colorScheme.tertiary.copy(0.35f),
        errorContainerColor = MaterialTheme.colorScheme.tertiary.copy(0.2f),
        focusedTextColor = MyWhite,
        unfocusedTextColor = MyWhite,
        unfocusedBorderColor = Color.Transparent,
        focusedBorderColor = MyWhite,
        errorBorderColor = MyBlack,
        focusedPlaceholderColor = MyWhite.copy(0.85f),
        unfocusedPlaceholderColor = MyWhite.copy(0.85f),
        errorPlaceholderColor = MyWhite.copy(0.85f),
        cursorColor = MyWhite,
        errorSupportingTextColor = MyWhite,
    )

    @Composable
    fun colorsTextFieldsCard() = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = MaterialTheme.colorScheme.primary,
        focusedContainerColor = MaterialTheme.colorScheme.primary.copy(0.7f),
        errorContainerColor = MyRed.copy(0.9f),
        focusedTextColor = MyWhite,
        unfocusedTextColor = MyWhite,
        unfocusedBorderColor = Color.Transparent,
        focusedBorderColor = MaterialTheme.colorScheme.onTertiary,
        errorBorderColor = MyBlack,
        errorTextColor = MyWhite,
        focusedPlaceholderColor = MyWhite.copy(0.85f),
        unfocusedPlaceholderColor = MyWhite.copy(0.85f),
        errorPlaceholderColor = MyWhite.copy(0.85f),
        cursorColor = MyWhite,
        errorCursorColor = MyWhite,
        errorSupportingTextColor = MyBlack,
    )
}