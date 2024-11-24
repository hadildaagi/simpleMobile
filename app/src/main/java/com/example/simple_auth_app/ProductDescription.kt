package com.example.simple_auth_app

import android.content.Context
import android.os.Bundle
import android.provider.BaseColumns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simple_auth_app.data.Product
import com.example.simple_auth_app.dbHandler.FeedReaderContract
import com.example.simple_auth_app.dbHandler.FeedReaderDbHelper
import com.example.simple_auth_app.ui.theme.Simple_auth_appTheme

class ProductDescription : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ProdId = intent.getLongExtra("ProdId",0)

        enableEdgeToEdge()
        setContent {
            Simple_auth_appTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SingleProd(
                        id = ProdId,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun SingleProd(id: Long , modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val productDetail by remember { _GetOneProdsFromDB(id, context) }
    if (productDetail !== null){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(16.dp))

            Text(
                text = productDetail!!.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.padding(16.dp))

            val imageId = context.resources.getIdentifier(productDetail!!.imageUrl, "drawable", context.packageName)
            Image(
                painter = painterResource(id = imageId),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.padding(16.dp))

            Text(
                text = "$${productDetail!!.price}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.padding(16.dp))

            Text(
                text = productDetail!!.description,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun _GetOneProdsFromDB(id: Long, context: Context): MutableState<Product?> {
    val dbHelper = FeedReaderDbHelper(context)
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
