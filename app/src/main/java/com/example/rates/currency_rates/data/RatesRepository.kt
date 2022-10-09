package com.example.rates.currency_rates.data

import com.example.rates.currency_rates.data.cache.CurrencyRateEntity
import com.example.rates.currency_rates.data.cache.FavoriteDao
import com.example.rates.currency_rates.data.network.RatesApi
import com.example.rates.currency_rates.data.network.RatesResponseData
import com.example.rates.currency_rates.data.network.toError
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RatesRepository @Inject constructor(
    private val ratesApi: RatesApi,
    private val favoriteDao: FavoriteDao
) {

    suspend fun loadRates(baseCurrency: String, symbols: String? = null): Resource<RatesResponseData> {
        return try {
            val response = ratesApi.loadRates(baseCurrency, symbols)
            when {
                !response.isSuccessful ->  {
                    Resource.Error(response.code().toString(), response.errorBody()?.toError()?.message ?: "Unknown error")
                }
                response.isSuccessful && response.body() == null -> Resource.Error("", "Server error")
                response.body()!!.error != null -> response.body()!!.error!!.run {
                    Resource.Error(code, message)
                }
                else -> Resource.Success(response.body()!!)
            }
        } catch (e: Exception) {
            Resource.Error("", e.localizedMessage ?: "")
        }
    }

    fun getFavoritesIntoMapFlow(): Flow<Map<String, CurrencyRateEntity>> {
        return favoriteDao.getFavoritesIntoMapFlow()
    }

    suspend fun getFavoritesIntoMap(): Map<String, CurrencyRateEntity> {
        return favoriteDao.getFavoritesIntoMap()
    }

    fun getFavoritesFlow(): Flow<List<CurrencyRateEntity>> {
        return favoriteDao.getFavoritesFlow()
    }

    suspend fun getFavoritesByBase(base: String): List<CurrencyRateEntity> {
        return favoriteDao.getFavoritesByBase(base)
    }

    suspend fun removeFavorite(id: String) {
        favoriteDao.deleteFavoriteById(id)
    }

    suspend fun addFavorite(currencyRate: CurrencyRateEntity) {
        favoriteDao.insertFavorite(currencyRate)
    }

    suspend fun addFavorites(currencyRates: List<CurrencyRateEntity>) {
        favoriteDao.insertFavorites(currencyRates)
    }
}