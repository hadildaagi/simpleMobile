package com.example.simple_auth_app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simple_auth_app.dbHandler.FeedReaderDbHelper
import com.example.simple_auth_app.ui.theme.Simple_auth_appTheme

class AddProduct : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Simple_auth_appTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AddProductForm(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AddProductForm(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val dbHelper = FeedReaderDbHelper(context)
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Product Name") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = { Text("Image URL") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                  if (name.isNotEmpty() && price.isNotEmpty() && description.isNotEmpty() && imageUrl.isNotEmpty()) {
                      val priceValue = price.toDoubleOrNull() ?: 0.0
                      dbHelper.insertData(
                          name,
                          priceValue,
                          description,
                          imageUrl
                      )
                      Toast.makeText(context, "Product Added!", Toast.LENGTH_SHORT).show()
                      name = ""
                      price = ""
                      description = ""
                      imageUrl = ""
                  }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ajouter")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddProductPreview() {
    Simple_auth_appTheme {
        AddProductForm()
    }
}