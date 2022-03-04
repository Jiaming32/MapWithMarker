package com.example.mapwithmarker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MarkerDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_detail);
        TextView markerName = findViewById(R.id.markerName);
        TextView markerDescription = findViewById(R.id.markerDescription);
        TextView likes = findViewById(R.id.likes);
        TextView dislikes = findViewById(R.id.dislikes);
        ImageView markerImage = findViewById(R.id.markerImage);

        if(getIntent().getExtras() != null) {
            Mark mark = (Mark) getIntent().getParcelableExtra("marker");
            //markerName.setText(mark.);
            markerDescription.setText(mark.description);
            //likes.setText(mark.likes);
            //dislikes.setText(mark.dislikes);
            //markerImage.setImageURI(mark.imageUri);
        }



    }
}