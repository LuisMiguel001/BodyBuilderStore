package com.ucne.bodybuilderstore.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ucne.bodybuilderstore.data.local.entity.CartEntity
import com.ucne.bodybuilderstore.data.local.entity.Location
import com.ucne.bodybuilderstore.data.local.entity.PaymentMethod
import com.ucne.bodybuilderstore.data.local.entity.StoreEntity
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
    @Query("DELETE FROM table_location")
    suspend fun clearLocation()
    @Query("DELETE FROM table_payment_method")
    suspend fun clearPay()

    @Transaction
    suspend fun clearAll() {
        clearCart()
        clearLocation()
        clearPay()
    }

    @Query("SELECT * FROM table_cart WHERE nombre = :nombre LIMIT 1")
    suspend fun getCartItemByName(nombre: String): CartEntity?

    @Query("UPDATE table_cart SET cantidad = :newQuantity WHERE id = :itemId")
    suspend fun updateCartItemQuantity(itemId: Int, newQuantity: Int)

    /*Location*/
    @Query("SELECT * FROM table_cart WHERE id = :itemId")
    suspend fun getItemById(itemId: Int): CartEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateLocation(location: Location)

    @Query("SELECT * FROM table_location WHERE id = :locationId")
    fun getLocationById(locationId: Int): Flow<Location?>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdatePaymentMethod(paymentMethod: PaymentMethod)

    @Query("SELECT * FROM table_payment_method WHERE id = :paymentMethodId")
    fun getPaymentMethodById(paymentMethodId: Int): Flow<PaymentMethod?>
}
