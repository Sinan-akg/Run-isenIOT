package com.example.runisenapp

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    private var map: GoogleMap? = null

 /*   private lateinit var map: GoogleMap
    private val LOCATION_PERMISSION_REQUEST = 1

    private fun getLocationAccess(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            map.isMyLocationEnabled = true
        }
        else
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST)
    }

    override fun onRequestPermissionResult(requestCode: Int, permissions: ArrayList<String>, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                map.isMyLocationEnabled = true
            } else {
                Toast.makeText(
                    this,
                    "L'utilisateur n'a pas activé la localisation",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

*/



    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        val toulon = LatLng(43.120797293899564, 5.9391092981435545)
       // googleMap.isMyLocationEnabled = true
        googleMap.addMarker(MarkerOptions().position(toulon).title("ISEN Toulon").icon(
            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
        ))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(toulon, 17f), 1000, null)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}