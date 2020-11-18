package com.example.coffeeloby_basicactivity.shop.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName= "items")
data class CoffeeItem(
    @PrimaryKey @ColumnInfo(name = "_id") val _id: String,
    @ColumnInfo(name = "title")var title: String,
    @ColumnInfo(name="description") var description: String,
    @ColumnInfo(name = "date")var date : String,
    @ColumnInfo(name="recommended")var recommended: Boolean,
    @ColumnInfo(name="mark") var mark: Int
){
    override fun toString(): String = title
}