package com.utsman.tokocot.productlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

class ProductListViewModel(
    private val repository: ProductListRepository
) : ViewModel() {

    private val productListErrorHandler = CoroutineExceptionHandler { _, throwable ->
        repository.postErrorProductList(throwable)
    }

    private val productListSafeScope: CoroutineScope
        get() = viewModelScope + productListErrorHandler

    val productList
        get() = repository.productList.asLiveData(productListSafeScope.coroutineContext)

    val productListFlow
        get() = repository.productList

    fun getProductList(page: Int) = productListSafeScope.launch {
        repository.getProductList(page)
    }

    companion object {
        fun modules(): Module {
            return module {
                viewModel { ProductListViewModel(get()) }
            }
        }
    }
}