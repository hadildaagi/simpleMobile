package com.example.simple_auth_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.BaseColumns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simple_auth_app.data.Product
import com.example.simple_auth_app.dbHandler.FeedReaderContract
import com.example.simple_auth_app.dbHandler.FeedReaderDbHelper
import com.example.simple_auth_app.ui.theme.Simple_auth_appTheme

class Home : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Simple_auth_appTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val PREF_NAME = "MyPref"
    val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    val dbHelper = FeedReaderDbHelper(context)
    var list by remember { mutableStateOf(_GetAllProdsFromDB(dbHelper)) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hello",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
//                prefs.edit().remove("State").apply()
                prefs.edit().putBoolean("State", false).apply()
                context.startActivity(Intent(context, MainActivity::class.java))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Se deconnecter")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                context.startActivity(Intent(context, AddProduct::class.java))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Ajouter un produit")
        }

        ListComposable(modifier = Modifier,  context = context, dbHelper = dbHelper, list = list)

    }
}

@Composable
fun ListComposable(modifier: Modifier = Modifier, dbHelper : FeedReaderDbHelper, context: Context, list: List<Product>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(list) { prod ->
            ItemComposable(product = prod, dbHelper = dbHelper, context = context)
        }
    }
}


@Composable
fun ItemComposable(modifier: Modifier = Modifier, dbHelper : FeedReaderDbHelper, context: Context, product: Product) {
    val IntentProdDesc = Intent(context, ProductDescription::class.java)
    val IntentUpdateProd = Intent(context, UpdateProduct::class.java)

    Card(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable {
                IntentProdDesc.putExtra("ProdId", product.id)
                context.startActivity(IntentProdDesc)
            },
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = product.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            val imageId = context.resources.getIdentifier(product.imageUrl, "drawable", context.packageName)
            Image(
                painter = painterResource(id = imageId),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(8.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$${product.price}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        dbHelper.deleteData(product.id)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Supprimer")
                }

                Button(
                    onClick = {
                        IntentUpdateProd.putExtra("ProdId", product.id)
                        context.startActivity(IntentUpdateProd)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Mis à jour")
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    Simple_auth_appTheme {
        HomeScreen()
    }
}

private fun _GetAllProdsFromDB(dbHelper: FeedReaderDbHelper): MutableList<Product> {
    val productList = mutableListOf<Product>()

    productList.clear()

    val cursor = dbHelper.readAllData()
    with(cursor) {
        while (moveToNext()) {
            val itemId = getLong(getColumnIndexOrThrow(BaseColumns._ID))
            val itemName = getString(getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_NAME))
            val itemPrice = getDouble(getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_PRICE))
            val itemDescription = getString(getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION))
            val itemImageUrl = getString(getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_IMAGE_URL))

            productList.add(
                Product(
                    id = itemId,
                    name = itemName,
                    price = itemPrice,
                    description = itemDescription,
                    imageUrl = itemImageUrl
                )
            )
        }
    }
    cursor.close()

    return productList
}