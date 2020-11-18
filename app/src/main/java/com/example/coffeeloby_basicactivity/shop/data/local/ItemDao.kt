package com.example.coffeeloby_basicactivity.shop.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.coffeeloby_basicactivity.shop.data.CoffeeItem

@Dao
interface ItemDao {
    @Query("SELECT * from items ORDER BY title ASC")
    fun getAll(): LiveData<List<CoffeeItem>>

    @Query("SELECT * FROM items WHERE _id=:id ")
    fun getById(id: String): LiveData<CoffeeItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CoffeeItem)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: CoffeeItem)

    @Query("DELETE FROM items")
    suspend fun deleteAll()

    @Query("DELETE FROM items WHERE _id=:id")
    suspend fun delete(id:String)
}