package com.example.simple_auth_app

import android.content.Context
import android.os.Bundle
import android.provider.BaseColumns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simple_auth_app.data.Product
import com.example.simple_auth_app.dbHandler.FeedReaderContract
import com.example.simple_auth_app.dbHandler.FeedReaderDbHelper
import com.example.simple_auth_app.ui.theme.Simple_auth_appTheme

class UpdateProduct : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ProdId = intent.getLongExtra("ProdId",0)

        enableEdgeToEdge()
        setContent {
            Simple_auth_appTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    UpdateProd(
                        id = ProdId,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun UpdateProd(id: Long , modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val dbHelper = FeedReaderDbHelper(context)
    val productDetail by remember { _GetOneProdsFromDB(id, dbHelper) }
    var name by remember { mutableStateOf(productDetail?.name ?: "") }
    var price by remember { mutableStateOf(productDetail?.price?.toString() ?: "") }
    var description by remember { mutableStateOf(productDetail?.description ?: "") }
    var imageUrl by remember { mutableStateOf(productDetail?.imageUrl ?: "") }

    if (productDetail !== null){
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
                        dbHelper.updateData(id, name, priceValue, description, imageUrl)
                        Toast.makeText(context, "Product Updated!", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Mis a jour")
            }
        }
    }
}

private fun _GetOneProdsFromDB(id: Long, dbHelper: FeedReaderDbHelper): MutableState<Product?> {
    val product = mutableStateOf<Product?>(null)

    val cursor = dbHelper.readOneById(id)
    if (cursor.moveToFirst()) {
        val _id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID))
        val _name = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_NAME))
        val _price = cursor.getDouble(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_PRICE))
        val _description = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION))
        val _imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_IMAGE_URL))

        product.value = Product(
            id = _id,
            name = _name,
            price = _price,
            description = _description,
            imageUrl = _imageUrl
        )
    }
    cursor.close()

    return product
}