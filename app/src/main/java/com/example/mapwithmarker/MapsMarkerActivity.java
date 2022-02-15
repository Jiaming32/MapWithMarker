// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.example.mapwithmarker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
// [START maps_marker_on_map_ready]
public class MapsMarkerActivity extends FragmentActivity
        implements OnMapReadyCallback {
    private Location mLastLocation;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    // [START_EXCLUDE]
    // [START maps_marker_get_map_async]
    private void setUpMap() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        return;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setUpMap();
    }
    // [END maps_marker_get_map_async]
    // [END_EXCLUDE]

    // [START_EXCLUDE silent]
    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    // [END_EXCLUDE]
    // [START maps_marker_on_map_ready_add_marker]
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng aalto = new LatLng(60.185364, 24.825476);
        googleMap.addMarker(new MarkerOptions()
            .position(aalto)
            .title("Marker in Aalto"));
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(aalto));

        ImageButton btn_camera = (ImageButton) findViewById(R.id.btn_camera);
        ImageButton btn_zoomin = (ImageButton) findViewById(R.id.btn_zoomin);
        ImageButton btn_zoomout = (ImageButton) findViewById(R.id.btn_zoomout);
        ImageButton btn_help = (ImageButton) findViewById(R.id.btn_help);

        btn_zoomin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleMap.moveCamera(CameraUpdateFactory.zoomTo(googleMap.getCameraPosition().zoom + 1));
            }
        });

        btn_zoomout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleMap.moveCamera(CameraUpdateFactory.zoomTo(googleMap.getCameraPosition().zoom - 1));
            }
        });


    }
    // [END maps_marker_on_map_ready_add_marker]

}
// [END maps_marker_on_map_ready]
