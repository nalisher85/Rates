package com.example.rates.di

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.rates.currency_rates.data.cache.CurrencyRateEntity
import com.example.rates.currency_rates.data.cache.FavoriteDao

@Database(entities = [CurrencyRateEntity::class], version = 3)
abstract class DataBase: RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}