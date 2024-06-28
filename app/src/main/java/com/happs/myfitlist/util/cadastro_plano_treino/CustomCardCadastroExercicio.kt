package com.happs.myfitlist.util.cadastro_plano_treino

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.happs.myfitlist.model.treino.Exercicio
import com.happs.myfitlist.ui.theme.MyBlack
import com.happs.myfitlist.ui.theme.MyRed
import com.happs.myfitlist.ui.theme.MyWhite
import com.happs.myfitlist.ui.theme.myFontBody
import com.happs.myfitlist.ui.theme.myFontTitle
import com.happs.myfitlist.viewmodel.AppViewModelProvider
import com.happs.myfitlist.viewmodel.CriarPlanoTreinoViewModel

@Composable
fun CustomCardCadastroExercicio(
    dia: Int,
    onClickSalvar: () -> Unit,
    onClickCancelar: () -> Unit,
    viewiewModel: CriarPlanoTreinoViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    val uiState by viewiewModel.criarPlanoTreinoState.collectAsState()

    var isNomeExercicioError by rememberSaveable { mutableStateOf(false) }
    var isNumeroSeriesError by rememberSaveable { mutableStateOf(false) }
    var isNumeroRepeticoesError by rememberSaveable { mutableStateOf(false) }

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

    Card(shape = RoundedCornerShape(10.dp)) {
        Column(
            modifier = Modifier
                .background(MyRed.copy(0.15f))
                .padding(10.dp)
        ) {
            OutlinedTextField(
                value = uiState.nomeExercicio[dia],
                onValueChange = {
                    if (it.length <= 100) {
                        viewiewModel.setNomeExercicio(dia, it)
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
                    value = uiState.numeroSeries[dia],
                    onValueChange = {
                        if (it.matches(pattern)) {
                            if (it.isNotEmpty()) {
                                viewiewModel.setNumeroSeries(
                                    dia, it.toInt().coerceAtMost(15).toString()
                                )
                            } else {
                                viewiewModel.setNumeroSeries(dia, it)
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
                    value = uiState.numeroRepeticoes[dia],
                    onValueChange = {
                        if (it.matches(pattern)) {
                            if (it.isNotEmpty()) {
                                viewiewModel.setNumeroRepeticoes(
                                    dia,
                                    it.toInt().coerceAtMost(40).toString()
                                )
                            } else {
                                viewiewModel.setNumeroRepeticoes(dia, it)
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

                    val nomeExercicio = uiState.nomeExercicio[dia]
                    val numeroSeries = uiState.numeroSeries[dia]
                    val numeroRepeticoes = uiState.numeroRepeticoes[dia]

                    if (nomeExercicio.isNotEmpty() && numeroSeries.isNotEmpty() && numeroRepeticoes.isNotEmpty()) {

                        viewiewModel.adicionarExercicio(
                            dia,
                            Exercicio(
                                nome = nomeExercicio,
                                numeroSeries = numeroSeries.toInt(),
                                numeroRepeticoes = numeroRepeticoes.toInt(),
                                idDiaTreino = -1
                            )
                        )

                        viewiewModel.setNomeExercicio(dia, "")
                        viewiewModel.setNumeroSeries(dia, "")
                        viewiewModel.setNumeroRepeticoes(dia, "")
                        onClickSalvar()
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
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "OK",
                    fontFamily = myFontTitle,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MyWhite,
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "CANCELAR", fontFamily = myFontTitle,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MyBlack,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable { onClickCancelar() }
            )
        }
    }
}