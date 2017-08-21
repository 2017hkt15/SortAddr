package com.a2017hkt15.sortaddr;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by gwmail on 2017-08-21.
 */

public class ListViewAdapter extends BaseAdapter {
    private InputActivity inputActivity;
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewWay> listViewWayList = new ArrayList<>();

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
    public View getView(int position, View convertView, ViewGroup parent) {
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

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewWay listViewWay = listViewWayList.get(position);
        //초기값
        inputActivity.setButton_pos(-1);
        imageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pos != 0 && pos + 1 <= inputActivity.getAddressInfo_array().size()) {

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
                } else if(pos!=0){
                    listViewWayList.remove(pos);
                    Variable.numberOfLine--;
                    notifyDataSetChanged();
                }

            }
        });

        wayEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: 입력하는 Activity로 이동
                Intent intent = new Intent(context, AutoCompleteActivity.class);
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
                intent.putExtra("position", pos);
                inputActivity.startActivityForResult(intent, 1);   //pos
            }
        });

        // 아이템 내 각 위젯에 데이터 반영
        imageButtonDelete.setImageDrawable(listViewWay.getDeleteImage());
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