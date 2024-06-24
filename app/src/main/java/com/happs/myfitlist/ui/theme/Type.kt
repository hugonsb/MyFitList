package com.happs.myfitlist.ui.theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.happs.myfitlist.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val myFontBody = FontFamily(
    Font(
        googleFont = GoogleFont("Roboto"),
        fontProvider = provider
    )
)

val myFontTitle = FontFamily(
    Font(
        googleFont = GoogleFont("Bebas Neue"),
        fontProvider = provider
    )
)