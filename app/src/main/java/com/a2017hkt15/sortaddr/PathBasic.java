package com.a2017hkt15.sortaddr;

import android.content.DialogInterface;
import android.graphics.Color;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;
import java.util.ArrayList;

/**
 * Created by YouMin on 2017-08-21.
 */

public class PathBasic {
    private TMapView tmapView;
    private TMapData tmapdata;
    private MarkerController markerController;
    private DistanceCalcThread distanceCalcThread;

    private TMapPoint[][][] pathPoint;
    private double[][] distanceArr;

    private int[] pathRoute;

    private int pathID;

    public PathBasic(TMapView tmapView, MarkerController markerController) {
        this.pathID = 0;
        this.tmapView = tmapView;
        this.tmapdata = new TMapData();
        this.distanceArr = new double[10][10];
        this.pathPoint = new TMapPoint[10][10][2];
        this.markerController = markerController;
    }

    // 리스트에 있는 마커끼리의 거리를 계산해 거리 배열에 저장
    public void calcDistancePath(ArrayList<TMapMarkerItem> markerList, boolean comeback) {
        this.removePath();
        this.distanceCalcThread = new DistanceCalcThread(tmapdata);

        for (int start = 0; start < markerList.size(); start++) {
            for (int end = 0; end < markerList.size(); end++) {
                if (start == end)
                    distanceArr[start][end] = -1.0;
                else {
                    pathPoint[start][end][0] = markerList.get(start).getTMapPoint();
                    pathPoint[start][end][1] = markerList.get(end).getTMapPoint();
                }
            }
        }

        distanceCalcThread.setPoint(pathPoint, markerList.size());
        distanceCalcThread.start();
        synchronized (distanceCalcThread) {
            try {
                distanceCalcThread.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(comeback)
        {
            markerController.setEndIndex(0);
        }
        else
        {
            markerController.setEndIndex(-1);
        }

        distanceArr = distanceCalcThread.getDistanceArr();
        Log.d("dddd","size : "+markerController.getMarkerList().size());
        CalcPath calcPath = new CalcPath(0, markerController.getMarkerList().size(), distanceArr, markerController.getEndIndex(), Variable.destinationPriority);
        PathInfo pathInfo = calcPath.pathCalc();
        Log.d("dddd",""+pathInfo.getPathRoute()[0]+","+pathInfo.getPathRoute()[1]+","+pathInfo.getPathRoute()[2]+","+pathInfo.getPathRoute()[3]+","+pathInfo.getPathRoute()[4]+","+pathInfo.getPathRoute()[5]+","+pathInfo.getPathRoute()[6]+","+pathInfo.getPathRoute()[7]);

        if ( pathInfo == null ) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(InputActivity.mContext);
            alertDialog.setTitle("알림")
                    .setMessage("만들어질 수 없는 경로입니다.")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            AlertDialog dialog = alertDialog.create();
            if (InputActivity.progressDialog != null) {
                InputActivity.progressDialog.dismiss();
                InputActivity.progressDialog = null;
            }
            dialog.show();
        }
        else {
            this.showPath(pathInfo);
        }
    }

    // 완료된 경로를 지도에 표시
    public void showPath(PathInfo pathInfo) {
        this.setPathRoute(pathInfo.getPathRoute());

        ArrayList<TMapMarkerItem> markerList = markerController.getMarkerList();
        double distanceSum = 0;
        for(int cur = 0; cur < markerList.size() - 1; cur++) {
            distanceSum += distanceArr[pathRoute[cur]][pathRoute[cur+1]] / 1000d;

            if (pathRoute[cur + 1] != markerController.getEndIndex())
                markerController.setMarkerNumber(pathRoute[cur + 1], cur + 1);
            markerController.getMarkerList().get(pathRoute[cur+1]).setCalloutSubTitle("(" + Math.round(distanceSum) + "km)");

            TMapPolyLine polyLine = distanceCalcThread.getPathLine()[pathRoute[cur]][pathRoute[cur + 1]];
            polyLine.setLineColor(Color.BLUE);
            polyLine.setLineWidth(5);
            tmapView.addTMapPolyLine(pathID + "Route", polyLine);
            pathID++;

            if ( markerController.getEndIndex() == 0 && cur == markerList.size() - 2) {
                distanceSum += distanceArr[pathRoute[markerList.size() - 1]][0] / 1000d;
                markerController.getMarkerList().get(pathRoute[cur+1]).setCalloutSubTitle("(" + Math.round(distanceSum) + "km)");
                polyLine = (distanceCalcThread.getPathLine())[pathRoute[markerList.size() - 1]][0];
                polyLine.setLineColor(Color.BLUE);
                polyLine.setLineWidth(5);
                tmapView.addTMapPolyLine(pathID + "Route", polyLine);
                pathID++;
            }
        }


        if (InputActivity.progressDialog != null) {
            InputActivity.progressDialog.dismiss();
            InputActivity.progressDialog = null;
        }
    }

    public int[] getPathRoute() {
        return pathRoute;
    }

    public void setPathRoute(int[] pathRoute) {
        this.pathRoute = pathRoute;
    }

    public void removePath() {
        tmapView.removeAllTMapPolyLine();
    }

    public double[][] getDistanceArr() {
        return distanceArr;
    }
}

