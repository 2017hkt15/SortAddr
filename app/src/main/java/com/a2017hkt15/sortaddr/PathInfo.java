package com.a2017hkt15.sortaddr;

/**
 * Created by 함상혁입니다 on 2017-08-21.
 */

public class PathInfo {
    private int[] pathRoute;
    private double pathLength;

    public PathInfo()
    {

    }

    public PathInfo(int[] pr,double pl)
    {
        pathRoute = pr;
        pathLength =pl;
    }


    public int[] getPathRoute() {
        return pathRoute;
    }

    public void setPathRoute(int[] pathRoute) {
        this.pathRoute = pathRoute;
    }

    public double getPathLength() {
        return pathLength;
    }

    public void setPathLength(double pathLength) {
        this.pathLength = pathLength;
    }
}
