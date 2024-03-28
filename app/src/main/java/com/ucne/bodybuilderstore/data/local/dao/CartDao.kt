package com.ucne.bodybuilderstore.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ucne.bodybuilderstore.data.local.entity.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(item: CartEntity)

    @Query("SELECT * FROM table_cart")
    fun getAllItems(): Flow<List<CartEntity>>

    @Query("DELETE FROM table_cart WHERE id = :itemId")
    suspend fun deleteById(itemId: Int)

    @Query("DELETE FROM table_cart")
    suspend fun clearCart()

    @Query("SELECT * FROM table_cart WHERE nombre = :nombre LIMIT 1")
    suspend fun getCartItemByName(nombre: String): CartEntity?

    @Query("UPDATE table_cart SET cantidad = :newQuantity WHERE id = :itemId")
    suspend fun updateCartItemQuantity(itemId: Int, newQuantity: Int)
}
