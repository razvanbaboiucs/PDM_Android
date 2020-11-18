package com.example.coffeeloby_basicactivity.shop.components.edit

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.coffeeloby_basicactivity.core.TAG
import com.example.coffeeloby_basicactivity.core.Result
import com.example.coffeeloby_basicactivity.shop.data.CoffeeItem
import com.example.coffeeloby_basicactivity.shop.data.CoffeeItemRepository
import com.example.coffeeloby_basicactivity.shop.data.local.CoffeeDatabase
import kotlinx.coroutines.launch

class CoffeeItemEditViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableFetching = MutableLiveData<Boolean>().apply { value = false }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val fetching: LiveData<Boolean> = mutableFetching
    val fetchingError: LiveData<Exception> = mutableException
    val completed: LiveData<Boolean> = mutableCompleted

    val itemRepository: CoffeeItemRepository

    init {
        val itemDao = CoffeeDatabase.getDatabase(application, viewModelScope).itemDao()
        itemRepository = CoffeeItemRepository(itemDao)
    }

    fun getItemById(itemId: String): LiveData<CoffeeItem> {
        Log.v(TAG, "getItemById...")
        return itemRepository.getById(itemId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveOrUpdateItem(item: CoffeeItem) {
        viewModelScope.launch {
            Log.v(TAG, "saveOrUpdateItem...");
            mutableFetching.value = true
            mutableException.value = null
            val result: Result<CoffeeItem>
            Log.v(TAG, item.toString())
            if (item._id.isNotEmpty()) {
                result = itemRepository.update(item)
            } else {
                result = itemRepository.save(item)
            }
            when(result) {
                is Result.Success -> {
                    Log.d(TAG, "saveOrUpdateItem succeeded");
                }
                is Result.Error -> {
                    Log.w(TAG, "saveOrUpdateItem failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableCompleted.value = true
            mutableFetching.value = false
        }
    }

    fun deleteItem(item:CoffeeItem){
        viewModelScope.launch {
            Log.v(TAG, "deleting item ...")
            mutableFetching.value = true
            mutableException.value = null
            Log.v(TAG, item.toString())
            val result: Result<Boolean>

            result = itemRepository.delete(item)

            when(result) {
                is Result.Success -> {
                    Log.d(TAG, "deleteItem succeeded");
                }
                is Result.Error -> {
                    Log.w(TAG, "deleteItem failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableCompleted.value = true
            mutableFetching.value = false
        }
    }

}
