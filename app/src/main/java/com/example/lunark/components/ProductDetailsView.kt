package com.example.lunark.components

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.lunark.AppUtil
import com.example.lunark.model.ProductModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType

@Composable
fun ProductDetailsView(modifier: Modifier = Modifier,productId : String){

    var product by remember { mutableStateOf(ProductModel()) }
    var context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        Firebase.firestore.collection("data").document("stock")
            .collection("products")
            .document(productId).get()
            .addOnCompleteListener{
                if(it.isSuccessful){
                    var result = it.result.toObject(ProductModel::class.java)
                    if (result!=null){
                        product = result
                    }
                }
            }
    }
    Column (modifier = modifier
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(rememberScrollState())){
        Text(text = product.title, fontWeight = FontWeight.Bold, fontSize = 20.sp,
            modifier = Modifier.padding(8.dp))
        Spacer(modifier = Modifier.height(8.dp))

        Column{
            val pagerState = rememberPagerState(0){
                product.images.size
            }
            HorizontalPager(
                state = pagerState,
                pageSpacing = 24.dp) {
                AsyncImage(model = product.images.get(it), contentDescription = "Product Images",
                    modifier = Modifier
                        .height(220.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                )

            }
            Spacer(modifier = Modifier.height(16.dp))

            DotsIndicator(
                dotCount = product.images.size,
                type = ShiftIndicatorType(
                    DotGraphic (
                    color = MaterialTheme.colorScheme.primary,
                    size = 6.dp
                )
                ),
                pagerState = pagerState
            )
        }

        Spacer(modifier=Modifier.width(8.dp))


        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically){
            Text(text = "$" + product.price, fontSize = 16.sp,
                style = TextStyle(textDecoration = TextDecoration.LineThrough)
            )
            Spacer(modifier=Modifier.width(8.dp))
            Text(text = "$" + product.actualPrice, fontSize = 20.sp, fontWeight = FontWeight.Bold,

                )
            IconButton(onClick = {

            }){
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Add to Favourites"
                )
            }
        }

        Spacer(modifier=Modifier.width(8.dp))


        Button(onClick = {
            AppUtil.addToCart(context , productId)

        },
            modifier = Modifier.fillMaxWidth().height(50.dp)) {
            Text(text = "Add to Cart", fontSize = 16.sp)
        }
        Spacer(modifier=Modifier.width(8.dp))
        Text(text = "Product description : ",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp)

        Spacer(modifier=Modifier.width(8.dp))
        Text(text = product.description, fontSize = 16.sp)
        Spacer(modifier=Modifier.width(16.dp))

        if(product
            .otherDetails
            .isNotEmpty())
        Text(text = "Other Product Details : ",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp)

        Spacer(modifier=Modifier.width(8.dp))

        product.otherDetails.forEach{(key,value)->
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)){
                Text(text = "$key : ", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text(text = value, fontSize = 16.sp)

            }

        }


    }

}