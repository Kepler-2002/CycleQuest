package com.cyclequest.domain.repository

import com.cyclequest.data.local.dao.RideRecordDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RideRecordRepository @Inject constructor(
    private val rideRecordDao: RideRecordDao
) {
    // 获取所有骑行记录
//    fun getAllRides(): Flow<List<Ride>> {
//        return rideDao.getAllRidesFlow()
//            .map { rides -> rides.map { rideMapper.toDomain(it) } }
//    }

    // 获取单个骑行记录
//    suspend fun getRideById(id: String): Ride? {
//        return rideDao.getRideById(id)?.let { rideMapper.toDomain(it) }
//    }

    // 添加新的骑行记录
//    suspend fun addRide(ride: Ride) {
//        rideDao.insertRide(RideEntity.fromDomain(ride))
//    }

    // 更新骑行记录
//    suspend fun updateRide(ride: Ride) {
//        rideDao.updateRide(RideEntity.fromDomain(ride))
//    }

    // 删除骑行记录
//    suspend fun deleteRide(id: String) {
//        rideDao.deleteRide(id)
//    }

    // 获取用户的总骑行距离
    suspend fun getUserTotalDistance(userId: String): Float {
        return rideRecordDao.getRidesDistanceByUserId(userId)
            .map { distances -> distances.sum() } // Summing up the distances
            .first() // Collecting the first emitted value
    } // 返回float类型的 该用户各骑行route的距离之和
}