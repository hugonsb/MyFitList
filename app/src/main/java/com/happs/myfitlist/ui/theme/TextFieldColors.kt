package com.happs.myfitlist.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable

object TextFieldColors {
    @Composable
    fun colorsTextFields() = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = MaterialTheme.colorScheme.tertiary.copy(0.5f),
        focusedContainerColor = MaterialTheme.colorScheme.tertiary.copy(0.7f),
        errorContainerColor = MaterialTheme.colorScheme.tertiary.copy(0.2f),
        focusedTextColor = MyWhite,
        unfocusedTextColor = MyWhite,
        unfocusedBorderColor = MyWhite.copy(0.5f),
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
        unfocusedContainerColor = MyRed.copy(0.9f),
        focusedContainerColor = MyRed.copy(0.6f),
        errorContainerColor = MyRed.copy(0.9f),
        focusedTextColor = MyWhite,
        unfocusedTextColor = MyWhite,
        unfocusedBorderColor = MyRed.copy(0.5f),
        focusedBorderColor = MyRed,
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