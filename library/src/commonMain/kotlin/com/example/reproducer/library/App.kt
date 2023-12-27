package com.example.reproducer.library

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.skeptick.libres.compose.painterResource

@Composable
fun App() {
    Column(Modifier.statusBarsPadding()) {
        Text(Res.string.app_name)
        Icon(
            painterResource(Res.image.ic_info),
            contentDescription = null
        )
    }
}