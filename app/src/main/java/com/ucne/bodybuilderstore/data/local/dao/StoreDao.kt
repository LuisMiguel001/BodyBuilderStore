package com.ucne.bodybuilderstore.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.ucne.bodybuilderstore.data.local.entity.StoreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreDao {
    @Upsert
    suspend fun upsert(store: StoreEntity)

    @Delete
    suspend fun delete(store: StoreEntity)

    @Query("Select * From table_store")
    fun getAll(): Flow<List<StoreEntity>>

    @Query("SELECT * FROM table_store WHERE id = :id LIMIT 1")
    fun getById(id: Int): Flow<StoreEntity?>
}