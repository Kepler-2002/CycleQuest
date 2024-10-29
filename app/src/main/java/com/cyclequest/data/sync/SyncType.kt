import com.cyclequest.core.database.sync.ConflictStrategy
import com.cyclequest.core.database.sync.RetryPolicy
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

sealed class SyncType(
    val syncInterval: Duration,
    val retryPolicy: RetryPolicy,
    val conflictStrategy: ConflictStrategy
) {
    object User : SyncType(
        syncInterval = 1.hours,
        retryPolicy = RetryPolicy.Exponential(
            initialDelay = 5.seconds,
            maxAttempts = 3
        ),
        conflictStrategy = ConflictStrategy.SERVER_WINS
    )

    object Profile : SyncType(
        syncInterval = 30.minutes,
        retryPolicy = RetryPolicy.Linear(
            initialDelay = 10.seconds,
            maxAttempts = 3
        ),
        conflictStrategy = ConflictStrategy.CLIENT_WINS
    )

    // 其他数据类型...
}