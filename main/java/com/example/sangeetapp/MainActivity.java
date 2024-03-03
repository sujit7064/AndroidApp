package com.example.sangeetapp;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Set the content view first
        ListView listView = findViewById(R.id.listView); // Initialize ListView after setting content view

        Dexter.withContext(this)
        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
        .withListener(new PermissionListener() {
@Override
public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
       // Toast.makeText(MainActivity.this, "Runtime permission given", Toast.LENGTH_SHORT).show();
        ArrayList<File> mysongs = fetchsongs(Environment.getExternalStorageDirectory());
        String[] items = new String[mysongs.size()];
        for (int i = 0;  i < mysongs.size(); i++) {
        items[i] = mysongs.get(i).getName().replace("mp3", "");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, PlaySong.class);
                String currentsong = listView.getItemAtPosition(position).toString();
              intent.putExtra("songlist",mysongs);
              intent.putExtra("currentsongs",currentsong);
              intent.putExtra("position",position);
              startActivity(intent);
            }
        });
        }

@Override
public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
        // Handle permission denied
        }

@Override
public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
        permissionToken.continuePermissionRequest();
        }
        })
        .check();
        }
    public ArrayList<File> fetchsongs(File file) {
        ArrayList<File> arraylist = new ArrayList<>();
        File[] songs = file.listFiles();
        if (songs != null) {
            for (File myFile : songs){
                if(!myFile.isHidden() && myFile.isDirectory()){
                    arraylist.addAll(fetchsongs(myFile));
                }
                else{
                    if(myFile.getName().endsWith(".mp3") && !myFile.getName().startsWith(".")){
                        arraylist.add(myFile);
                    }

                }
            }
        }
        return arraylist;
    }


}
