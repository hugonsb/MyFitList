package com.happs.myfitlist.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.happs.myfitlist.ui.theme.MyBlack
import com.happs.myfitlist.ui.theme.myFontTitle

@Composable
fun CustomAlertDialog(onclose: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = {
            onclose()
        },
        icon = {
            Icon(
                Icons.Filled.Delete,
                contentDescription = null,
                tint = MyBlack
            )
        },
        title = {
            Text(
                "Tem certeza que deseja excluir?",
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                color = MyBlack,
                fontFamily = myFontTitle,
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                }
            ) {
                Text(
                    "Confirmar",
                    color = Color.Red,
                    fontFamily = myFontTitle,
                    fontSize = 20.sp
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onclose()
                }
            ) {
                Text(
                    "Cancelar",
                    color = MyBlack,
                    fontFamily = myFontTitle,
                    fontSize = 20.sp
                )
            }
        }
    )
}