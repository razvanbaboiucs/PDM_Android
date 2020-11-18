package com.example.coffeeloby_basicactivity.shop.components.list

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.coffeeloby_basicactivity.shop.data.CoffeeItem
import com.example.coffeeloby_basicactivity.shop.data.CoffeeItemRepository
import kotlinx.coroutines.launch
import com.example.coffeeloby_basicactivity.core.TAG
import com.example.coffeeloby_basicactivity.shop.data.local.CoffeeDatabase
import com.example.coffeeloby_basicactivity.core.Result
import com.example.coffeeloby_basicactivity.shop.data.remote.WebSocketsDataSource
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class CoffeeItemListViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val items: LiveData<List<CoffeeItem>>
    val loading: LiveData<Boolean> = mutableLoading
    val loadingError: LiveData<Exception> = mutableException

    val itemRepository: CoffeeItemRepository

    init {
        val itemDao = CoffeeDatabase.getDatabase(application, viewModelScope).itemDao()
        itemRepository = CoffeeItemRepository(itemDao)
        items = itemRepository.items
        Log.v(TAG, "init coffee list view model")
        val request = Request.Builder().url("ws://192.168.0.104:3000").build()
        OkHttpClient().newWebSocket(
            request,
            WebSocketsDataSource.MyWebSocketListener(application.applicationContext)
        )
        CoroutineScope(Dispatchers.Main).launch {
            Log.v(TAG, "launch event collector")
            collectEvents() }
    }

    fun refresh() {
        viewModelScope.launch {
            Log.v(TAG, "refresh...");
            mutableLoading.value = true
            mutableException.value = null
            when (val result = itemRepository.refresh()) {
                is Result.Success -> {
                    Log.d(TAG, "refresh succeeded");
                }
                is Result.Error -> {
                    Log.w(TAG, "refresh failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableLoading.value = false
        }
    }

    private suspend fun collectEvents() {
        while (true) {
            Log.v("ws", "receiving")
            val res = JSONObject(WebSocketsDataSource.eventChannel.receive())
            val coffee = Gson().fromJson(res.getJSONObject("payload").toString(), CoffeeItem::class.java)
            Log.d("ws", "received $coffee")
            refresh()
        }
    }

}