package com.a2017hkt15.sortaddr;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mylibrary.SlidingUpPanelLayout;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapTapi;
import com.skp.Tmap.TMapView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by gwmail on 2017-08-21.
 */

public class InputActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {
    private SlidingUpPanelLayout mLayout;
    private MarkerController markerController;
    private CheckBox comeback;
    private PathBasic pathBasic;
    private int button_pos;
    private ListView listview;
    private ListViewAdapter adapter;
    private AddressInfo addressInfo = new AddressInfo(); //AddressInfo class
    private ArrayList<AddressInfo> AddressInfo_array = new ArrayList<>(Collections.nCopies(10,new AddressInfo()));
    String address_lat_lon;
    float lat;
    float lon;
    static ProgressDialog progressDialog;
    public static Context mContext;

    private TMapGpsManager tmapgps = null;
    private TMapView tmapview = null;

    private TMapTapi tmaptapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        mContext=this;

        Variable.numberOfLine=0;

        comeback=(CheckBox)findViewById(R.id.comeBack);

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
        tmapview.setCompassMode(false);
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

        tmaptapi = new TMapTapi(InputActivity.this);

        tmapview.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
            @Override
            public void onCalloutRightButton(TMapMarkerItem markerItem) {
                //tmap 설치유무 확인
                boolean isTmapApp = tmaptapi.isTmapApplicationInstalled();

                if(!isTmapApp) {
                    Toast.makeText(InputActivity.this, "T map 이 설치되어 있지 않습니다. 설치 페이지로 이동합니다.",Toast.LENGTH_LONG).show();
                    //T map 어플리케이션 다운로드
                    tmapInstall();
                } else {
                    boolean result = tmaptapi.invokeRoute(markerItem.getName(), (float)markerItem.getTMapPoint().getLongitude(), (float)markerItem.getTMapPoint().getLatitude());
                    Toast.makeText(InputActivity.this, "경로탐색을 시작합니다. 실행되지 않을 경우 시스템 어플리케이션을 확인해주세요.",Toast.LENGTH_LONG).show();
                    Log.d("tmapicon", "Tmap 경로탐색 : " + result);
                }
                //기존 구글검색 기능 Intent
//                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
//                intent.putExtra(SearchManager.QUERY, markerItem.getName());
//                startActivity(intent);
            }
        });

        layoutForMap.addView(tmapview);

        // Adapter 생성
        adapter = new ListViewAdapter(this);

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.wayListView1);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        // 첫 번째 아이템 추가.
        adapter.addItem("출발지");

        addLine();

        ImageButton addimageButton = (ImageButton) findViewById(R.id.addimageButton);
        addimageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLine();
            }
        });


        // 현재 위치 마커 아이콘 리소스 불러온 후 적용
        Context context = getApplicationContext();
        Bitmap startIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.start);
        Bitmap passIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.pass);
        Bitmap endIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.end);
        Bitmap poiIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.search);

        Bitmap[] numberIcon = new Bitmap[10];
        numberIcon[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.mark1);
        numberIcon[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.mark2);
        numberIcon[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.mark3);
        numberIcon[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.mark4);
        numberIcon[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.mark5);
        numberIcon[6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.mark6);
        numberIcon[7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.mark7);
        numberIcon[8] = BitmapFactory.decodeResource(context.getResources(), R.drawable.mark8);
        numberIcon[9] = BitmapFactory.decodeResource(context.getResources(), R.drawable.mark9);

        // 마커, 경로 관련 클래스
        markerController = new MarkerController(tmapview, startIcon, passIcon, numberIcon, endIcon, poiIcon);
        pathBasic = new PathBasic(tmapview, markerController);
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);


    }

    public void tmapInstall() {
        new Thread() {
            @Override
            public void run() {
                Uri uri = Uri.parse(tmaptapi.getTMapDownUrl().get(0));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

                Log.d("tmapicon", "미설치 : "+tmaptapi.getTMapDownUrl().get(0));
            }

        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_input, menu);

        return true;
    }

    @Override
    public void onLocationChange(Location location) {
        tmapview.setLocationPoint(location.getLongitude(), location.getLatitude());
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_bt1:
                findButton();
                break;
            case android.R.id.home:
                Variable.numberOfLine = 0;
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            progressDialog.dismiss(); // 다이얼로그 삭제
            // View갱신
        }
    };

    //검색 버튼 클릭
    //경로 출력
    private void findButton() {
        if (AddressInfo_array.size() > 1) {    //출발지1개 목적지1개 일 때
            progressDialog = ProgressDialog.show(InputActivity.this, "경로 탐색 중", "잠시만 기다려주세요");

            mHandler = new Handler();

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    // 처리할 부분
                    pathBasic.calcDistancePath(markerController.getMarkerList(),comeback.isChecked());
                    mHandler.sendEmptyMessage(0);
                }
            });
            thread.start();

//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    pathBasic.calcDistancePath(markerController.getMarkerList(),comeback.isChecked());
//                }
//            }, 1000);
        } else if (AddressInfo_array.size() == 0) {   //출발지 입력을 안 했을때
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(InputActivity.this);
            alertDialog.setTitle("알림")
                    .setMessage("출발지를 입력해주세요")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            AlertDialog dialog = alertDialog.create();
            dialog.show();
        } else if (AddressInfo_array.size() == 1) {   //목적지 입력을 안했을때
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(InputActivity.this);
            alertDialog.setTitle("알림")
                    .setMessage("목적지를 입력해주세요")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            AlertDialog dialog = alertDialog.create();
            dialog.show();
        }
    }

    private void addLine() {
        if (Variable.numberOfLine < 10) {
            adapter.addItem("목적지");
        } else {
            Toast.makeText(getApplicationContext(), "최대 갯수에 도달했습니다.", Toast.LENGTH_LONG).show();
        }
    }

    //AutoComplete으로부터 데이터를 받음
    //position, address_name
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {int division = getIntent().getIntExtra("division",0);
        if(resultCode == RESULT_OK) {
            if (requestCode == 10) {
                final int position = intent.getIntExtra("position", 0);
                final String address_name = intent.getStringExtra("address_name");
                addressInfo.setAddr(address_name);
                adapter.getItem(position).setAddrStr(address_name);
                //edittext에 setText
                adapter.notifyDataSetChanged();
                //변경완료
                runOnUiThread(new Runnable() {
                    public void run() {
                        TMapData tdata = new TMapData();
                        tdata.findAllPOI(address_name, new TMapData.FindAllPOIListenerCallback() {
                            @Override
                            public void onFindAllPOI(ArrayList<TMapPOIItem> poiItem) {
                                String[] array;
                                TMapPOIItem item2 = poiItem.get(poiItem.size() - 1);
                                array = item2.getPOIPoint().toString().split(" ");
                                lat = Float.parseFloat(array[1]);
                                addressInfo.setLat(Float.parseFloat(array[1]));
                                lon = Float.parseFloat(array[3]);
                                addressInfo.setLon(lon);
                            }
                        });
                    }
                });
                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        address_lat_lon = address_name + "," + String.valueOf(addressInfo.getLat()) + "," + String.valueOf(addressInfo.getLon());
                        if (position < AddressInfo_array.size()) {
                            AddressInfo_array.remove(position);
                            AddressInfo_array.add(position, addressInfo);
                        } else if (position == AddressInfo_array.size()) {
                            AddressInfo_array.add(position, addressInfo);
                        }
                        // AddressInfo_array.add(position, addressInfo);
                        if (position == 0) {
                            markerController.setStartMarker(AddressInfo_array.get(position).getLat(), AddressInfo_array.get(position).getLon(), AddressInfo_array.get(0).getAddr());
                        } else {
                            if (position < markerController.getMarkerList().size()) {
                                markerController.removeMarker(position);
                                markerController.addMarker(AddressInfo_array.get(position).getLat(), AddressInfo_array.get(position).getLon(), AddressInfo_array.get(position).getAddr(), position);
                            } else if (position == markerController.getMarkerList().size()) {
                                markerController.addMarker(AddressInfo_array.get(position).getLat(), AddressInfo_array.get(position).getLon(), AddressInfo_array.get(position).getAddr());
                            }
                        }
                    }
                }, 1000);
            }
            if (requestCode == 2) {
                final int position = intent.getIntExtra("position_full", 0);
                final String address_name = intent.getStringExtra("fulladdress");
                addressInfo.setAddr(address_name);
                adapter.getItem(position).setAddrStr(address_name);
                //edittext에 setText
                adapter.notifyDataSetChanged();
                //변경완료
                final float lat = intent.getFloatExtra("lati_full", 0);
                final float lon = intent.getFloatExtra("lon_full", 0);
                addressInfo.setAddr(address_name);
                addressInfo.setLat(lat);
                addressInfo.setLon(lon);
                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        address_lat_lon = address_name + "," + String.valueOf(addressInfo.getLat()) + "," + String.valueOf(addressInfo.getLon());
                        if (position < AddressInfo_array.size()) {
                            AddressInfo_array.remove(position);
                            AddressInfo_array.add(position, addressInfo);
                        } else if (position == AddressInfo_array.size()) {
                            AddressInfo_array.add(position, addressInfo);
                        }
                        // AddressInfo_array.add(position, addressInfo);
                        if (position == 0) {
                            markerController.setStartMarker(AddressInfo_array.get(position).getLat(), AddressInfo_array.get(position).getLon(), AddressInfo_array.get(0).getAddr());
                        } else {
                            if (position < markerController.getMarkerList().size()) {
                                markerController.removeMarker(position);
                                markerController.addMarker(AddressInfo_array.get(position).getLat(), AddressInfo_array.get(position).getLon(), AddressInfo_array.get(position).getAddr(), position);
                            } else if (position == markerController.getMarkerList().size()) {
                                markerController.addMarker(AddressInfo_array.get(position).getLat(), AddressInfo_array.get(position).getLon(), AddressInfo_array.get(position).getAddr());
                            }
                        }
                    }
                }, 1000);
            }
        }
        else if (resultCode == RESULT_CANCELED) {

        }

        Log.d("supl","supl end");
//        mLayout.setPanelHeight(Variable.panelHeight);
    }

    public int getButton_pos() {
        return button_pos;
    }

    public void setButton_pos(int button_pos) {
        this.button_pos = button_pos;
    }

    public ArrayList<AddressInfo> getAddressInfo_array() {
        return AddressInfo_array;
    }

    public void setAddressInfo_array(ArrayList<AddressInfo> addressInfo_array) {
        AddressInfo_array = addressInfo_array;
    }

    public MarkerController getMarkerController() {
        return markerController;
    }

    public void setMarkerController(MarkerController markerController) {
        this.markerController = markerController;
    }
}