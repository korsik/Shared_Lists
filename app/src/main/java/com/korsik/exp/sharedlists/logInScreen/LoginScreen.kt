package com.korsik.exp.sharedlists.logInScreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.korsik.exp.sharedlists.R

@Composable
fun LoginScreen(
    viewModel: LoginScreenViewModel,
    navController: NavController
) {
    viewModel.onSuccess(navController = navController)
    val loginState = viewModel.buttonType.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val emailState = viewModel.emailState.value
            val password = viewModel.passwordState.value
            Image(painter = painterResource(id = R.drawable.korsik_list), contentDescription = null)
            TextField(
                value = emailState,
                onValueChange = { viewModel.onEmailFieldChanged(it) },
                label = {Text(text = "email address")}
            )
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            TextField(
                value = password,
                onValueChange = { viewModel.onPasswordFieldChanged(it) },
                label = { Text(text = "password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            Button(
                onClick = {
                    viewModel.onSubmitBtnPressed(emailState, password, navController = navController)
                }
            ) {
                Text(text = loginState.value.btnText)
            }
            Spacer(modifier = Modifier.padding(vertical = 3.dp))
            Text(
                text = buildAnnotatedString {
                  append(loginState.value.bottomString)
                  withStyle(style = SpanStyle(color = MaterialTheme.colors.primaryVariant)){
                      append(loginState.value.bottomText)
                  }
                },
                fontStyle = FontStyle.Italic,
                fontSize = 12.sp,
                modifier = Modifier
                    .clickable {
                       viewModel.onSwapType()
                    }
            )
        }

        if(viewModel.validatedInput.value) {
            viewModel.validatedInput.value = false
            Toast.makeText(
                LocalContext.current,
                viewModel.errorMessage.value,
                Toast.LENGTH_SHORT)
                .show()
        }

    }
}