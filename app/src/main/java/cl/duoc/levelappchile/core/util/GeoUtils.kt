package cl.duoc.levelappchile.core.util

data class LatLng(val lat: Double, val lon: Double)

/** Bounding-box simple de Chile continental (aprox). */
fun isInChile(lat: Double, lon: Double): Boolean {
    val latMin = -56.0; val latMax = -17.5
    val lonMin = -75.0; val lonMax = -66.0
    return lat in latMin..latMax && lon in lonMin..lonMax
}