@Composable
fun Polygon(
    points: List<LatLng>,
    strokeWidth: Float = 2f,
    strokeColor: Color = Color.Black,
    fillColor: Color = Color(0x33FF0000),
    modifier: Modifier = Modifier
) {
    val map = LocalAMap.current
    
    DisposableEffect(points) {
        val polygon = PolygonOptions().apply {
            addAll(points)
            strokeWidth(strokeWidth)
            strokeColor(strokeColor.toArgb())
            fillColor(fillColor.toArgb())
        }.let { map.addPolygon(it) }
        
        onDispose {
            polygon.remove()
        }
    }
} 