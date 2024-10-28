package com.cyclequest.core.database.sync

enum class ConflictStrategy {
    SERVER_WINS,
    CLIENT_WINS,
    LAST_MODIFIED_WINS
}
