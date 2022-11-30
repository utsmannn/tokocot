package com.utsman.tokocot.network


import com.google.gson.annotations.SerializedName

data class ProductListResponse(
    @SerializedName("status")
    var status: Boolean?,
    @SerializedName("code")
    var code: Int?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("data")
    var `data`: Data?
) {
    data class Data(
        @SerializedName("count")
        var count: Int?,
        @SerializedName("count_per_page")
        var countPerPage: Int?,
        @SerializedName("current_page")
        var currentPage: Int?,
        @SerializedName("data")
        var `data`: List<Data?>?
    ) {
        data class Data(
            @SerializedName("id")
            var id: String?,
            @SerializedName("name")
            var name: String?,
            @SerializedName("seller")
            var seller: Seller?,
            @SerializedName("stock")
            var stock: Int?,
            @SerializedName("category")
            var category: Category?,
            @SerializedName("price")
            var price: Long?,
            @SerializedName("image_url")
            var imageUrl: String?,
            @SerializedName("description")
            var description: String?,
            @SerializedName("sold_count")
            var soldCount: Int?,
            @SerializedName("popularity")
            var popularity: Double?
        ) {
            data class Seller(
                @SerializedName("id")
                var id: String?,
                @SerializedName("name")
                var name: String?,
                @SerializedName("city")
                var city: String?
            )

            data class Category(
                @SerializedName("id")
                var id: String?,
                @SerializedName("name")
                var name: String?,
                @SerializedName("image_cover")
                var imageCover: String?,
                @SerializedName("image_icon")
                var imageIcon: String?
            )
        }
    }
}