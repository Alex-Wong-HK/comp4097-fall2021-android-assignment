package hk.edu.hkbu.comp.comp4097.qpon.ui.user

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import hk.edu.hkbu.comp.comp4097.qpon.Network
import hk.edu.hkbu.comp.comp4097.qpon.R

data class UserEntity (
    val createdAt: Float,
    val updatedAt: Float,
    val id:Float,
    val userID:Float,
    val username:String,
    val role:String,
    val coin:Float
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserLogin(context: Context,nav:NavHostController) {
    val focusRequester = remember {
        FocusRequester()
    }
    val usernameSate = remember { mutableStateOf("") }
    val passwordSate = remember { mutableStateOf("") }
    val isVisibility = remember {
        mutableStateOf(false)
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(

        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("user"),
            value = usernameSate.value,
            onValueChange = {
                usernameSate.value = it
            },
            label = { Text("Username") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),

            keyboardActions = KeyboardActions(
                onNext = {
                    focusRequester.requestFocus()
                }
            )
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .testTag("password"),
            value = passwordSate.value,
            onValueChange = { passwordSate.value = it },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation = if (isVisibility.value) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )
        )
        loginButton(usernameSate.value,passwordSate.value,nav,context)

    }
}


@Composable
fun UserLogout(context: Context,nav:NavHostController) {
    Column(Modifier.padding(8.dp)) {
        val sp = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier
            .fillMaxWidth()) {
        Card(
            modifier = Modifier.size(152.dp).fillMaxWidth(),
            shape = CircleShape,
            elevation = 2.dp,


            ) {
            Image(
                painterResource(R.drawable.ic_launcher_background),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
        Row(horizontalArrangement = Arrangement.SpaceBetween,modifier = Modifier
            .fillMaxWidth()) {
            sp.getString("user","Mem")?.let { Text(text = "Username : "+ it) }
            sp.getInt("coins",500)?.let { Text(text = "Coins : " +it.toString()) }
        }
        Button(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            onClick = {
                Network.Logout(context)
                Thread.sleep(1000)

                nav.navigate("user")

            }) {
            Text("Logout")
        }

        Button(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            onClick = {
                nav.navigate("redeem")
            }) {
            Text("My Redeem")
        }
    }
}

@Composable
fun loginButton(username:String,password:String,nav: NavHostController,context: Context) {
    var message:String = ""
    MaterialTheme {
        Column {
            val openDialog = remember { mutableStateOf(false) }
            Button(
                modifier = Modifier.padding(top = 16.dp),
                onClick = {
                    if (Network.sendPostRequest(username,password,nav,context)){
                        nav.navigate("user")
                    }else{
                        Thread.sleep(1000)
                        if(Network.result.isBlank()){
                            message = "Login Fail"
                        }else{
                            message = Network.result
                        }
                        openDialog.value =true
                    }
                }) {
                Text("Login")
            }

            if (openDialog.value) {

                AlertDialog(
                    onDismissRequest = {
                        openDialog.value = false
                    },
                    title = {
                        Text(text = "Alert")
                    },
                    text = {
                        Text(message)
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                openDialog.value = false
                            }) {
                            Text("Confirm")
                        }
                    },

                    )
            }
        }
    }
}