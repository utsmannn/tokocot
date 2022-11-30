package com.utsman.tokocot.productlist

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
            .doOnSuccess {

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    content = {
                        items(items = it) {
                            Text(text = it.name)
                        }
                    }
                )

            }
    }
}