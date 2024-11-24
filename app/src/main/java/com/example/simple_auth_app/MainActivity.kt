package com.example.simple_auth_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.BaseColumns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simple_auth_app.dbHandler.FeedReaderContract
import com.example.simple_auth_app.dbHandler.FeedReaderDbHelper
import com.example.simple_auth_app.ui.theme.Simple_auth_appTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Simple_auth_appTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var msg by remember { mutableStateOf("") }
    val context = LocalContext.current
    val PREF_NAME = "MyPref"
    val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    var session by remember { mutableStateOf(prefs.getBoolean("State", false)) }



    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (session){
            context.startActivity(Intent(context, Home::class.java))
        }

        Text(
            text = "Connexion",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Adresse e-mail") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mot de passe") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (msg.isNotEmpty()) {
            Text(
                text = msg,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(9.dp))
        }

        Button(
            onClick = {
                if (email.isEmpty() || password.isEmpty()) {
                    msg = "Veuillez remplir tous les champs."
                } else {
                    prefs.edit().putBoolean("State", true).apply()
                    context.startActivity(Intent(context, Home::class.java))
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Se connecter")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = {
            context.startActivity(Intent(context, Register::class.java))
        }) {
            Text(text = "Créer un nouveau compte")
        }

        TextButton(onClick = {
            context.startActivity(Intent(context, ForgotPassword::class.java))
        }) {
            Text(text = "Mot de passe oublié ?")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    Simple_auth_appTheme {
        LoginScreen()
    }
}