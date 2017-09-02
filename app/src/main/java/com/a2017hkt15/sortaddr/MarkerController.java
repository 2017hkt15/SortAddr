package com.a2017hkt15.sortaddr;

import android.graphics.Bitmap;
import android.util.Log;

import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

import java.util.ArrayList;

/**
 * Created by YouMin on 2017-08-21.
 */

public class MarkerController {
    // 최초 시작시 스타트 마커 여부
    private boolean isStartExist;
    private boolean isEndExist;
    private int endIndex = -1;

    // 현재 체크된 마커 리스트들
    private ArrayList<TMapMarkerItem> markerList;

    // 마커 중심점 위치 (하단, 중앙)
    private float markerCenterDx = 0.5f;
    private float markerCenterDy = 1.0f;

    // 지도 포인터 및 마커 아이콘 설정
    private TMapView tmapView;
    private Bitmap startMarkerIcon;
    private Bitmap passMarkerIcon;
    private Bitmap endMarkerIcon;
    private Bitmap poiIcon;
    private Bitmap[] numberMarkerIcon;

    public static boolean firstDefault = true;
    public static boolean secondDefault = true;
    public static boolean thirdDefault = true;

    private TMapMarkerItem[] markerArray = new TMapMarkerItem[10];

    public MarkerController (TMapView tmapView, Bitmap startMarkerIcon, Bitmap passMarkerIcon, Bitmap[] markerNumberIcon, Bitmap endMarkerIcon, Bitmap poiIcon) {
        this.tmapView = tmapView;
        this.isStartExist = false;
        this.isEndExist = false;
        this.startMarkerIcon = startMarkerIcon;
        this.passMarkerIcon = passMarkerIcon;
        this.numberMarkerIcon = markerNumberIcon;
        this.endMarkerIcon = endMarkerIcon;
        this.poiIcon = poiIcon;
        this.markerList = new ArrayList<TMapMarkerItem>();

        for (int i = 0; i < 2; i++) {
            TMapMarkerItem temp = new TMapMarkerItem();
            temp.setID("dummy" + i);
            markerList.add(temp);
            tmapView.addMarkerItem(temp.getID(), temp);
        }
    }

    // 위도, 경도, 마커(장소) 이름
    public void addMarker(float latitude, float longitude, String placeName) {
        // 일반 마커
        // 포인트 지점 인스턴스 생성 후 마커 인스턴스 생성
        TMapPoint placePoint = new TMapPoint(latitude, longitude);
        TMapMarkerItem placeMarker = new TMapMarkerItem();

        // 마커 속성 설정
        placeMarker.setTMapPoint(placePoint);
        placeMarker.setID(placeName);
        placeMarker.setName(placeName);
        placeMarker.setVisible(TMapMarkerItem.VISIBLE);
        placeMarker.setIcon(passMarkerIcon);
        placeMarker.setPosition(markerCenterDx, markerCenterDy);
        placeMarker.setCanShowCallout(true);
        placeMarker.setAutoCalloutVisible(true);
        placeMarker.setCalloutTitle(placeName);
        placeMarker.setCalloutLeftImage(passMarkerIcon);
        placeMarker.setCalloutRightButtonImage(poiIcon);

        // 배열리스트 및 지도에 마커 추가
        tmapView.addMarkerItem(placeMarker.getID(), placeMarker);
        markerList.add(placeMarker);

    }

    // 위도, 경도, 마커(장소) 이름
    public void addMarker(float latitude, float longitude, String placeName, int pos) {
        Log.d("qwer", "in");

        // 일반 마커
        // 포인트 지점 인스턴스 생성 후 마커 인스턴스 생성
        TMapPoint placePoint = new TMapPoint(latitude, longitude);
        TMapMarkerItem placeMarker = new TMapMarkerItem();

        // 마커 속성 설정
        placeMarker.setTMapPoint(placePoint);
        placeMarker.setID(placeName);
        placeMarker.setName(placeName);
        placeMarker.setVisible(TMapMarkerItem.VISIBLE);
        placeMarker.setIcon(passMarkerIcon);
        placeMarker.setPosition(markerCenterDx, markerCenterDy);
        placeMarker.setCanShowCallout(true);
        placeMarker.setAutoCalloutVisible(true);
        placeMarker.setCalloutTitle(placeName);
        placeMarker.setCalloutLeftImage(passMarkerIcon);
        placeMarker.setCalloutRightButtonImage(poiIcon);

        // 배열리스트 및 지도에 마커 추가
        markerList.add(pos, placeMarker);
        tmapView.addMarkerItem(placeMarker.getID(), placeMarker);

    }

    public void addMarker2(float latitude, float longitude, String placeName, int pos) {
        // 일반 마커
        // 포인트 지점 인스턴스 생성 후 마커 인스턴스 생성
        TMapPoint placePoint = new TMapPoint(latitude, longitude);
        TMapMarkerItem placeMarker = new TMapMarkerItem();

        // 마커 속성 설정
        placeMarker.setTMapPoint(placePoint);
        placeMarker.setID(placeName);
        placeMarker.setName(placeName);
        placeMarker.setVisible(TMapMarkerItem.VISIBLE);
        placeMarker.setIcon(passMarkerIcon);
        placeMarker.setPosition(markerCenterDx, markerCenterDy);
        placeMarker.setCanShowCallout(true);
        placeMarker.setAutoCalloutVisible(true);
        placeMarker.setCalloutTitle(placeName);
        placeMarker.setCalloutLeftImage(passMarkerIcon);
        placeMarker.setCalloutRightButtonImage(poiIcon);

        // 배열리스트 및 지도에 마커 추가
        tmapView.addMarkerItem(placeMarker.getID(), placeMarker);
        markerArray[pos] = placeMarker;

    }

    public void setStartMarker2(float latitude, float longitude, String placeName) {
        // 시작 마커가 이미 있다면 제거하고 새롭게 생성
        removeMarker2(0);

        TMapPoint placePoint = new TMapPoint(latitude, longitude);
        TMapMarkerItem placeMarker = new TMapMarkerItem();

        // 마커 속성 설정
        placeMarker.setTMapPoint(placePoint);
        placeMarker.setID(placeName);
        placeMarker.setName(placeName);
        placeMarker.setVisible(TMapMarkerItem.VISIBLE);
        placeMarker.setIcon(startMarkerIcon);
        placeMarker.setPosition(markerCenterDx, markerCenterDy);
        placeMarker.setCanShowCallout(true);
        placeMarker.setAutoCalloutVisible(true);
        placeMarker.setCalloutTitle(placeName);
        placeMarker.setCalloutLeftImage(startMarkerIcon);
        placeMarker.setCalloutRightButtonImage(poiIcon);

        // 배열리스트 및 지도에 마커 추가
        tmapView.addMarkerItem(placeMarker.getID(), placeMarker);
        markerArray[0] = placeMarker;
    }

    public void setStartMarker(float latitude, float longitude, String placeName) {
        // 시작 마커가 이미 있다면 제거하고 새롭게 생성
        removeMarker(0);

        TMapPoint placePoint = new TMapPoint(latitude, longitude);
        TMapMarkerItem placeMarker = new TMapMarkerItem();

        // 마커 속성 설정
        placeMarker.setTMapPoint(placePoint);
        placeMarker.setID(placeName);
        placeMarker.setName(placeName);
        placeMarker.setVisible(TMapMarkerItem.VISIBLE);
        placeMarker.setIcon(startMarkerIcon);
        placeMarker.setPosition(markerCenterDx, markerCenterDy);
        placeMarker.setCanShowCallout(true);
        placeMarker.setAutoCalloutVisible(true);
        placeMarker.setCalloutTitle(placeName);
        placeMarker.setCalloutLeftImage(startMarkerIcon);
        placeMarker.setCalloutRightButtonImage(poiIcon);

        // 배열리스트 및 지도에 마커 추가
        tmapView.addMarkerItem(placeMarker.getID(), placeMarker);
        markerList.add(0, placeMarker);
        isStartExist = true;
    }

    public void setMarkerNumber(int index, int number) {
        // 경유지 마커
        if (number == 0) return;
        if (isEndExist && endIndex == markerList.size() - 1) return;
        markerList.get(index).setIcon(numberMarkerIcon[number]);
        tmapView.removeMarkerItem(markerList.get(index).getID());
        tmapView.addMarkerItem(markerList.get(index).getID(), markerList.get(index));
    }

    public void setEndIndex(int index) {
        if (index == -1) {
            this.endIndex = index;
            this.isEndExist = false;
            return;
        }

        if (isEndExist) {
            markerList.get(endIndex).setIcon(passMarkerIcon);
            tmapView.removeMarkerItem(markerList.get(endIndex).getID());
            tmapView.addMarkerItem(markerList.get(endIndex).getID(), markerList.get(endIndex));
        }

        if (index == 0) {
            this.endIndex = index;
            this.isEndExist = true;
            return;
        }
        else {
            this.endIndex = index;
            this.isEndExist = true;
            markerList.get(index).setIcon(endMarkerIcon);
            tmapView.removeMarkerItem(markerList.get(index).getID());
            tmapView.addMarkerItem(markerList.get(index).getID(), markerList.get(index));
        }
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void modifyMarker(float latitude, float longitude, String placeName, int markerIndex) {
        if (markerList.size() <= markerIndex) return;
        Log.d("ssss", markerList.get(markerIndex).getID());
        tmapView.removeMarkerItem(markerList.get(markerIndex).getID());
        markerList.remove(markerIndex);

        TMapPoint placePoint = new TMapPoint(latitude, longitude);
        TMapMarkerItem placeMarker = new TMapMarkerItem();

        // 마커 속성 설정
        placeMarker.setTMapPoint(placePoint);
        placeMarker.setID(placeName);
        placeMarker.setName(placeName);
        placeMarker.setVisible(TMapMarkerItem.VISIBLE);
        placeMarker.setIcon(passMarkerIcon);
        placeMarker.setPosition(markerCenterDx, markerCenterDy);
        placeMarker.setCanShowCallout(true);
        placeMarker.setAutoCalloutVisible(true);
        placeMarker.setCalloutTitle(placeName);
        placeMarker.setCalloutLeftImage(passMarkerIcon);
        placeMarker.setCalloutRightButtonImage(poiIcon);

        // 배열리스트 및 지도에 마커 추가
        tmapView.addMarkerItem(placeMarker.getID(), placeMarker);
        markerList.add(markerIndex, placeMarker);
    }

    // Parameter : 지울 마커의 인덱스 번호
    public void removeMarker(int markerIndex) {
        // 해당 번호의 마커를 맵과 배열 리스트에서 삭제.
        if (markerList.size() <= markerIndex) return;
        Log.d("ssss", markerList.get(markerIndex).getID());
        tmapView.removeMarkerItem(markerList.get(markerIndex).getID());
        markerList.remove(markerIndex);
    }

    public void removeMarker2(int markerIndex) {
        // 해당 번호의 마커를 맵과 배열 리스트에서 삭제.
        tmapView.removeMarkerItem(markerArray[markerIndex].getID());
        markerArray[markerIndex] = null;
    }

    // 모든 마커 제거 (true : 시작점 포함, false : 시작점 미포함)
    public void removeAllMarker(boolean fromStart) {
        int isStart;

        if (fromStart) {
            isStart = -1;
            isStartExist = false;
        }
        else isStart = 0;

        for (int index = markerList.size() - 1; index > isStart; index--)
            removeMarker(index);
    }

    public ArrayList<TMapMarkerItem> getMarkerList() {
        return markerList;
    }

    public TMapMarkerItem[] getMarkerArray() {
        return markerArray;
    }
}