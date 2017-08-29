package com.a2017hkt15.sortaddr;

import android.util.Log;

/**
 * Created by 함상혁입니다 on 2017-08-21.
 */

public class CalcPath {
    final int MAX_NODE=10;
    int nodeNum;
    double min;
    int[] minPath;
    double[][] map;
    boolean[][] priority;
    int start;
    int destination; // if -1 , it doesn't have specific desti.

    public CalcPath()
    {
        nodeNum = 0;
        start=0;
        destination = -1;
        min=987654321;
        minPath=new int[MAX_NODE];
        map=new double[MAX_NODE][MAX_NODE];
        priority=new boolean[MAX_NODE][MAX_NODE];
    }

    public CalcPath(int start,int nodeNum,double[][] map,int dest,boolean[][] priority)
    {
        this();
        this.start=start;
        this.nodeNum= nodeNum;
        this.destination=dest;
        this.map=map;
        this.priority=priority;
    }

    public PathInfo pathCalc()
    {
        PathInfo ret=new PathInfo();

        Log.d("ssss","calc start, destination : "+destination+", strat :"+start+", num : "+nodeNum);
        //Log.d("ssss","map : "+map[0][0]+","+map[0][1]+","+map[0][2]);
        preCalcDfs(1,new int[10],0);
        Log.d("ssss","calc finish");

        //Log.d("ssss",minPath[0]+","+minPath[1]+","+minPath[2]+","+minPath[3]+","+minPath[4]);

        if(min==987654321)//존재할수없는 priority
            return null;

        ret.setPathLength(min);
        ret.setPathRoute(minPath);

        return ret;
    }

    public double preCalc(int num, int[] ver, double length)
    {
        int[] v=new int[MAX_NODE];
        double ret = length;

        for (int i = 0; i < nodeNum; i++)
        {
            v[i]=1;
        }
        for (int i = 0; i < num; i++)
        {
            v[i]=0;
        }


        for (int i = 0; i < nodeNum; i++)
        {
            if (v[i]==1)
            {
                double m = map[i][0];
                for (int j = 0; j < nodeNum; j++)
                {
                    if (map[i][j] < m)
                    {
                        m = map[i][j];
                    }
                }
                ret += m;
            }
        }
        if(destination==start)
        {
            double m=987654321.0;
            for(int i=0;i<num;i++)
            {
                if(map[start][i]<m)
                {
                    m=map[start][i];
                }
            }
            ret+=m;
        }

        return ret;
    }

    private double preCalcDfs(int num, int[] ver, double length)
    {
        //도착지가 정해졌는지 않됬는지
        if (destination==-1 && num == nodeNum)//일단 도착지 없음
        {
            if (min > length)
            {
                min = length;
                for (int i = 0; i < nodeNum; i++)
                {
                    minPath[i]=ver[i];
                }
            }
            return 1;
        }
        else if(destination==start && num==nodeNum)
        {

            length+=map[ver[num-1]][destination];
            Log.d("ssss","end of dfs , length"+length);

            if (min > length)
            {
                min = length;
                for (int i = 0; i < nodeNum; i++)
                {
                    minPath[i]=ver[i];
                }
            }
            return 1;
        }
        else if(destination!=-1&& destination !=start && num==nodeNum-1)
        {
            length+=map[ver[num-1]][destination];

            if (min > length)
            {
                min = length;
                for (int i = 0; i < nodeNum-1; i++)
                {
                    minPath[i]=ver[i];
                }
                minPath[nodeNum-1]=destination;
            }
            return 1;
        }


        if (preCalc(num,ver,length) >= min)
        {
            return -1;
        }




        for (int i = 0; i < nodeNum; i++) {
            boolean flag = true;
            for (int j = 0; j < num; j++) {
                if (i == ver[j] || i == destination) {
                    flag = false;
                    break;
                }
            }
            for(int j=0;j<nodeNum;j++)
            {
                if(priority[i][j])
                {
                    boolean f=false;
                    for(int k=0;k<num;k++)
                    {
                        if(ver[k]==j) {
                            f = true;
                            break;
                        }
                    }
                    if(!f)
                    {
                        flag=false;
                        break;
                    }
                }
            }
            if (flag) {
                ver[num] = i;

                if (length < min) {
                    preCalcDfs(num + 1, ver, length + map[ver[num - 1]][i]);
                }
            }
        }


        return min;
    }
}
