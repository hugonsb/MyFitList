package com.happs.myfitlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.core.view.WindowInsetsControllerCompat
import com.happs.myfitlist.navigation.NavCadastro
import com.happs.myfitlist.ui.theme.ThemeSwitcherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // manter os icons da navBar visiveis ao trocar cor do tema
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightNavigationBars =
                !isSystemInDarkTheme()
            // manter os icons da statusBar visiveis ao trocar cor do tema
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
                !isSystemInDarkTheme()

            ThemeSwitcherTheme {
                NavCadastro()
            }
        }
    }
}