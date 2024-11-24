package com.example.simple_auth_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.* // For layout
import androidx.compose.material3.* // For Material Design 3 components
import androidx.compose.runtime.* // For state management
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simple_auth_app.ui.theme.Simple_auth_appTheme

class ForgotPassword : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Simple_auth_appTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ForgotPasswordScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ForgotPasswordScreen(modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var msg by remember { mutableStateOf("") }
    val context = LocalContext.current;

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Réinitialiser le mot de passe",
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

        if (msg === "Veuillez saisir votre adresse e-mail.") {
            Text(
                text = msg,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (msg === "Un e-mail de réinitialisation a été envoyé.") {
            Text(
                text = msg,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                if (email.isEmpty()) {
                    msg = "Veuillez saisir votre adresse e-mail."
                } else {
                    msg = "Un e-mail de réinitialisation a été envoyé."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Envoyer")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = {
            context.startActivity(Intent(context, MainActivity::class.java))
        }) {
            Text(text = "Revenir à la connexion")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    Simple_auth_appTheme {
        ForgotPasswordScreen()
    }
}
