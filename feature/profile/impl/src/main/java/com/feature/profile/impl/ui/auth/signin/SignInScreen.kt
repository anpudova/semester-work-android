@file:OptIn(ExperimentalMaterial3Api::class)

package com.feature.profile.impl.ui.auth.signin

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.itis.core.utils.PreferencesManager

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Composable
fun SignInScreen(
    navController: NavController,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    SignInContent(
        viewState = state.value,
        eventHandler = viewModel::event
    )

    SignInActions(
        navController = navController,
        viewAction = action
    )
}

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Composable
fun SignInContent(
    viewState: SignInViewState,
    eventHandler: (SignInEvent) -> Unit,
) {
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }

    val preferencesManager = PreferencesManager(LocalContext.current)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "SignIn",
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(text = "username") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "password") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Text(
            text = viewState.messageState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            color = Color.Red
        )

        Button(
            onClick = {
                eventHandler.invoke(SignInEvent.OnSignInClick(username.text, password.text))
                if (viewState.idState != -1L) {
                    preferencesManager.saveDataString("username", username.text)
                    preferencesManager.saveDataLong("id", viewState.idState)
                    Log.i("ID USER", "SignInContent: ${viewState.idState}")
                    eventHandler.invoke(SignInEvent.OnNavigateProfile)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "SignIn")
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "no account? "
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    eventHandler.invoke(SignInEvent.OnSignUpNavigateClick)
                },
                modifier = Modifier
                    .heightIn(min = 20.dp)
            ) {
                Text(text = "SignUp")
            }
        }
    }
}

@Composable
private fun SignInActions(
    navController: NavController,
    viewAction: SignInAction?
) {
    LaunchedEffect(viewAction) {
        when (viewAction) {
            null -> Unit
            SignInAction.NavigateSignUp -> {
                navController.navigate("signup")
            }
            SignInAction.NavigateProfile -> {
                navController.navigate("profile")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.GINGERBREAD)
@Preview(showBackground = true)
@Composable
fun SignInPreview() {
    SignInContent(
        viewState = SignInViewState(
            messageState = ""
        ), {}
    )
}
