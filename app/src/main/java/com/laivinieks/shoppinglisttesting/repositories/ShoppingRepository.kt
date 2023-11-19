package com.laivinieks.shoppinglisttesting.repositories

import androidx.lifecycle.LiveData
import com.laivinieks.shoppinglisttesting.data.local.ShoppingItem
import com.laivinieks.shoppinglisttesting.data.remote.responses.ImageResponse
import com.laivinieks.shoppinglisttesting.other.Resource
import retrofit2.Response

interface ShoppingRepository {
    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)
    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)
    fun observeAllShoppingItems():LiveData<List<ShoppingItem>>
    fun observeTotalPrice():LiveData<Float>

    suspend fun searchForImage(imageQuery:String):Resource<ImageResponse>
}