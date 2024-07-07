package com.happs.myfitlist.view.configuracoes

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.happs.myfitlist.R
import com.happs.myfitlist.ui.theme.MyBlack
import com.happs.myfitlist.ui.theme.MyWhite
import com.happs.myfitlist.ui.theme.myFontTitle
import com.happs.myfitlist.util.CustomAlertDialog
import com.happs.myfitlist.viewmodel.AppViewModelProvider
import com.happs.myfitlist.viewmodel.cadastro.CadastroViewModel
import kotlinx.coroutines.launch

@Composable
fun ConfiguracoesView(
    navControllerConfiguracoes: NavController,
    navControllerCadastro: NavController,
    viewModel: CadastroViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(top = 10.dp)
    ) {
        Header()
        Spacer(modifier = Modifier.height(20.dp))
        Menu(navControllerConfiguracoes, navControllerCadastro, viewModel)
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Icon(
                tint = MyWhite,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .align(Alignment.BottomEnd),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null
            )
        }
    }
}

@Composable
fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Configurações",
            fontFamily = myFontTitle,
            fontSize = 50.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            color = MyWhite,
        )
    }
}

@Composable
fun Menu(
    navControllerConfiguracoes: NavController,
    navControllerCadastro: NavController,
    viewModel: CadastroViewModel
) {

    val coroutineScope = rememberCoroutineScope()

    val openDialog = remember { mutableStateOf(false) }

    if (openDialog.value) {
        CustomAlertDialog(
            title = stringResource(R.string.confirmar),
            text = "Tem certeza? Todos os dados serão apagados.",
            textButtomConfirm = stringResource(R.string.confirmar),
            onclose = { openDialog.value = false },
            onConfirm = {
                coroutineScope.launch {
                    viewModel.deleteAllData()
                }
                openDialog.value = false


                navControllerCadastro.navigate("cadastro") {

                    // // evitar abrir novamente a mesma tela ao reselecionar mesmo item
                    launchSingleTop = true
                    // restaura o estado ao voltar para a tela anterior
                    restoreState = false
                }
            }
        )
    }

    Column {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(horizontal = 10.dp)
                .clickable(true, onClick = {
                    navControllerConfiguracoes.navigate("editar_dados_pessoais") { launchSingleTop = true }
                }),
            shape = CutCornerShape(topStart = 10.dp, bottomEnd = 10.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(start = 10.dp),
                Arrangement.Start,
                Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(35.dp),
                    painter = painterResource(id = R.drawable.baseline_person_24),
                    contentDescription = null,
                )
                Text(
                    text = "Alterar dados pessoais",
                    fontSize = 20.sp,
                    color = MyBlack
                )
            }
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(15.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(horizontal = 10.dp)
                .clickable(true, onClick = {
                    openDialog.value = true
                }),
            shape = CutCornerShape(topStart = 10.dp, bottomEnd = 10.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(start = 10.dp),
                Arrangement.Start,
                Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(35.dp),
                    painter = painterResource(id = R.drawable.baseline_delete_forever_24),
                    contentDescription = null,
                )
                Text(
                    text = "Excluir todos os dados",
                    fontSize = 20.sp,
                    color = MyBlack
                )
            }
        }
    }
}