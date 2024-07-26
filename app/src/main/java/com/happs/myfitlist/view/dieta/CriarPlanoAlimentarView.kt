package com.happs.myfitlist.view.dieta

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.happs.myfitlist.R
import com.happs.myfitlist.model.dieta.Refeicao
import com.happs.myfitlist.navigation.canGoBack
import com.happs.myfitlist.ui.theme.MyBlack
import com.happs.myfitlist.ui.theme.MyRed
import com.happs.myfitlist.ui.theme.MyWhite
import com.happs.myfitlist.ui.theme.TextFieldColors
import com.happs.myfitlist.ui.theme.TextFieldColors.colorsTextFieldsCard
import com.happs.myfitlist.ui.theme.myFontBody
import com.happs.myfitlist.ui.theme.myFontTitle
import com.happs.myfitlist.util.CustomAlertDialog
import com.happs.myfitlist.util.CustomTopAppBar
import com.happs.myfitlist.util.DiasList
import com.happs.myfitlist.util.pager.PageIndicator
import com.happs.myfitlist.viewmodel.dieta.CriarPlanoAlimentarViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CriarPlanoAlimentarView(
    navController: NavHostController,
    criarPlanoDietaViewModel: CriarPlanoAlimentarViewModel = koinViewModel<CriarPlanoAlimentarViewModel>()
) {

    val coroutineScope = rememberCoroutineScope()
    val uiState by criarPlanoDietaViewModel.criarPlanoAlimentarState.collectAsState()

    var isNomePlanoDietaError by rememberSaveable { mutableStateOf(false) }

    var enabledButton by remember { mutableStateOf(true) }

    val ctx = LocalContext.current

    val pagerState = rememberPagerState(pageCount = { DiasList.dias.size })
    val scrollState = rememberScrollState()

    val localFocusManager = LocalFocusManager.current

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { _ ->
            localFocusManager.clearFocus()
        }
    }

    val openDialog = remember { mutableStateOf(false) }

    BackHandler {
        openDialog.value = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(start = 10.dp, end = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (openDialog.value) {
            CustomAlertDialog(
                title = stringResource(R.string.deseja_voltar),
                text = stringResource(R.string.seus_dados_nao_serao_salvos),
                textButtomConfirm = stringResource(R.string.voltar),
                onclose = { openDialog.value = false },
                onConfirm = {
                    if (navController.canGoBack) {
                        navController.popBackStack("dieta", false)
                    }
                    openDialog.value = false
                }
            )
        }

        CustomTopAppBar(onBackPressed = {
            openDialog.value = true
        }, barTitle = "Criar plano alimentar")

        OutlinedTextField(
            value = uiState.nomePlanoAlimentar,
            onValueChange = {
                if (it.length <= 100) {
                    criarPlanoDietaViewModel.setNomePlanoAlimentar(it)
                    isNomePlanoDietaError = false
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences
            ),
            isError = isNomePlanoDietaError,
            supportingText = { if (isNomePlanoDietaError) Text(stringResource(R.string.digite_um_nome_para_o_plano)) },
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

        PageIndicator(
            pageCount = DiasList.dias.size,
            currentPage = pagerState.currentPage,
            pagerState = pagerState,
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                HorizontalPager(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                    beyondBoundsPageCount = 2,
                    state = pagerState,
                    verticalAlignment = Alignment.Top,
                    key = { pageIndex -> pageIndex }
                ) { currentPage ->
                    CustomCardCadastroDiaSemanaDieta(currentPage, criarPlanoDietaViewModel)
                }
            }
        }

        if (enabledButton) {
            FilledTonalButton(
                onClick = {
                    coroutineScope.launch {
                        enabledButton = false
                        val (success, message) = criarPlanoDietaViewModel.savePlanoAlimentar()
                        if (success) {
                            navController.popBackStack("dieta", false)
                        } else {
                            enabledButton = true
                        }
                        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MyWhite),
                shape = CutCornerShape(topStart = 14.dp, bottomEnd = 14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .height(70.dp)
                    .align(Alignment.CenterHorizontally),
            ) {
                Text(
                    text = stringResource(R.string.salvar_plano),
                    fontFamily = myFontTitle,
                    fontSize = 25.sp,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
fun CustomCardCadastroDiaSemanaDieta(
    indiceDia: Int,
    criarPlanoAlimentarViewModel : CriarPlanoAlimentarViewModel
) {

    val uiState by criarPlanoAlimentarViewModel.criarPlanoAlimentarState.collectAsState()

    var enabledButton by remember { mutableStateOf(true) }

    val openDialog = remember { mutableStateOf(false) }

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
                text = DiasList.dias[indiceDia],
                fontFamily = myFontTitle,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = MyBlack,
            )


            uiState.refeicoesList[indiceDia].forEach { refeicao ->
                Card(
                    modifier = Modifier.padding(bottom = 10.dp),
                    shape = CutCornerShape(topStart = 10.dp, bottomEnd = 10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary.copy(0.5f))
                            .padding(8.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${refeicao.tipo}:",
                                fontFamily = myFontBody,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MyWhite,
                            )
                            Text(
                                text = refeicao.detalhes,
                                fontFamily = myFontBody,
                                fontSize = 15.sp,
                                color = MyWhite,
                            )
                        }
                        Icon(
                            modifier = Modifier
                                .clickable {
                                    criarPlanoAlimentarViewModel.removerRefeicao(indiceDia, refeicao)
                                },
                            tint = MyWhite,
                            painter = painterResource(id = R.drawable.baseline_close_24),
                            contentDescription = "Remover exercício",
                        )
                    }
                }
            }

            if (openDialog.value) {
                CustomAlertDialogCadastroRefeicao(
                    title = "Adicionar Refeição",
                    dia = indiceDia,
                    onClickOk = {
                        openDialog.value = false
                        enabledButton = true
                    },
                    onClickCancelar = {
                        openDialog.value = false
                        enabledButton = true
                    },
                    criarPlanoAlimentarViewModel)
            }

            OutlinedButton(
                onClick = {
                    enabledButton = false
                    openDialog.value = true
                },
                enabled = enabledButton,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MyRed,
                    disabledContainerColor = MyRed.copy(0.5f)
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
            ) {
                Text(
                    text = "Adicionar refeição",
                    fontFamily = myFontTitle,
                    fontSize = 18.sp,
                    color = MyWhite,
                )
            }

        }
    }
}


@Composable
fun CustomAlertDialogCadastroRefeicao(
    title: String,
    dia: Int,
    onClickOk: () -> Unit,
    onClickCancelar: () -> Unit,
    criarPlanoAlimentarViewModel : CriarPlanoAlimentarViewModel
) {
    val uiState by criarPlanoAlimentarViewModel.criarPlanoAlimentarState.collectAsState()

    var isTipoRefeicaoError by rememberSaveable { mutableStateOf(false) }
    var isDetalhesRefeicaoError by rememberSaveable { mutableStateOf(false) }

    var checkerClone by rememberSaveable { mutableStateOf(false) }

    AlertDialog(
        shape = CutCornerShape(topStart = 24.dp, bottomEnd = 24.dp),
        containerColor = Color.White,
        onDismissRequest = {
            onClickCancelar()
        },
        title = {
            Text(
                title,
                fontSize = 35.sp,
                color = MyBlack,
                textAlign = TextAlign.Center,
                fontFamily = myFontTitle,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = uiState.tipoRefeicao[dia],
                    onValueChange = {
                        if (it.length <= 200) {
                            criarPlanoAlimentarViewModel.setTipoRefeicao(dia, it)
                            isTipoRefeicaoError = false
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    isError = isTipoRefeicaoError,
                    supportingText = { if (isTipoRefeicaoError) Text(stringResource(R.string.campo_obrigat_rio)) },
                    placeholder = {
                        Text(
                            text = "Tipo (Almoço, Jantar, etc)",
                            fontFamily = myFontBody,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = colorsTextFieldsCard(),
                    textStyle = TextStyle(
                        fontFamily = myFontBody,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    shape = CutCornerShape(topStart = 14.dp, bottomEnd = 14.dp)
                )

                OutlinedTextField(
                    value = uiState.detalhesRefeicao[dia],
                    onValueChange = {
                        if (it.length <= 500) {
                            criarPlanoAlimentarViewModel.setDetalhesRefeicao(dia, it)
                            isDetalhesRefeicaoError = false
                        }
                    },
                    singleLine = false,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    isError = isDetalhesRefeicaoError,
                    supportingText = { if (isDetalhesRefeicaoError) Text(stringResource(R.string.campo_obrigat_rio)) },
                    maxLines = 8,
                    placeholder = {
                        Text(
                            text = "Detalhes da refeição",
                            fontFamily = myFontBody,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = colorsTextFieldsCard(),
                    textStyle = TextStyle(
                        fontFamily = myFontBody,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    shape = CutCornerShape(topStart = 14.dp, bottomEnd = 14.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Checkbox(
                        checked = checkerClone,
                        onCheckedChange = { checkerClone = !checkerClone },
                        colors = CheckboxDefaults.colors(
                            MaterialTheme.colorScheme.primary,
                            checkmarkColor = MyWhite
                        )
                    )
                    Text(
                        text = "Adicionar essa refeição em todos os outros dias da semana",
                        fontFamily = myFontBody,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 12.sp,
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                OutlinedButton(
                    onClick = {
                        val tipoRefeicao = uiState.tipoRefeicao[dia]
                        val detalhesRefeicao = uiState.detalhesRefeicao[dia]

                        if (tipoRefeicao.isNotEmpty() && detalhesRefeicao.isNotEmpty()) {

                            if (checkerClone) {
                                for (i in 0..6) {
                                    adicionarRefeicao(
                                        i,
                                        tipoRefeicao,
                                        detalhesRefeicao,
                                        onClickOk,
                                        criarPlanoAlimentarViewModel
                                    )
                                }
                            } else {
                                adicionarRefeicao(
                                    dia,
                                    tipoRefeicao,
                                    detalhesRefeicao,
                                    onClickOk,
                                    criarPlanoAlimentarViewModel
                                )
                            }

                        } else {
                            if (tipoRefeicao.isEmpty()) {
                                isTipoRefeicaoError = true
                            }
                            if (detalhesRefeicao.isEmpty()) {
                                isDetalhesRefeicaoError = true
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MyRed),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(120.dp)
                        .height(50.dp)
                ) {
                    Text(
                        text = stringResource(R.string.ok),
                        fontFamily = myFontTitle,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MyWhite,
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = stringResource(id = R.string.cancelar), fontFamily = myFontTitle,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = MyBlack,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable { onClickCancelar() }
                )
            }
        },
        confirmButton = {},
        dismissButton = {},
    )
}

fun adicionarRefeicao(
    indiceDia: Int,
    tipo: String,
    detalhes: String,
    onClickOk: () -> Unit,
    criarPlanoAlimentarViewModel : CriarPlanoAlimentarViewModel
) {
    criarPlanoAlimentarViewModel.adicionarRefeicao(
        indiceDia,
        Refeicao(
            tipo = tipo,
            detalhes = detalhes,
            idDiaDieta = -1
        )
    )

    criarPlanoAlimentarViewModel.setTipoRefeicao(indiceDia, "")
    criarPlanoAlimentarViewModel.setDetalhesRefeicao(indiceDia, "")
    onClickOk()
}