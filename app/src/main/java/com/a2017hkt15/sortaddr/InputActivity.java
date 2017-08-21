package com.a2017hkt15.sortaddr;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapView;

import java.util.ArrayList;

/**
 * Created by gwmail on 2017-08-21.
 */

public class InputActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {
    private TMapGpsManager tmapgps = null;
    private TMapView tmapview = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        //툴바 세팅
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_input);
        setSupportActionBar(toolbar);

        toolbar.setTitle(R.string.app_name);
        String subtitle = "목적지 입력: 10개까지 입력 가능";
        toolbar.setSubtitle(subtitle);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(ContextCompat.getColor(InputActivity.this, R.color.colorSubtitle));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        LinearLayout layoutForMap = (LinearLayout) findViewById(R.id.layout_for_map);
        tmapview = new TMapView(this);
        tmapview.setSKPMapApiKey(Variable.mapApiKey);
        tmapview.setCompassMode(true);
        tmapview.setIconVisibility(true);
        tmapview.setZoomLevel(8);
        tmapview.setMapType(TMapView.MAPTYPE_STANDARD);
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);

        tmapgps = new TMapGpsManager(InputActivity.this);
        tmapgps.setMinTime(1000);
        tmapgps.setMinDistance(5);
        tmapgps.setProvider(tmapgps.NETWORK_PROVIDER);
//        tmapgps.setProvider(tmapgps.GPS_PROVIDER);
        tmapgps.OpenGps();

        tmapview.setTrackingMode(true);
        tmapview.setSightVisible(true);
        tmapview.setTrafficInfo(true);

        layoutForMap.addView(tmapview);
    }
    @Override
    public void onLocationChange(Location location) {
        tmapview.setLocationPoint(location.getLongitude(), location.getLatitude());
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Variable.numberOfLine = 0;
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
