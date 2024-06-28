package com.happs.myfitlist.util.cadastro_plano_treino

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.happs.myfitlist.R
import com.happs.myfitlist.ui.theme.MyBlack
import com.happs.myfitlist.ui.theme.MyRed
import com.happs.myfitlist.ui.theme.MyWhite
import com.happs.myfitlist.ui.theme.TextFieldColors
import com.happs.myfitlist.ui.theme.myFontBody
import com.happs.myfitlist.ui.theme.myFontTitle
import com.happs.myfitlist.viewmodel.AppViewModelProvider
import com.happs.myfitlist.viewmodel.CriarPlanoTreinoViewModel

@Composable
fun CustomCardCadastroDiaSemana(
    indiceDia: Int,
    nomeDia: String,
    viewModel: CriarPlanoTreinoViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    val uiState by viewModel.criarPlanoTreinoState.collectAsState()

    var isGrupoMuscularError by rememberSaveable { mutableStateOf(false) }

    var expandedAdicionarExercicio by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
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
                text = nomeDia,
                fontFamily = myFontTitle,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = MyBlack,
            )

            Column {
                OutlinedTextField(
                    value = uiState.grupoMuscular[indiceDia],
                    onValueChange = {
                        if (it.length <= 100) {
                            viewModel.setGrupoMuscular(indiceDia, it)
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
                    colors = TextFieldColors.colorsTextFieldsCard(),
                    textStyle = TextStyle(
                        fontFamily = myFontBody,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    shape = CutCornerShape(topStart = 14.dp, bottomEnd = 14.dp)
                )

                uiState.exerciciosList[indiceDia].forEach { exercicio ->
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
                                    viewModel.removerExercicio(indiceDia, exercicio)
                                },
                                tint = MyBlack,
                                painter = painterResource(id = R.drawable.baseline_close_24),
                                contentDescription = "Remover exercício",
                            )
                        }
                    }
                }

                if (expandedAdicionarExercicio) {
                    CustomCardCadastroExercicio(
                        indiceDia,
                        onClickSalvar = { expandedAdicionarExercicio = false },
                        onClickCancelar = { expandedAdicionarExercicio = false })
                } else {
                    OutlinedButton(
                        onClick = {
                            expandedAdicionarExercicio = true
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