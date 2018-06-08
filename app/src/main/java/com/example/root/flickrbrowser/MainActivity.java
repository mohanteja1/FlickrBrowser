package com.example.root.flickrbrowser;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity implements GetFlickrJasonData.OnDataAvailable{
    private static final String TAG = "MainActivity";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Log.d(TAG, "onCreate: ends");

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: starts");
        GetFlickrJasonData getFlickrJasonData = new GetFlickrJasonData(this,"https://api.flickr.com/services/feeds/photos_public.gne?",true,"en-us");

        getFlickrJasonData.execute("android,nougat");

        Log.d(TAG, "onResume: ends");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.d(TAG, "onCreateOptionsMenu() returned: " + true);
        return true;
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        Log.d(TAG, "onOptionsItemSelected() returned: " + true);
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDataAvailable(List<Photo> photoList, DownloadStatus downloadStatus) {

        if(downloadStatus==DownloadStatus.OK){
            Log.d(TAG, "onDataAvailable: photo list =  " + photoList.toString());
        }
        else
        {
            Log.e(TAG, "onDataAvailable: error downloading "+ downloadStatus);
        }

    }








}
