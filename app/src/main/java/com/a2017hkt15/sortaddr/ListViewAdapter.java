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