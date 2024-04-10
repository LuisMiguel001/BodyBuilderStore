package com.ucne.bodybuilderstore.data.repository

import com.ucne.bodybuilderstore.data.local.dao.StoreDao
import com.ucne.bodybuilderstore.data.local.entity.StoreEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StoreRepository @Inject constructor(
    private val storeDao: StoreDao
) {
    suspend fun upsert(store: StoreEntity) {
        storeDao.upsert(store)
    }

    suspend fun delete(store: StoreEntity) {
        storeDao.delete(store)
    }

    fun getProductoById(id: Int): Flow<StoreEntity?> {
        return storeDao.getById(id)
    }

    fun getProductosByType(type: String): Flow<List<StoreEntity>> {
        return storeDao.getByType(type)
    }

    suspend fun markAsFavorite(id: Int) {
        storeDao.markAsFavorite(id)
    }

    suspend fun removeFromFavorites(id: Int) {
        storeDao.removeFromFavorites(id)
    }

    fun getFavorites(): Flow<List<StoreEntity>> {
        return storeDao.getFavorites()
    }
}
