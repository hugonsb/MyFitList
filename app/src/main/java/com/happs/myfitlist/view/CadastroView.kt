package com.happs.myfitlist.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.happs.myfitlist.R
import com.happs.myfitlist.model.usuario.Usuario
import com.happs.myfitlist.ui.theme.MyBlack
import com.happs.myfitlist.ui.theme.MyWhite
import com.happs.myfitlist.ui.theme.myFontBody
import com.happs.myfitlist.ui.theme.myFontTitle
import com.happs.myfitlist.viewmodel.AppViewModelProvider
import com.happs.myfitlist.viewmodel.CadastroViewModel
import kotlinx.coroutines.launch

@Composable
fun CadastroView(
    navController: NavController,
    cadastroViewModel: CadastroViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiCadastroState by cadastroViewModel.cadastroState.collectAsState()

    if (uiCadastroState.usuario.id != -1) {
        navController.navigate("home") { launchSingleTop = true }
    } else if (uiCadastroState.isUserLoaded) {
        ContentCadastro(cadastroViewModel)
    }
}

@Composable
fun ContentCadastro(cadastroViewModel: CadastroViewModel) {

    var nome by rememberSaveable { mutableStateOf("") }
    var idade by rememberSaveable { mutableStateOf("") }
    var peso by rememberSaveable { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    var isNomeEror by rememberSaveable { mutableStateOf(false) }

    var enabledButton by remember { mutableStateOf(true) }

    val colorsTextFields = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = MyWhite.copy(0.45f),
        focusedContainerColor = MaterialTheme.colorScheme.tertiary.copy(0.2f),
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(start = 10.dp, end = 10.dp),
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "MY",
                fontFamily = myFontTitle,
                fontSize = 55.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onTertiary,
            )
            Text(
                text = "FIT",
                fontFamily = myFontTitle,
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                color = MyWhite,
            )
            Text(
                text = "LIST",
                fontFamily = myFontTitle,
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                color = MyWhite,
            )
        }

        Icon(
            tint = MyWhite,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .align(Alignment.CenterHorizontally),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Comece por aqui 👇",
            fontFamily = myFontTitle,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = MyWhite,
            modifier = Modifier
                .align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = nome,
            onValueChange = { it ->
                if (it.length <= 100 && it.all { it.isLetter() || it.isWhitespace() }) {
                    nome = it
                    isNomeEror = false
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences
            ),
            isError = isNomeEror,
            supportingText = { if (isNomeEror) Text("Digite seu nome") },
            placeholder = {
                Text(
                    text = "Nome (Obrigatório)",
                    fontFamily = myFontBody,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            colors = colorsTextFields,
            textStyle = TextStyle(
                fontFamily = myFontBody,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            ),
            shape = CutCornerShape(topStart = 14.dp, bottomEnd = 14.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        val pattern = remember { Regex("^\\d*\$") }

        OutlinedTextField(
            value = idade,
            onValueChange = {
                if (it.matches(pattern)) {
                    idade = if (it.isNotEmpty()) {
                        it.toInt().coerceAtMost(122).toString()
                    } else {
                        it
                    }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            placeholder = {
                Text(
                    text = "Idade (Opcional)",
                    fontFamily = myFontBody,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = colorsTextFields,
            textStyle = TextStyle(
                fontFamily = myFontBody,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            ),
            shape = CutCornerShape(topStart = 14.dp, bottomEnd = 14.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = peso,
            onValueChange = {
                // Verifica se o input tem no máximo 6 caracteres e no máximo um ponto decimal
                if (it.length <= 6 && it.count { char -> char == '.' } <= 1) {
                    peso = when {
                        // Se o valor for maior que 635, seta para 635
                        it.isNotEmpty() && (it.toFloatOrNull() ?: 0f) > 635f -> {
                            "635"
                        }
                        // Se o valor é "0" ou começa com "0." permite a entrada
                        it == "0" || it.startsWith("0.") -> {
                            it
                        }
                        // Se o valor é um número decimal válido ou vazio, permite a entrada
                        it.toFloatOrNull() != null || it.isEmpty() -> {
                            it
                        }
                        // Caso contrário, mantém o valor atual de `peso`
                        else -> {
                            peso
                        }
                    }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
            placeholder = {
                Text(
                    text = "Peso (Kg) (Opcional)",
                    fontFamily = myFontBody,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            colors = colorsTextFields,
            textStyle = TextStyle(
                fontFamily = myFontBody,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            ),
            shape = CutCornerShape(topStart = 14.dp, bottomEnd = 14.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        ElevatedButton(
            onClick = {
                if (nome.isNotBlank()) {
                    enabledButton = false
                    val usuario =
                        Usuario(
                            nome = nome,
                            idade = idade.toByteOrNull() ?: -1,
                            peso = peso.toFloatOrNull() ?: -1f
                        )
                    coroutineScope.launch {
                        cadastroViewModel.addUser(usuario)
                    }
                } else {
                    isNomeEror = true
                }
            },
            modifier = Modifier
                .width(260.dp)
                .height(60.dp)
                .align(Alignment.CenterHorizontally),
            enabled = enabledButton,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary.copy(
                    0.3f
                )
            ),
            shape = CutCornerShape(topStart = 14.dp, bottomEnd = 14.dp)
        ) {
            Text(
                text = "SALVAR",
                fontFamily = myFontTitle,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}