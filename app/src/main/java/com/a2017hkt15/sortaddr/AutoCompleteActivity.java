package com.a2017hkt15.sortaddr;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Toast;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapView;

import java.util.ArrayList;

public class AutoCompleteActivity extends AppCompatActivity {
    TMapData tMapdata = new TMapData();
    String address_send;
    static double lat;
    static double lon;
    String address;
    EditText editText;
    String ggu="어이어이, 그 앞은 '無'다.";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_complete);
       editText = (EditText) findViewById(R.id.edit);
        //툴바 세팅
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_auto_complete);
        setSupportActionBar(toolbar);

        toolbar.setTitle(R.string.app_name);
        String subtitle = "목적지 입력: 검색 후 목적지 클릭";
        toolbar.setSubtitle(subtitle);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(ContextCompat.getColor(AutoCompleteActivity.this, R.color.colorSubtitle));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Button button = (Button) findViewById(R.id.button);

        TMapView tmapview = new TMapView(this);
        tmapview.setSKPMapApiKey(Variable.mapApiKey);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    onClick(v);
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
                Intent intent = new Intent(AutoCompleteActivity.this, InputActivity.class);
                setResult(RESULT_CANCELED, intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {

        final ArrayList<String> addressList = new ArrayList<>();
        // String address;

        address = editText.getText().toString();

        //address가 장소 이름 edittext로 부터 받아옴
        //address를 키워드로 해당 리스트를 받아옴
        runOnUiThread(new Runnable() {
            public void run() {
                tMapdata.autoComplete(address, new TMapData.AutoCompleteListenerCallback() {
                    @Override
                    public void onAutoComplete(ArrayList<String> poiltem) {
                        if(poiltem.size()==0) {
                            addressList.clear();
                            addressList.add(ggu);
                        }

                        for (int i = 0; i < poiltem.size(); i++) {
                            addressList.add(poiltem.get(i));
                        }
                        runOnUiThread(new Runnable() {
                            public void run() {
                                ArrayAdapter<String> Adapter;
                                Adapter = new ArrayAdapter<String>(AutoCompleteActivity.this, android.R.layout.simple_list_item_1, addressList);
                                ListView list = (ListView) findViewById(R.id.list);
                                Adapter.notifyDataSetChanged();
                                list.setAdapter(Adapter);
                                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        if(addressList.get(position).equals(ggu))
                                            return;
                                        editText.setText(addressList.get(position));
                                        address_send = editText.getText().toString();
                                        Intent intent1 = getIntent();

                                        //위 경도값도 같이 보내기
                                        //받는 곳 inputActivity에서 배열로 저장 후 마커 찍기
                                        AddressInfo info = new AddressInfo();
                                        position = intent1.getIntExtra("position", 0);
                                        Intent intent = new Intent(AutoCompleteActivity.this, InputActivity.class);
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                TMapData tdata = new TMapData();
                                                tdata.findAllPOI(address_send, new TMapData.FindAllPOIListenerCallback() {
                                                    @Override
                                                    public void onFindAllPOI(ArrayList<TMapPOIItem> poiItem) {
                                                        String[] array;
                                                        TMapPOIItem item2 = poiItem.get(poiItem.size() - 1);
                                                        array = item2.getPOIPoint().toString().split(" ");
                                                        lat = Double.parseDouble(array[1]);
                                                        lon = Double.parseDouble(array[3]);
                                                    }
                                                });
                                            }
                                        });
                                        intent.putExtra("address_name", address_send);
                                        intent.putExtra("position", position);
                                        intent.putExtra("division",1);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

    }
}
