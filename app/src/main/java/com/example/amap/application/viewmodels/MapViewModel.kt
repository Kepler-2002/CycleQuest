@HiltViewModel
class MapViewModel @Inject constructor(
    private val locationService: LocationService
) : ViewModel() {

    fun startLocationTracking() {
        locationService.startLocation()
    }

    fun stopLocationTracking() {
        locationService.stopLocation()
    }
}