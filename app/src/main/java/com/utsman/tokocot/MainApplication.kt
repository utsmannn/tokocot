package com.utsman.tokocot

import android.app.Application
import com.utsman.tokocot.network.WebServices
import com.utsman.tokocot.productlist.ProductListRepository
import com.utsman.tokocot.productlist.ProductListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(
                WebServices.modules(),
                ProductListRepository.modules(),
                ProductListViewModel.modules()
            )
        }
    }
}