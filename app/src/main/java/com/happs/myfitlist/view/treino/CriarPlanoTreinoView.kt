package com.happs.myfitlist.view.treino

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.happs.myfitlist.R
import com.happs.myfitlist.model.treino.DiaTreino
import com.happs.myfitlist.model.treino.Exercicio
import com.happs.myfitlist.model.treino.PlanoTreino
import com.happs.myfitlist.ui.theme.MyBlack
import com.happs.myfitlist.ui.theme.MyRed
import com.happs.myfitlist.ui.theme.MyWhite
import com.happs.myfitlist.ui.theme.MyYellow
import com.happs.myfitlist.ui.theme.myFontBody
import com.happs.myfitlist.ui.theme.myFontTitle

@Composable
fun CriarPlanoTreinoView(navController: NavHostController) {

    val colorsTextFields = OutlinedTextFieldDefaults.colors(
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

    val colorsTextFieldsCard = OutlinedTextFieldDefaults.colors(
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

    var nomePlanoTreino by rememberSaveable { mutableStateOf("") }
    var isNomePlanoTreinoEror by rememberSaveable { mutableStateOf(false) }

    var expandedCamposExerciciosSegundaFeira by remember { mutableStateOf(false) }

    var grupoMuscular by rememberSaveable { mutableStateOf("") }
    var isGrupoMuscularError by rememberSaveable { mutableStateOf(false) }

    var nomeExercicio by rememberSaveable { mutableStateOf("") }
    var isNomeExercicioError by rememberSaveable { mutableStateOf(false) }

    var numeroSeries by rememberSaveable { mutableStateOf("") }
    var isNumeroSeriesError by rememberSaveable { mutableStateOf(false) }

    var numeroRepeticoes by rememberSaveable { mutableStateOf("") }
    var isNumeroRepeticoesError by rememberSaveable { mutableStateOf(false) }

    var exerciciosSegundaFeira = remember { mutableStateListOf<Exercicio>() }

    var enabledButton by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(start = 5.dp, bottom = 5.dp),
            text = "Criar plano de treino",
            fontFamily = myFontTitle,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = MyWhite,
        )

        OutlinedTextField(
            value = nomePlanoTreino,
            onValueChange = { it ->
                if (it.length <= 100 && it.all { it.isLetter() || it.isWhitespace() }) {
                    nomePlanoTreino = it
                    isNomePlanoTreinoEror = false
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences
            ),
            isError = isNomePlanoTreinoEror,
            supportingText = { if (isNomePlanoTreinoEror) Text("Digite um nome para o plano") },
            placeholder = {
                Text(
                    text = "Nome do plano",
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

        Spacer(modifier = Modifier.height(5.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = CutCornerShape(topStart = 14.dp, bottomEnd = 14.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MyWhite)
                    .padding(10.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 5.dp, bottom = 5.dp)
                        .align(Alignment.Start),
                    text = "Segunda-feira",
                    fontFamily = myFontTitle,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = MyBlack,
                )

                Column {
                    OutlinedTextField(
                        value = grupoMuscular,
                        onValueChange = { it ->
                            if (it.length <= 100) {
                                grupoMuscular = it
                                isGrupoMuscularError = false
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                        isError = isGrupoMuscularError,
                        supportingText = { if (isGrupoMuscularError) Text("Informe o Grupo muscular") },
                        placeholder = {
                            Text(
                                text = "Grupo muscular",
                                fontFamily = myFontBody,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = colorsTextFieldsCard,
                        textStyle = TextStyle(
                            fontFamily = myFontBody,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                        shape = CutCornerShape(topStart = 14.dp, bottomEnd = 14.dp)
                    )

                    for (exercicio in exerciciosSegundaFeira) {
                        Card(
                            modifier = Modifier.padding(bottom = 10.dp),
                            shape = CutCornerShape(topStart = 10.dp, bottomEnd = 10.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .background(MyRed.copy(0.3f))
                                    .padding(5.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${exercicio.nome} ${exercicio.numeroSeries}x${exercicio.numeroRepeticoes}",
                                    fontFamily = myFontBody,
                                    fontSize = 15.sp,
                                    color = MyBlack,
                                )
                                Icon(
                                    modifier = Modifier.clickable {
                                        exerciciosSegundaFeira.remove(
                                            exercicio
                                        )
                                    },
                                    tint = MyBlack,
                                    painter = painterResource(id = R.drawable.baseline_close_24),
                                    contentDescription = "Remover exercício",
                                )
                            }
                        }
                    }

                    if (expandedCamposExerciciosSegundaFeira) {
                        OutlinedTextField(
                            value = nomeExercicio,
                            onValueChange = {
                                if (it.length <= 100) {
                                    nomeExercicio = it
                                    isNomeExercicioError = false
                                }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Text,
                                capitalization = KeyboardCapitalization.Sentences
                            ),
                            isError = isNomeExercicioError,
                            supportingText = { if (isNomeExercicioError) Text("Campo obrigatório") },
                            placeholder = {
                                Text(
                                    text = "Nome do Exercício",
                                    fontFamily = myFontBody,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth(),
                            colors = colorsTextFieldsCard,
                            textStyle = TextStyle(
                                fontFamily = myFontBody,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                            ),
                            shape = CutCornerShape(topStart = 14.dp, bottomEnd = 14.dp)
                        )

                        Row {
                            val pattern = remember { Regex("^\\d*\$") }
                            OutlinedTextField(
                                value = numeroSeries,
                                onValueChange = {
                                    if (it.matches(pattern)) {
                                        numeroSeries = if (it.isNotEmpty()) {
                                            it.toInt().coerceAtMost(15).toString()
                                        } else {
                                            it
                                        }
                                        isNumeroSeriesError = false
                                    }
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                isError = isNumeroSeriesError,
                                supportingText = { if (isNumeroSeriesError) Text("Campo obrigatório") },
                                placeholder = {
                                    Text(
                                        text = "Series",
                                        fontFamily = myFontBody,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                colors = colorsTextFieldsCard,
                                textStyle = TextStyle(
                                    fontFamily = myFontBody,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                ),
                                shape = CutCornerShape(topStart = 14.dp, bottomEnd = 14.dp)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            OutlinedTextField(
                                value = numeroRepeticoes,
                                onValueChange = {
                                    if (it.matches(pattern)) {
                                        numeroRepeticoes = if (it.isNotEmpty()) {
                                            it.toInt().coerceAtMost(40).toString()
                                        } else {
                                            it
                                        }
                                        isNumeroRepeticoesError = false
                                    }
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                isError = isNumeroRepeticoesError,
                                supportingText = { if (isNumeroRepeticoesError) Text("Campo obrigatório") },
                                placeholder = {
                                    Text(
                                        text = "Repetições",
                                        fontFamily = myFontBody,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                colors = colorsTextFieldsCard,
                                textStyle = TextStyle(
                                    fontFamily = myFontBody,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                ),
                                shape = CutCornerShape(topStart = 14.dp, bottomEnd = 14.dp)
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                if (nomeExercicio.isNotEmpty() && numeroSeries.isNotEmpty() && numeroRepeticoes.isNotEmpty()) {
                                    exerciciosSegundaFeira.add(
                                        Exercicio(
                                            nome = nomeExercicio,
                                            numeroSeries = numeroSeries.toInt(),
                                            numeroRepeticoes = numeroRepeticoes.toInt(),
                                            idDiaTreino = 1
                                        )
                                    )
                                    nomeExercicio = ""
                                    numeroSeries = ""
                                    numeroRepeticoes = ""
                                    expandedCamposExerciciosSegundaFeira = false
                                } else {
                                    if (nomeExercicio.isEmpty()) {
                                        isNomeExercicioError = true
                                    }
                                    if (numeroSeries.isEmpty()) {
                                        isNumeroSeriesError = true
                                    }
                                    if (numeroRepeticoes.isEmpty()) {
                                        isNumeroRepeticoesError = true
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MyRed),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                        ) {
                            Text(
                                text = "OK",
                                fontFamily = myFontTitle,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = MyWhite,
                            )
                        }

                        Text(
                            text = "CANCELAR", fontFamily = myFontTitle,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MyBlack,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .clickable { expandedCamposExerciciosSegundaFeira = false }
                        )
                    } else {
                        OutlinedButton(
                            onClick = {
                                expandedCamposExerciciosSegundaFeira = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MyRed),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                        ) {
                            Text(
                                text = "Adicionar exercício",
                                fontFamily = myFontTitle,
                                fontSize = 18.sp,
                                color = MyWhite,
                            )
                        }
                    }

                }

            }
        }
    }
}