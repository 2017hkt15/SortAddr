package com.a2017hkt15.sortaddr;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylibrary.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import static com.a2017hkt15.sortaddr.Variable.MAX_NUMBER;
import static com.a2017hkt15.sortaddr.Variable.destinationPriority;

/**
 * Created by gwmail on 2017-08-21.
 */

public class ListViewAdapter extends BaseAdapter {

    private Button showDialog;
    private InputActivity inputActivity;
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewWay> listViewWayList = new ArrayList<>();

    private RadioButton radioButton;

    // ListViewAdapter의 생성자
    public ListViewAdapter(InputActivity inputActivity) {
        this.inputActivity = inputActivity;
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewWayList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();


        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_way, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageButton imageButtonDelete = (ImageButton) convertView.findViewById(R.id.imageButtonDelete);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.textViewTitle);
        EditText wayEditText = (EditText) convertView.findViewById(R.id.editTextWay);
        showDialog = (Button)convertView.findViewById(R.id.buttonPriorityVisit);




        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewWay listViewWay = listViewWayList.get(position);

        if(pos == 0) {
            showDialog.setVisibility(View.GONE);
            imageButtonDelete.setVisibility(View.INVISIBLE);
        }

        //초기값
        inputActivity.setButton_pos(-1);
        imageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pos==0)
                {
                    return;

                }
                else {
                    if (pos + 1 <= inputActivity.getAddressInfo_array().size()) {
                        for (int i = 0; i < MAX_NUMBER; i++)
                            for (int j = 0; j < MAX_NUMBER; j++)
                                destinationPriority[i][j] = false;

                        //해당 arraylist의 값을 삭제
                        inputActivity.getAddressInfo_array().remove(pos);
                        //해당 장소의 마커 삭제
                        inputActivity.getMarkerController().removeMarker(pos);
                        // listViewWayList.remove(pos);
                        // Variable.numberOfLine--;
                        // notifyDataSetChanged();    삭제해도 되는 듯
                    }
                    if (pos >= inputActivity.getAddressInfo_array().size() + 1) {
                        listViewWayList.remove(pos);
                        Variable.numberOfLine--;
                        notifyDataSetChanged();
                    } else {
                        listViewWayList.remove(pos);
                        Variable.numberOfLine--;
                        notifyDataSetChanged();
                    }
                }
            }
        });

        wayEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: 입력하는 Activity로 이동

                for(int i=1;i<position;i++)
                {
                    if(listViewWayList.get(i).getAddrStr()==null||listViewWayList.get(i).getAddrStr().equals(""))
                    {
                        Toast.makeText(context, "윗 줄 부터 순서대로 채워야 합니다.", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
              /*  Intent intent = new Intent(context, AutoCompleteActivity.class);
                //position값 보냄
                if (pos != 0) {
                    try {
                        inputActivity.getMarkerController().removeMarker(pos);
                        inputActivity.getAddressInfo_array().remove(pos);
                    } catch (Exception e) {


                    }
                } else if (pos == 0&&inputActivity.getAddressInfo_array().size()!=0)
                    try {
                        inputActivity.getAddressInfo_array().remove(pos);
                    } catch (Exception e) {

                    }

                Variable.panelHeight= ((SlidingUpPanelLayout) inputActivity.findViewById(R.id.sliding_layout)).getPanelHeight();
                //Log.d("cccc","Adapter height"+Variable.panelHeight);

                intent.putExtra("position", pos);
                inputActivity.startActivityForResult(intent, 1);   //pos
            }
        });

        // 아이템 내 각 위젯에 데이터 반영
//        imageButtonDelete.setImageDrawable(listViewWay.getDeleteImage());
        titleTextView.setText(listViewWay.getTitleStr());
        wayEditText.setText(listViewWay.getAddrStr());

        return convertView;*/
                final CharSequence[] selection = {"명칭 검색","주소 검색"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("알림");
                dialog.setSingleChoiceItems(selection, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(context,selection[which],Toast.LENGTH_SHORT).show();
                        if(which==0) {
                            Intent intent = new Intent(context, AutoCompleteActivity.class);
                            intent.putExtra("position", pos);
                            inputActivity.startActivityForResult(intent, 10);
                            dialog.cancel();
                        }
                        else {
                            Intent intent1 = new Intent(context, FullAddressActivty.class);
                            intent1.putExtra("position",pos);
                            //inputActivity.startActivity(intent);
                            inputActivity.startActivityForResult(intent1,2);
                            dialog.cancel();
                        }
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();

            }

        });

        //체크박스 Priority
        final List<String> list = new ArrayList<>();
        showDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cnt=0;
                String[] str = new String[MAX_NUMBER-1];
                for(int i = 0; i < MAX_NUMBER-1; i++) {
                    try {
                        str[i] = listViewWayList.get(i + 1).getAddrStr();
                        if(str[i]==null)
                            str[i]="(없음)";
                        cnt++;
//                        Log.d("ffff","for moon "+i+" : "+str[i]);
                    }
                    catch (Exception e){
                        //none
                    }
                }
                Log.d("ffff","out moon 1");
                //final String[] items = new String[]{str[0],str[1],str[2]};

                final String[] items = new String[cnt];
                Log.d("ffff","out moon 2 "+ items);
                for(int i=0;i<cnt;i++){
                    items[i]=str[i];
                }

                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("먼저 방문할 곳을 고르세요")
                        .setMultiChoiceItems(
                                items
                                , destinationPriority[pos]
                                , new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                        if(isChecked) {
//                                            Toast.makeText(context,items[which],Toast.LENGTH_LONG).show();
                                            list.add(items[which]);
                                        } else {
                                            list.remove(items[which]);
                                        }
                                    }
                                }
                        ).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedItem = "";
                        for (String item : list) {
                            selectedItem += item + ", ";
                        }
                        selectedItem += "을/를 들렀다 갑니다";

                        Toast.makeText(context, selectedItem, Toast.LENGTH_LONG).show();
                    }
                }).setNeutralButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "취소", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.create();
                dialog.show();

                Log.d("ffff",Variable.destinationPriority[0][0]+","+Variable.destinationPriority[0][1]+","+Variable.destinationPriority[1][0]+","+Variable.destinationPriority[1][1]);

            }
        });



        // 아이템 내 각 위젯에 데이터 반영
//        imageButtonDelete.setImageDrawable(listViewWay.getDeleteImage());
        titleTextView.setText(listViewWay.getTitleStr());
        wayEditText.setText(listViewWay.getAddrStr());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public ListViewWay getItem(int position) {
        return listViewWayList.get(position);
    }

    public void addItem(String title) {
        ListViewWay item = new ListViewWay();

        item.setTitleStr(title);

        Variable.numberOfLine++;
        listViewWayList.add(item);

        notifyDataSetChanged();
    }
}