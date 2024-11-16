package com.cyclequest.core.base

import com.cyclequest.core.database.sync.SyncStatus


// core/database/base/BaseEntity.kt
interface BaseEntity {
//    val id: String
    // val syncStatus: SyncStatus
    val createdAt: Long
    val updatedAt: Long
}
