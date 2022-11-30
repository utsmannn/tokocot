package com.utsman.tokocot.productlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.utsman.tokocot.event.*
import com.utsman.tokocot.productlist.databinding.ActivityProductListBinding
import com.utsman.tokocot.productlist.databinding.ItemProductBinding
import com.utsman.tokocot.utils.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates

class ProductListActivity : BaseActivity<ActivityProductListBinding>() {

    private val viewModel: ProductListViewModel by viewModel()
    private val adapter = ProductListAdapter()

    override fun inflateBinding(): ActivityProductListBinding {
        return ActivityProductListBinding.inflate(layoutInflater)
    }

    override fun onCreateBinding(savedInstanceState: Bundle?) {
        binding.rvProduct.layoutManager = GridLayoutManager(this, 2)
        binding.rvProduct.adapter = adapter

        viewModel.getProductList(1)
        viewModel.productList.observe(this) { state ->
            binding.progressCircular.isVisible = state is StateEvent.Loading

            state.doOnSuccess {
                adapter.items = it
            }.doOnFailure {
                // render failure
            }.doOnEmpty {
                // render empty
            }.doOnIdle {
                // render on idle
            }
        }
    }

    class ProductListAdapter : RecyclerView.Adapter<ProductListViewHolder>() {

        var items: List<Product> by Delegates.observable(emptyList()) { _, _, _ ->
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
            return ProductListViewHolder(ItemProductBinding.bind(view))
        }

        override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount(): Int {
            return items.size
        }
    }

    class ProductListViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Product) {
            binding.tvNameProduct.text = item.name
            binding.tvPriceProduct.text = item.price.toString()
        }

    }
}