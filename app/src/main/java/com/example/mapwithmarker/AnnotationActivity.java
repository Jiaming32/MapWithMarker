package com.example.mapwithmarker;

import static android.media.ExifInterface.TAG_ARTIST;
import static android.media.ExifInterface.TAG_IMAGE_DESCRIPTION;
import static android.media.ExifInterface.TAG_USER_COMMENT;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

public class AnnotationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Uri imageUri;
    String descrition;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent() != null) {
            imageUri = Uri.parse(getIntent().getStringExtra("imageUri"));
        }
        setContentView(R.layout.activity_annotation);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.mark_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        TextView markName = findViewById(R.id.editTextMarkName);
        TextView markDescription = findViewById(R.id.editTextMarkDescription);
        name = getResources().getStringArray(R.array.mark_names)[parent.getSelectedItemPosition()];
        descrition = getResources().getStringArray(R.array.mark_description)[parent.getSelectedItemPosition()];
        markName.setText(name);
        markDescription.setText(descrition);
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public String getFilePath(Context context, Uri uri) {
        String filePath = null;
        Cursor cursor = context.getContentResolver().query(imageUri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
        cursor.moveToFirst();
        filePath = cursor.getString(0);
        cursor.close();
        return filePath;
    }
    public void completeTable(View view) {

        try {
            ExifInterface newExif = new ExifInterface(getFilePath(this, imageUri));
            newExif.setAttribute(TAG_IMAGE_DESCRIPTION, descrition);
            newExif.setAttribute(TAG_USER_COMMENT, name);
            newExif.setAttribute(TAG_ARTIST, getString(R.string.user_id));
            newExif.saveAttributes();
            Toast.makeText(this, newExif.getAttribute(TAG_USER_COMMENT), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, newExif.getAttribute(TAG_IMAGE_DESCRIPTION), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, newExif.getAttribute(TAG_ARTIST), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}