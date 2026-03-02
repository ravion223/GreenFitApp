package com.example.greenfitapp.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.greenfitapp.R
import com.example.greenfitapp.data.auth.AuthManager
import com.example.greenfitapp.ui.theme.GreenFitAppTheme


@Composable
fun AuthScreen(onAuthSuccess: () -> Unit, modifier: Modifier = Modifier) {
    var email by remember{ mutableStateOf("") }
    var password by remember{ mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var isRegistering by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val genericErrorMessage = stringResource(R.string.auth_error_message)

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(16.dp)
    ) {

        Text(
            text = if(isRegistering){stringResource(R.string.register_label)}else{stringResource(R.string.login_label)},
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        if(errorMessage != ""){
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isRegistering == true) {
                    OutlinedTextField(
                        value = userName,
                        onValueChange = { userName = it },
                        label = { Text(stringResource(R.string.username_label)) },
                        colors = OutlinedTextFieldDefaults.colors(MaterialTheme.colorScheme.primary),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    label = { Text(stringResource(R.string.email_label)) },
                    colors = OutlinedTextFieldDefaults.colors(MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    label = { Text(stringResource(R.string.password_label)) },
                    colors = OutlinedTextFieldDefaults.colors(MaterialTheme.colorScheme.primary),
                    visualTransformation = if(passwordVisible){ VisualTransformation.None }else{PasswordVisualTransformation()},
                    trailingIcon = @Composable{ IconButton(
                        onClick = { passwordVisible = !passwordVisible }
                    ) {
                        Icon(
                            imageVector = if(passwordVisible){Icons.Default.VisibilityOff}else{Icons.Default.Visibility},
                            contentDescription = if(passwordVisible){stringResource(R.string.hide_password)}else{stringResource(R.string.show_password)}
                        )
                    }},
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }


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

@Preview(showBackground = true)
@Composable
fun AuthScreenPreview(){
    GreenFitAppTheme() {
        AuthScreen(onAuthSuccess = { /* */ })
    }
}