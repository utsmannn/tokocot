package com.utsman.tokocot.productlist

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.utsman.tokocot.utils.BaseViewModel
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

    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        repository.postErrorProductList(throwable)
    }

    private val safeScope: CoroutineScope
        get() = viewModelScope + errorHandler

    val productList
        get() = repository.productList.asLiveData(safeScope.coroutineContext)

    val productListFlow
        get() = repository.productList

    fun getProductList(page: Int) = safeScope.launch {
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