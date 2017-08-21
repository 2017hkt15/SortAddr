package com.a2017hkt15.sortaddr;


import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;

import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by YouMin on 2017-08-21.
 */

public class DistanceCalcThread extends Thread {
    private TMapData tmapdata;

    private TMapPoint[][][] pathPoint;
    private TMapPolyLine[][] pathLine;
    private double[][] distanceArr;
    private int pointSize;


    DistanceCalcThread(TMapData tmapdata) {
        super();
        this.tmapdata = tmapdata;
        this.distanceArr = new double[10][10];
        this.pathPoint = new TMapPoint[10][10][2];
        this.pathLine = new TMapPolyLine[10][10];
    }
    public void run() {
        synchronized (this) {
            try {
                for (int start = 0; start < pointSize; start++) {
                    for (int end = start + 1; end < pointSize; end++) {
                        this.pathLine[start][end] =  tmapdata.findPathData(pathPoint[start][end][0], pathPoint[start][end][1]);
                        this.distanceArr[start][end] = this.pathLine[start][end].getDistance();

                        this.pathLine[end][start] = this.pathLine[start][end];
                        this.distanceArr[end][start] = this.distanceArr[start][end];
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (RuntimeException var14) {
                var14.printStackTrace();
                throw var14;
            } catch (Exception var15) {
                var15.printStackTrace();
            }
            notify();
        }
    }

    public void setPoint(TMapPoint[][][] pathPoint, int pointSize) {
        this.pathPoint = pathPoint;
        this.pointSize = pointSize;
    }

    public double[][] getDistanceArr() {
        return distanceArr;
    }

    public TMapPolyLine[][] getPathLine() {
        return pathLine;
    }
}