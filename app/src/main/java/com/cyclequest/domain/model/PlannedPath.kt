package com.cyclequest.domain.model

data class PlannedPath(
//    val routeId: Long,
    val userId: String,
//    val startName: String,
//    val startLatitude: Double,
//    val startLongitude: Double,
//    val endName: String,
//    val endLatitude: Double,
//    val endLongitude: Double,
    val distance: Float,
    val duration: Long,
    val routeData: List<List<Double>>,
    val createdAt: Long,
//    val isFavorite: Boolean
)