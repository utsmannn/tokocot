package com.utsman.tokocot.utils

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<B: ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: B
    abstract fun inflateBinding(): B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflateBinding()
        setContentView(binding.root)
        onCreateBinding(savedInstanceState)
    }

    abstract fun onCreateBinding(savedInstanceState: Bundle?)
}