package com.cyclequest.core.database

data class DatabaseConfig(
    val name: String,
    val version: Int,
    val enableWAL: Boolean,
    val enableForeignKeys: Boolean
)

