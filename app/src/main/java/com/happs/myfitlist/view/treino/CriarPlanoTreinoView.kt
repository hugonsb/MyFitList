package com.happs.myfitlist.view.treino

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.happs.myfitlist.navigation.canGoBack
import com.happs.myfitlist.ui.theme.MyRed
import com.happs.myfitlist.ui.theme.MyWhite
import com.happs.myfitlist.ui.theme.TextFieldColors
import com.happs.myfitlist.ui.theme.myFontBody
import com.happs.myfitlist.ui.theme.myFontTitle
import com.happs.myfitlist.util.cadastro_plano_treino.CustomCardCadastroDiaSemana
import com.happs.myfitlist.util.CustomTopAppBar
import com.happs.myfitlist.util.cadastro_plano_treino.DiasList
import com.happs.myfitlist.viewmodel.AppViewModelProvider
import com.happs.myfitlist.viewmodel.CriarPlanoTreinoViewModel
import kotlinx.coroutines.launch

@Composable
fun CriarPlanoTreinoView(
    navController: NavHostController,
    viewModel: CriarPlanoTreinoViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val coroutineScope = rememberCoroutineScope()

    val listDiasState = rememberLazyListState()

    var isNomePlanoTreinoEror by rememberSaveable { mutableStateOf(false) }

    val uiState by viewModel.criarPlanoTreinoState.collectAsState()

    var enabledButton by remember { mutableStateOf(true) }

    val ctx = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(start = 10.dp, end = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CustomTopAppBar(onBackPressed = {
            if (navController.canGoBack) {
                navController.popBackStack("treino", false)
            }
        }, barTitle = "Criar plano de treino")


        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), state = listDiasState
        ) {
            item {
                OutlinedTextField(
                    value = uiState.nomePlanoTreino,
                    onValueChange = {
                        if (it.length <= 100) {
                            viewModel.setNomePlanoTreino(it)
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
                    colors = TextFieldColors.colorsTextFields(),
                    textStyle = TextStyle(
                        fontFamily = myFontBody,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    shape = CutCornerShape(topStart = 14.dp, bottomEnd = 14.dp)
                )
            }

            itemsIndexed(DiasList.dias) { index, dia ->
                CustomCardCadastroDiaSemana(
                    index,
                    dia
                )
            }
        }

        if (enabledButton) {
            OutlinedButton(
                onClick = {
                    coroutineScope.launch {
                        enabledButton = false
                        val (success, message) = viewModel.savePlanoTreino()
                        if (success) {
                            navController.navigate("treino")
                        } else {
                            enabledButton = true
                        }
                        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MyRed),
                shape = CutCornerShape(topStart = 14.dp, bottomEnd = 14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .height(70.dp)
                    .align(Alignment.CenterHorizontally),
            ) {
                Text(
                    text = "SALVAR PLANO",
                    fontFamily = myFontTitle,
                    fontSize = 25.sp,
                    color = MyWhite,
                )
            }
        }
    }
}