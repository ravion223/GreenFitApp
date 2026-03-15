package com.example.greenfitapp.screens

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import com.example.greenfitapp.R

@Composable
fun SettingsScreen (
    isDarkMode: Boolean,
    onThemeChange: (Boolean) -> Unit
){
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        ThemeSetting(isDarkMode, onThemeChange)
        LanguageSetting()
    }
}

@Composable
fun ThemeSetting(
    isDarkMode: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    var checked by remember { mutableStateOf(true) }


    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.dark_mode_switch)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Switch(
            checked = isDarkMode,
            onCheckedChange = { newCheckedState ->
                onThemeChange(newCheckedState)
            },

        )
    }
}

@Composable
fun LanguageSetting() {
    var expanded by remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(R.string.switch_language)
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = { expanded = !expanded }
        ) {
            Icon(
                imageVector = Icons.Default.Language,
                contentDescription = stringResource(R.string.icon_language)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(R.string.english_lang)
                    )
                },
                onClick = {
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("en"))
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(R.string.ukrainian_lang)
                    )
                },
                onClick = {
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("uk"))
                    expanded = false
                }

            )
        }
    }
}