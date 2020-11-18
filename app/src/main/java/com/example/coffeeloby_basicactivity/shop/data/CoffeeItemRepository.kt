package com.example.coffeeloby_basicactivity.shop.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.coffeeloby_basicactivity.core.TAG
import com.example.coffeeloby_basicactivity.shop.data.local.ItemDao
import com.example.coffeeloby_basicactivity.shop.data.remote.ItemApi
import com.example.coffeeloby_basicactivity.core.Result
class CoffeeItemRepository(private val itemDao : ItemDao) {

    val items = itemDao.getAll()

    suspend fun refresh(): Result<Boolean> {
        try {
            val items = ItemApi.service.find()
            for (item in items) {
                itemDao.insert(item)
            }
            return Result.Success(true)
        } catch(e: Exception) {
            return Result.Error(e)
        }
    }

    fun getById(itemId: String): LiveData<CoffeeItem> {
        return itemDao.getById(itemId)
    }

    suspend fun save(item: CoffeeItem): Result<CoffeeItem> {
        try {
            Log.v(TAG, "creating item..." + item.title)
            val createdItem = ItemApi.service.create(item)
            Log.v(TAG, "created item..." + createdItem._id)
            itemDao.insert(createdItem)
            return Result.Success(createdItem)
        } catch(e: Exception) {
            return Result.Error(e)
        }
    }

    suspend fun update(item: CoffeeItem): Result<CoffeeItem> {
        try {
            val updatedItem = ItemApi.service.update(item._id, item)
            itemDao.update(updatedItem)
            return Result.Success(updatedItem)
        } catch(e: Exception) {
            return Result.Error(e)
        }
    }

    suspend fun delete(item: CoffeeItem):Result<Boolean>{
        try{
            Log.v(TAG, "deleting item ... "+item.title)
            val result = ItemApi.service.delete(item._id)
            Log.v(TAG, "dc nu merge delete")
            itemDao.delete(item._id)
            return Result.Success(true)
        }catch(e: Exception) {
            return Result.Error(e)
        }
    }
}