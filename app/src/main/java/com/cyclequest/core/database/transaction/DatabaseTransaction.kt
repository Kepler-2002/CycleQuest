package com.cyclequest.core.database.transaction

// core/database/transaction/DatabaseTransaction.kt
interface DatabaseTransaction {
    suspend fun <T> withTransaction(block: suspend () -> T): T
}