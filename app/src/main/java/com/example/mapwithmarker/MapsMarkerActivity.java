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

import static android.media.ExifInterface.TAG_ARTIST;
import static android.media.ExifInterface.TAG_GPS_LATITUDE;
import static android.media.ExifInterface.TAG_GPS_LATITUDE_REF;
import static android.media.ExifInterface.TAG_GPS_LONGITUDE_REF;
import static android.media.ExifInterface.TAG_IMAGE_DESCRIPTION;
import static android.media.ExifInterface.TAG_USER_COMMENT;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
// [START maps_marker_on_map_ready]
public class MapsMarkerActivity extends FragmentActivity
        implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    private Location mLastLocation;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int CAMERA_PERMISSION_CODE = 2;
    private static final int CAMERA = 3;
    private File tmpFile = null;
    private Uri tmpFileURI;
    ImageButton btn_camera;
    ImageButton btn_zoomout;
    ImageButton btn_zoomin;
    ImageView imageView;
    Marker newMark;
    ArrayList<Uri> uriList = new ArrayList<Uri>();
    ArrayList<Mark> markList = new ArrayList<>();
    Uri testUri = null;
    // [START_EXCLUDE]
    // [START maps_marker_get_map_async]

    ContentValues cv;


    private void setUpMap() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
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

        btn_camera = (ImageButton) findViewById(R.id.btn_camera);
        btn_zoomout = (ImageButton) findViewById(R.id.btn_zoomout);
        btn_zoomin = (ImageButton) findViewById(R.id.btn_zoomin);
        imageView = (ImageView) findViewById(R.id.image_view);

        if(ContextCompat.checkSelfPermission(MapsMarkerActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsMarkerActivity.this, new String[] {
                    Manifest.permission.CAMERA
            }, 100);
        }
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cv = new ContentValues();
                cv.put(MediaStore.Images.Media.TITLE, "test");
                cv.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
                tmpFileURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, tmpFileURI);
                startActivityForResult(intent, 1337);
            }
        });

        setUpMap();
    }

    private void parseUri(GoogleMap googleMap) throws IOException {
        for(Uri uri: uriList ) {
            InputStream in = getContentResolver().openInputStream(uri);
            ExifInterface ef = new ExifInterface(in);
            String markName = ef.getAttribute(TAG_USER_COMMENT);
            String markDescription = ef.getAttribute(TAG_IMAGE_DESCRIPTION);
            String markAuthor = ef.getAttribute(TAG_ARTIST);
            float markLatitude = Float.parseFloat(ef.getAttribute(TAG_GPS_LATITUDE_REF));
            float markLongitude = Float.parseFloat(ef.getAttribute(TAG_GPS_LONGITUDE_REF));


            LatLng location = new LatLng(markLatitude, markLongitude);
            newMark = googleMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title(markName)
                    .snippet("This is my spot!"));
            markList.add(new Mark(newMark, markDescription, markAuthor, 0,0, uri));
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1337) {
            //Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            try {
                Bitmap captureImage = MediaStore.Images.Media.getBitmap(getContentResolver(), tmpFileURI);
                Intent intent = new Intent(this, PhotoEditorActivity.class);
                intent.putExtra("captureImageURI", tmpFileURI.toString());
                startActivity(intent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //LatLng aalto = new LatLng(60.185364, 24.825476);
        /*
        googleMap.addMarker(new MarkerOptions()
            .position(aalto)
            .title("Marker in Aalto"));
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(aalto));
        */

        String imageUri = getIntent().getStringExtra("imageUri");
        newMark = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(60.185364, 24.825476))
                .title("markName")
                .snippet("This is my spot!"));
        if(imageUri != null) {
            uriList.add(Uri.parse(imageUri));
            try {
                parseUri(googleMap);
                displayMark(googleMap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        btn_zoomout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleMap.moveCamera(CameraUpdateFactory.zoomTo(googleMap.getCameraPosition().zoom - 1));
            }
        });
        btn_zoomin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleMap.moveCamera(CameraUpdateFactory.zoomTo(googleMap.getCameraPosition().zoom + 1));
            }
        });

    }

    private void displayMark(GoogleMap googleMap) {
        for (Mark mark: markList) {
            Toast.makeText(this, newMark.getTitle(), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, mark.description, Toast.LENGTH_SHORT).show();

            googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(newMark.getPosition()));
        }
        googleMap.setOnInfoWindowClickListener(this);
    }
    // [END maps_marker_on_map_ready_add_marker]

    public void openHelp(View view) {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }
    public void openEditor(View view) {
        Intent intent = new Intent(this, MarkerDetail.class);
        Mark test = new Mark(newMark,"asdasdasd", "", 1,1, testUri);
        intent.putExtra("marker", test);
        startActivity(intent);
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        //Toast.makeText(this, marker.toString(), Toast.LENGTH_SHORT).show();
        for (Mark mark: markList) {
            if(marker.equals(mark.marker)) {
                Toast.makeText(this, mark.marker.getTitle(), Toast.LENGTH_SHORT).show();
                Intent test = new Intent(this, MarkerDetail.class);
                test.putExtra("marker", (Serializable) "asd");
                startActivity(test);
            }
        }
    }
}
// [END maps_marker_on_map_ready]
