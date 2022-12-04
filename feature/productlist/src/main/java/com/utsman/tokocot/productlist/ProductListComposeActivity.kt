package com.utsman.tokocot.productlist

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import com.utsman.tokocot.event.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductListComposeActivity : AppCompatActivity() {
    private val viewModel: ProductListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column {
                ComposeScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ComposeScreen(viewModel: ProductListViewModel) {
    val productList by viewModel.productListFlow.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        productList
            .doOnIdle {
                viewModel.getProductList(1)
            }
            .doOnSuccess {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    content = {
                        items(items = it) {
                            ProductItemScreen(product = it)
                        }
                    }
                )
            }
            .doOnLoading {
                CircularProgressIndicator()
            }
            .doOnFailure {
                Text(text = it.message.orEmpty())
            }
    }
}

@Composable
fun ProductItemScreen(product: Product) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        val (image, textName, textPrice) = createRefs()

        val gh1 = createGuidelineFromTop(0.5f)

        AsyncImage(
            model = product.image,
            contentDescription = product.id,
            contentScale = ContentScale.Crop,
            modifier = Modifier.constrainAs(image) {
                bottom.linkTo(gh1)
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
        )

        Text(text = product.name, modifier = Modifier.constrainAs(textName) {
            top.linkTo(image.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })

        Text(text = product.price.toString(), modifier = Modifier.constrainAs(textPrice) {
            top.linkTo(textName.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })
    }
}