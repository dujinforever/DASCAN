package com.zl;

import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.zhu on 2015/8/31.
 */
public class DBScan {

    //����һ����:���ϴ�
    public class Cluster{
        private List<Tuple2<String, Long>> dataPoints = new ArrayList();//����һ�������д�ż����еĵ�
        public List<Tuple2<String, Long>> getDataPoints(){ return dataPoints; } //��ȡһ�������еĸ�����
        public void setDataPoints(List<Tuple2<String, Long>> dataPoints) { this.dataPoints = dataPoints;}
    }

    public void doDBScanAnalysis(List<Tuple2<String, Long>> dataPoints, long radius, int minPts){

       List<Cluster> clusterList = new ArrayList();//����������ض���

       for(int i=0; i<dataPoints.size(); i++){
           Tuple2<String, Long> dp =  dataPoints.get(i);
           List<Tuple2<String, Long>> arrivablePoints = isKeyAndReturnPoints(dp, dataPoints, radius, minPts);
           if(arrivablePoints != null){          //����Ǻ��Ķ���
               Cluster tempCluster = new Cluster();
               tempCluster.setDataPoints(arrivablePoints);
               clusterList.add(tempCluster);     //����һ�����
           }
       }
       for(int i=0;i<clusterList.size();i++){    //�����м��ϣ����ܺϲ��ĺϲ���һ��
           for(int j=0;j<clusterList.size();j++){
               if(i!=j){
                   Cluster clusterA = clusterList.get(i);
                   Cluster clusterB = clusterList.get(j);

                   List<Tuple2<String, Long>> dpsA = clusterA.getDataPoints();
                   List<Tuple2<String, Long>> dpsB = clusterB.getDataPoints();

                   boolean flag=mergeList(dpsA,dpsB);
                   if(flag){                     //����������Ϻϲ��ˣ������õ�ǰ����jΪ�ϲ���ļ���
                       clusterList.set( j, new Cluster());
                   }
               }
           }
       }
       displayCluster(clusterList);
    }

    //���������
    public void displayCluster(List<Cluster> clusterList){
        if(clusterList != null){
            //for(Cluster tempCluster:clusterList)
            int NotNullClusterCount = 0;
            for(int i=0; i<clusterList.size(); i++){
                if(clusterList.get(i).getDataPoints()!=null&&clusterList.get(i).getDataPoints().size()>0){
                    System.out.print("The " + (++NotNullClusterCount) + "st Cluster " + clusterList.get(i).getDataPoints().size() + ": ");
                    for(Tuple2<String, Long> dp:clusterList.get(i).getDataPoints()){
                        System.out.print("[" + dp._1() + "," + dp._2() + " ]; ");
                    }
                System.out.println("");
                }
            }
            System.out.println("Total Number of Clusters:"+ NotNullClusterCount);
        }
    }

    //�жϵ�ǰԪ���Ƿ�Ϊ���Ķ���������򷵻�����Ϊ���Ķ���ֱ���ܶȿɴ��ļ��ϣ������򷵻ؿ�
    private List<Tuple2<String, Long>> isKeyAndReturnPoints(Tuple2<String, Long> dataPoint, List<Tuple2<String, Long>> dataPoints, long radius, int minPts) {
        List<Tuple2<String, Long>> arrivablePoints = new ArrayList(); //�����洢����ֱ���ܶȿɴ����

        for(Tuple2<String, Long> dp:dataPoints){//���������и����㣬�ж��Ƿ�Ϊ��ǰ�����ָ���ܶȿɴ��
            double distance = getDistance(dataPoint,dp);
            if(distance <= radius){             //����С�ڸ���ֵ��Ϊֱ���ܶȿɴ��
                arrivablePoints.add(dp);
            }
        }
        if(arrivablePoints.size() >= minPts){//�жϵ�ǰ�����ֱ���ܶȿɴ���Ƿ���ڵ��ڸ���ֵ
            return arrivablePoints;           //�Ǿͷ��غ��Ķ���
        }
        return null;                           //���Ǿͷ���null
    }

    //������������֮��ľ��루����Ӧ����ʱ����Ϊ����Ķ�����
    private long getDistance(Tuple2<String, Long> dp1, Tuple2<String, Long> dp2) {
        long num1 = dp1._2();
        long num2 = dp2._2();
        return Math.abs(num1 - num2);
    }

    //�ϲ����Ķ�������
    private boolean mergeList(List<Tuple2<String, Long>> dps1,List<Tuple2<String, Long>> dps2){
        boolean flag=false;
        if(dps1==null||dps2==null||dps1.size()==0||dps2.size()==0){
            return flag;
        }
        for(Tuple2<String, Long> dp:dps2){
            if(isContain(dp,dps1)){//�жϼ���2�еĵ��Ƿ��뼯��1�еĵ��ܶ���������flag==true��:(flag==false)
                flag=true;
                break;
            }
        }
        if(flag){                  //�����������ܶ������ĵ�
            for(Tuple2<String, Long> dp:dps2){
                if(!isContain(dp,dps1)){ //��һ��������2�еĵ㣬������ڼ���1��
                    dps1.add(dp);        //�ھͽ��ϲ�������1��
                }
            }
        }
        return flag;
    }

    private boolean isContain(Tuple2<String, Long> dp,List<Tuple2<String, Long>> dps){
        boolean flag=false;
        for(Tuple2<String, Long> tempDp:dps){
            if(dp._1().equals(tempDp._1())){
                flag=true;
                break;
            }
        }
        return flag;
    }
}
