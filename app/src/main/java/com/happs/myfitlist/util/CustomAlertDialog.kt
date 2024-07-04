package com.happs.myfitlist.util

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.happs.myfitlist.R
import com.happs.myfitlist.ui.theme.MyBlack
import com.happs.myfitlist.ui.theme.myFontTitle

@Composable
fun CustomAlertDialog(title: String, text: String, textButtomConfirm: String, onclose: () -> Unit, onConfirm: () -> Unit) {
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
                title,
                fontSize = 35.sp,
                color = MyBlack,
                fontFamily = myFontTitle,
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text,
                    textAlign = TextAlign.Center,
                    fontSize = 25.sp,
                    color = MyBlack,
                    fontFamily = myFontTitle,
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row {
                    TextButton(
                        onClick = { onclose() },
                        modifier = Modifier.weight(1f) // Equal width for buttons
                    ) {
                        Text(
                            stringResource(R.string.cancelar),
                            color = MyBlack,
                            fontFamily = myFontTitle, fontSize = 30.sp
                        )
                    }
                    TextButton(
                        onClick = { onConfirm() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            textButtomConfirm,
                            color = Color.Red,
                            fontFamily = myFontTitle,
                            fontSize = 30.sp
                        )
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {},

        )
}