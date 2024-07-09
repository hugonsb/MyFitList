package com.happs.myfitlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsControllerCompat
import com.happs.myfitlist.navigation.NavCadastro
import com.happs.myfitlist.ui.theme.ThemeSwitcherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            // manter os icons da navBar visiveis
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightNavigationBars =
                false
            // manter os icons da statusBar visiveis
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
                false

            ThemeSwitcherTheme {
                NavCadastro()
            }
        }
    }
}