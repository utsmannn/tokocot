package com.utsman.tokocot.productlist

import com.utsman.tokocot.event.DefaultEventFlow
import com.utsman.tokocot.event.StateEvent
import com.utsman.tokocot.event.fetch
import com.utsman.tokocot.event.map
import com.utsman.tokocot.network.ProductListResponse
import com.utsman.tokocot.network.WebServices
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import org.koin.core.module.Module
import org.koin.dsl.module

interface ProductListRepository {
    val productList: StateFlow<StateEvent<List<Product>>>
    suspend fun getProductList(page: Int)
    fun postErrorProductList(throwable: Throwable)

    private class Impl(private val webServices: WebServices) : ProductListRepository {
        private val _productList = DefaultEventFlow<List<Product>>()
        override val productList: StateFlow<StateEvent<List<Product>>>
            get() = _productList

        override suspend fun getProductList(page: Int) {
            _productList.value = StateEvent.Loading()
            webServices.getProduct(page = page).fetch()
                .map { stateResponse ->
                    stateResponse.map { Mapper.mapResponseToProductList(it) }
                }
                .collect(_productList)
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