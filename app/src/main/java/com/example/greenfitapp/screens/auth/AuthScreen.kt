package com.example.greenfitapp.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.greenfitapp.R
import com.example.greenfitapp.data.auth.AuthManager


@Composable
fun AuthScreen(onAuthSuccess: () -> Unit, modifier: Modifier = Modifier) {
    var email by remember{ mutableStateOf("") }
    var password by remember{ mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var isRegistering by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val genericErrorMessage = stringResource(R.string.auth_error_message)

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        if(errorMessage != ""){
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isRegistering == true) {
            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text(stringResource(R.string.username_label)) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.email_label)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.password_label)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { if (isRegistering){
            AuthManager.signUp(email, password, userName, onComplete = { success ->
                if(success){
                    onAuthSuccess()
                }else{
                    errorMessage = genericErrorMessage
                }
            })
        }else{
            AuthManager.signIn(email, password, onComplete = {success ->
                if(success){
                    onAuthSuccess()
                }else{
                    errorMessage = genericErrorMessage
                }
            })
        } },
        enabled = email.contains("@") && password.length >= 6 && (!isRegistering || userName.isNotEmpty())
        ) {
            Text(
                text = if(isRegistering){stringResource(R.string.sign_up_text)}else{stringResource(R.string.sign_in_text)}
            )
        }

        TextButton(onClick = { isRegistering = !isRegistering }) {
            Text(text = if(!isRegistering){ stringResource(R.string.register_redirect)}else{stringResource(R.string.login_redirect)})
        }
    }
}