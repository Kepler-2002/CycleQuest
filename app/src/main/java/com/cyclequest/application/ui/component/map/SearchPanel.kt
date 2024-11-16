package com.cyclequest.application.ui.component.map

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.amap.api.services.geocoder.GeocodeQuery
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener
import com.amap.api.services.geocoder.RegeocodeResult

@Composable
fun SearchPanel(
    modifier: Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf(0.0) }
    var longitude by remember { mutableStateOf(0.0) }

    var isDestinationAvail by remember { mutableStateOf(0) }

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
                Log.i("Geocode", "Latitude: $latitude, Longitude: $longitude")

                isDestinationAvail = 1 // switch state for pull down menu
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
        }
        // 搜索栏下拉，显示位置，右侧按钮开始导航
    }
}