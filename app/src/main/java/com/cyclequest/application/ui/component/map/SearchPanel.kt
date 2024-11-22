package com.cyclequest.application.ui.component.map

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amap.api.maps2d.model.LatLng
import com.amap.api.services.geocoder.GeocodeQuery
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener
import com.amap.api.services.geocoder.RegeocodeResult
import com.cyclequest.application.viewmodels.MapViewModel
import com.cyclequest.application.viewmodels.RoutingViewModel
import com.cyclequest.service.route.RouteService

@Composable
fun SearchPanel(
    modifier: Modifier,
    routeInfo: RouteService.RouteInfo?,
    mapViewModel: MapViewModel = hiltViewModel(),
    routingViewModel: RoutingViewModel = hiltViewModel(),
) {
    var searchQuery by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf(0.0) }
    var longitude by remember { mutableStateOf(0.0) }

    val currentLocation by mapViewModel.currentLocation.collectAsState()

    var isDestinationAvail by remember { mutableStateOf(false) }
    val isNavigationStarted by routingViewModel.isNavigationStarted.collectAsState()

    // Initialize GeocodeSearch
    val context = LocalContext.current
    val geocodeSearch = remember { GeocodeSearch(context) }

    // Set the listener for geocoding
    geocodeSearch.setOnGeocodeSearchListener(object : OnGeocodeSearchListener {
        override fun onGeocodeSearched(result: GeocodeResult?, rCode: Int) {
            if (rCode == 1000 && result != null && result.geocodeAddressList.isNotEmpty()) {
                val location = result.geocodeAddressList[0].latLonPoint
                latitude = location.latitude
                longitude = location.longitude
                Log.i("Destination", "Latitude: $latitude, Longitude: $longitude")

                // 加载路线数据
                mapViewModel.getCurrentLocation()?.let {
                    routingViewModel.searchRoute(
                        LatLng(it.latitude, it.longitude),
//                        LatLng(22.3383,114.1720),
//                        LatLng(22.3361,114.1750)
                        LatLng(latitude, longitude)
                    )
                    Log.i("CurrentLoc", "Latitude: ${it.latitude}, Longitude: ${it.longitude}")
                }
                isDestinationAvail = true // switch state for pull down menu

            } else {
                Log.e("Geocode", "Geocode search failed with code: $rCode")
            }
        }

        override fun onRegeocodeSearched(result: RegeocodeResult?, rCode: Int) {
            // Handle reverse geocoding results if needed
        }
    })

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 80.dp)
    ) {
        Column(modifier = Modifier.padding(2.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search...") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                )
                IconButton(
                    onClick = {
                        // Trigger geocoding when the search button is clicked
                        val query = GeocodeQuery(searchQuery, "香港") // Replace with the appropriate city if needed
                        geocodeSearch.getFromLocationNameAsyn(query)
                    },
                    modifier = Modifier
                        .size(32.dp)
                        .padding(top = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                    )
                }
            }
            AnimatedVisibility(
                visible = isDestinationAvail,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    routeInfo?.let { info ->
                        Text("${info.totalDistance / 1000.0}公里  ${info.totalDuration / 60}分钟")
                    }
                    IconButton(
                        onClick = {
                            // start navigation
                            routingViewModel.NaviFlag_Set()

                            // save path to DB
                            routingViewModel.saveRoute()

                            // Last: change state flag
                            isDestinationAvail = false
                        },
                        modifier = Modifier
                            .size(32.dp)
                            .padding(top = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Go",
                        )
                    }
                }
            }
        }
        // 搜索栏下拉，显示位置&路线（缩放居中），右侧按钮开始导航
    }
}