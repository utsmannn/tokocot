package com.utsman.tokocot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.utsman.tokocot.productlist.ProductListActivity
import com.utsman.tokocot.productlist.ProductListComposeActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startActivity(Intent(this, ProductListComposeActivity::class.java))
    }
}