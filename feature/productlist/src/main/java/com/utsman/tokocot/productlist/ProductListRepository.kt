package com.utsman.tokocot.productlist

import com.utsman.tokocot.event.*
import com.utsman.tokocot.network.ProductListResponse
import com.utsman.tokocot.network.WebServices
import kotlinx.coroutines.flow.*
import org.koin.core.module.Module
import org.koin.dsl.module

interface ProductListRepository {
    val productList: StateFlow<StateEvent<List<Product>>>
    suspend fun getProductList(page: Int)
    fun postErrorProductList(throwable: Throwable)

    private class Impl(
        private val webServices: WebServices
    ) : ProductListRepository {

        private val _productList: MutableStateFlow<StateEvent<List<Product>>>
            get() = MutableStateFlow(StateEvent.Idle())

        override val productList: StateFlow<StateEvent<List<Product>>>
            get() = _productList

        override suspend fun getProductList(page: Int) {
            _productList.value = StateEvent.Loading()
            webServices.getProduct(page = page).reduce().map {
                Mapper.mapResponseToProductList(it)
            }.also { result ->
                _productList.value = result
            }
        }

        override fun postErrorProductList(throwable: Throwable) {
            if (productList.value is StateEvent.Loading) {
                _productList.value = StateEvent.Failure(throwable)
            }
        }
    }

    private object Mapper {
        fun mapResponseToProductList(response: ProductListResponse?): List<Product> {
            val mapper: (ProductListResponse.Data.Data?) -> Product = { itemResponse ->
                Product(
                    id = itemResponse?.id.orEmpty(),
                    name = itemResponse?.name.orEmpty(),
                    price = itemResponse?.price ?: 0L,
                    image = itemResponse?.imageUrl.orEmpty()
                )
            }

            return response?.data?.data?.map(mapper).orEmpty()
        }
    }

    companion object {
        fun modules(): Module {
            return module {
                single<ProductListRepository> { Impl(get()) }
            }
        }
    }
}