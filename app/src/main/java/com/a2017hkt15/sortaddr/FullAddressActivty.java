package com.a2017hkt15.sortaddr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashSet;

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
    private ArrayList<String> address_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_address_activty);
        Button button = (Button) findViewById(R.id.button);
        edit_ex = (EditText)findViewById(R.id.edit_ex);
        edit_law = (EditText)findViewById(R.id.edit);
        Adapter = new ArrayAdapter<String>(FullAddressActivty.this, android.R.layout.simple_list_item_1, addressList);

        edit_law.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER){
                    onClick(v);
                    return true;
                }
                return false;
            }
        });
        edit_ex.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER){
                    onPass(v);
                    return true;
                }
                return false;
            }
        });
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

        final_fulladdress = fulladdress+edit_ex.getText().toString();
        Log.i("final",final_fulladdress);
    }
}
