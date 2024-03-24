package com.ucne.bodybuilderstore.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ucne.bodybuilderstore.data.local.dao.StoreDao
import com.ucne.bodybuilderstore.data.local.entity.StoreEntity

@Database(entities = [StoreEntity::class], version = 2)
abstract class StoreDataBase: RoomDatabase() {
    abstract fun storedao(): StoreDao
}