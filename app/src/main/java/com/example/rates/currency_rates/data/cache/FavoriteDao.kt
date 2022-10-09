package com.example.rates.currency_rates.data.cache

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @MapInfo(keyColumn = "id")
    @Query("SELECT * FROM favorites")
    fun getFavoritesIntoMapFlow(): Flow<Map<String, CurrencyRateEntity>>

    @MapInfo(keyColumn = "id")
    @Query("SELECT * FROM favorites")
    suspend fun getFavoritesIntoMap(): Map<String, CurrencyRateEntity>

    @Query("SELECT * FROM favorites")
    fun getFavoritesFlow(): Flow<List<CurrencyRateEntity>>

    @Query("SELECT * FROM favorites WHERE base = :base")
    suspend fun getFavoritesByBase(base: String): List<CurrencyRateEntity>

    @Query("DELETE FROM favorites WHERE id = :id")
    suspend fun deleteFavoriteById(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(currencyRate: CurrencyRateEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorites(currencyRates: List<CurrencyRateEntity>)
}