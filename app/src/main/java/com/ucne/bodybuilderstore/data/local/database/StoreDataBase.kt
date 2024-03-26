package com.ucne.bodybuilderstore.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ucne.bodybuilderstore.data.local.dao.CartDao
import com.ucne.bodybuilderstore.data.local.dao.StoreDao
import com.ucne.bodybuilderstore.data.local.entity.CartEntity
import com.ucne.bodybuilderstore.data.local.entity.StoreEntity

@Database(entities = [StoreEntity::class, CartEntity::class], version = 5)
abstract class StoreDataBase: RoomDatabase() {
    abstract fun storedao(): StoreDao

    abstract fun cartDao(): CartDao
}