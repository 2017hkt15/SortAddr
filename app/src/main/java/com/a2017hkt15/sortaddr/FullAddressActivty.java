package com.a2017hkt15.sortaddr;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapPOIItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FullAddressActivty extends AppCompatActivity {
    String address_text;
    TMapData tMapdata = new TMapData();
    private ArrayList<String> addressList = new ArrayList<>();
    String a;
    private ArrayList<String> address_List;
    private ArrayAdapter<String> Adapter;
    String fulladdress;
    String final_fulladdress;
    EditText edit_law;
    EditText edit_ex;
    int position;
    float lati_full;
    float lon_full;
    List<Address> addr = null;
    private ArrayList<String> address_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_address_activty);
        //툴바 세팅
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_auto_complete);
        setSupportActionBar(toolbar);

        toolbar.setTitle(R.string.app_name);
        String subtitle = "목적지 입력: 검색 후 입력 클릭";
        toolbar.setSubtitle(subtitle);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(ContextCompat.getColor(FullAddressActivty.this, R.color.colorSubtitle));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Button button = (Button) findViewById(R.id.button);
        edit_ex = (EditText) findViewById(R.id.edit_ex);
        edit_law = (EditText) findViewById(R.id.edit);
        Adapter = new ArrayAdapter<String>(FullAddressActivty.this, android.R.layout.simple_list_item_1, addressList);
        Log.i("gggg2", "gggg");
        edit_law.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    onClick(v);
                    return true;
                }
                return false;
            }
        });
        edit_ex.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    onPass(v);
                    return true;
                }
                return false;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(FullAddressActivty.this, InputActivity.class);
                setResult(RESULT_CANCELED, intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    public void onClick(View v) {
        //    final EditText edit_law = (EditText) findViewById(R.id.edit);
        final ArrayList<String> addressList = new ArrayList<>();

        fulladdress = edit_law.getText().toString();

        Log.i("check123", fulladdress);          //장소 입력
        tMapdata.findAddressPOI(fulladdress, 300, new TMapData.FindAddressPOIListenerCallback() {
            @Override
            public void onFindAddressPOI(ArrayList<TMapPOIItem> poiItem) {
                for (int i = 0; i < poiItem.size(); i++) {
                    TMapPOIItem item = poiItem.get(i);
                    addressList.add(item.getPOIAddress().replace("null", ""));
                }
                HashSet hs = new HashSet(addressList);
                address_list = new ArrayList<String>(hs);
                // ArrayList<String> address_List = new ArrayList<String>(hs);
                runOnUiThread(new Runnable() {
                    public void run() {
                        ListView list = (ListView) findViewById(R.id.list);
                        Adapter = new ArrayAdapter<String>(FullAddressActivty.this, android.R.layout.simple_list_item_1, address_list);
                        list.setAdapter(Adapter);
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                edit_law.setText(address_list.get(position));
                                fulladdress = edit_law.getText().toString();
                            }
                        });
                    }
                });
            }

        });

    }

    public void onPass(View v) {

        final_fulladdress = fulladdress + edit_ex.getText().toString();
        Log.i("final", final_fulladdress);
        //intent가 pos을 보냄


        final Location loc = new Location("");
        final Geocoder geocoder = new Geocoder(FullAddressActivty.this);
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        Log.i("position",position+"");
        Intent intent1 = new Intent(FullAddressActivty.this, InputActivity.class);
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    addr = geocoder.getFromLocationName(fulladdress, 5);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addr != null) {
                    if (addr.size() == 0)
                        Log.i("error", "addr.size222 == 0");
                    for (int i = 0; i < addr.size(); i++) {
                        Address lating = addr.get(i);
                        double lat = lating.getLatitude(); // 위도가져오기
                        double lon = lating.getLongitude(); // 경도가져오기
                        loc.setLatitude(lat);
                        loc.setLongitude(lon);
                        lati_full = Float.parseFloat(String.valueOf(loc.getLatitude()));
                        lon_full = Float.parseFloat(String.valueOf(loc.getLongitude()));
                        Log.i("check20", String.valueOf(loc.getLatitude()));
                        Log.i("check10", String.valueOf(loc.getLongitude()));
                    }
                }
            }
        });
        intent1.putExtra("lati_full",lati_full);
        intent1.putExtra("lon_full",lon_full);
        intent1.putExtra("position_full",position);
        intent1.putExtra("fulladdress",final_fulladdress);
        intent1.putExtra("division",2);
        setResult(RESULT_OK, intent1);

        finish();
    }
}
