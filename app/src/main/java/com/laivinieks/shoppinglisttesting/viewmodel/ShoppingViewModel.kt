package com.laivinieks.shoppinglisttesting.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laivinieks.shoppinglisttesting.data.local.ShoppingItem
import com.laivinieks.shoppinglisttesting.data.local.ShoppingItemDb
import com.laivinieks.shoppinglisttesting.data.remote.responses.ImageResponse
import com.laivinieks.shoppinglisttesting.other.Constants
import com.laivinieks.shoppinglisttesting.other.Event
import com.laivinieks.shoppinglisttesting.other.Resource
import com.laivinieks.shoppinglisttesting.repositories.ShoppingRepository
import kotlinx.coroutines.launch
import javax.inject.Inject


// the repository interface was  taken as parameter so that we can use viewmodel both in the  test and in the project
// while that we don't need an additional viewmodel
class ShoppingViewModel @Inject constructor(private val repository: ShoppingRepository) :
    ViewModel() {


    val shoppingItems: LiveData<List<ShoppingItem>> = repository.observeAllShoppingItems()

    val totalPrice: LiveData<Float> = repository.observeTotalPrice()

    private val _images = MutableLiveData<Event<Resource<ImageResponse>>>()
    val images: LiveData<Event<Resource<ImageResponse>>> = _images

    private val _currentImageUrl = MutableLiveData<String>()
    val currentImageUrl: LiveData<String> = _currentImageUrl

    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemStatus: LiveData<Event<Resource<ShoppingItem>>> =
        _insertShoppingItemStatus

    fun setCurrentImageUrl(url: String) {
        _currentImageUrl.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItemToDb(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(name: String, amountString: String, price: String) {

        if (name.isEmpty() || amountString.isEmpty() || price.isEmpty()) {
            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.error(
                        "The fields must not be empty",
                        null
                    )
                )
            )
            return
        }
        if (name.length > Constants.MAX_NAME_LENGTH) {
            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.error(
                        "The name of the item must not exceed ${Constants.MAX_NAME_LENGTH} characters",
                        null
                    )
                )
            )
            return
        }
        if (price.length > Constants.MAX_PRICE_LENGTH) {
            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.error(
                        "The name of the item must not exceed ${Constants.MAX_PRICE_LENGTH} characters",
                        null
                    )
                )
            )
            return
        }

        val amount = try {

            amountString.toInt()
        } catch (e: Exception) {
            _insertShoppingItemStatus.postValue(Event(Resource.error("Please enter  a valid amount", null)))
            return
        }

        val shoppingItem = ShoppingItem(name, amount, price.toFloat(), _currentImageUrl.value ?: "")
        insertShoppingItemToDb(shoppingItem)
        setCurrentImageUrl("")

        _insertShoppingItemStatus.postValue(Event(Resource.success(shoppingItem)))
    }

    fun searchForImage(imageQuery: String) {
        if (imageQuery.isEmpty()) {
            return
        }

        /**----- why we use value instead of postValue() ?
         * if we set the value than the changes will always notify all of observers of that live data
         * when we use postValue if we use postValue several times in a very short time frame then only the last time we will be notify our observers.
         *
         * we did it for testing
         * */
        _images.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.searchForImage(imageQuery)

            _images.value = Event(response)
        }

    }
}