package com.cyclequest.data.local.dao

import com.cyclequest.data.local.TestDatabase
import com.cyclequest.data.local.entity.PlannedRouteEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PlannedRouteDaoTest : TestDatabase() {
    private val plannedRouteDao by lazy { getDatabase().plannedRouteDao() }

    @Test
    fun insertAndGetPlannedRoute() = runBlocking {
        println("=== 测试插入和获取计划路线 ===")
        val route = createTestPlannedRoute("route1")
        println("创建测试路线: $route")
        plannedRouteDao.insertPlannedRoute(route)
        println("路线已插入数据库")
        
        val loadedRoute = plannedRouteDao.getPlannedRouteById("route1")
        println("从数据库加载的路线: $loadedRoute")
        assertEquals(route, loadedRoute)
        println("=== 测试完成 ===")
    }

    @Test
    fun getUserFavoriteRoutes() = runBlocking {
        println("=== 测试获取用户收藏路线 ===")
        val routes = listOf(
            createTestPlannedRoute("route1", "user1", isFavorite = true),
            createTestPlannedRoute("route2", "user1", isFavorite = false),
            createTestPlannedRoute("route3", "user1", isFavorite = true)
        )
        println("创建测试路线列表: ${routes.joinToString()}")
        
        routes.forEach { plannedRouteDao.insertPlannedRoute(it) }
        println("所有路线已插入数据库")
        
        val favoriteRoutes = plannedRouteDao.getUserFavoriteRoutesFlow("user1").first()
        println("获取user1的收藏路线: ${favoriteRoutes.joinToString()}")
        assertEquals(2, favoriteRoutes.size)
        println("=== 测试完成 ===")
    }

    private fun createTestPlannedRoute(
        routeId: String,
        userId: String = "test_user",
        isFavorite: Boolean = false
    ) = PlannedRouteEntity(
        routeId = routeId,
        userId = userId,
        startName = "起点",
        startLatitude = 39.9,
        startLongitude = 116.4,
        endName = "终点",
        endLatitude = 40.0,
        endLongitude = 116.5,
        distance = 10000f,
        duration = 3600,
        routeData = "test_route_data",
        createdAt = System.currentTimeMillis(),
        isFavorite = isFavorite
    )
} 