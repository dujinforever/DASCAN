package com.zl;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.List;

/**
 * Created by Mr.zhu on 2015/8/26.
 */
public class DataProccess {
    public static void main(String[] args){
        SparkConf conf = new SparkConf().setAppName("Data Proccess").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);

        if(args.length == 0){
            System.out.println("Must specify an access data file");
            System.exit(-1);
        }

        //��textFile������һ���ļ�����RDD,textFile�Ĳ�����һ��path,���path�����ǣ�
        //1�� һ���ļ�·������ʱ��ֻװ��ָ�����ļ�
        //2�� һ��Ŀ¼·������ʱ��ֻװ��ָ��Ŀ¼����������ļ�����������Ŀ¼������ļ���
        //3�� ͨ��ͨ�������ʽ���ض���ļ����߼��ض��Ŀ¼����������ļ�
        String dataFile = args[0];
        JavaRDD<String> dataLines = sc.textFile(dataFile);

        //������־
        JavaRDD<String> filterData = dataLines.filter(Functions.DATA_FILTER).cache();
        //��ȡ��־����
        JavaRDD<DataFilter> accessData = filterData.map(Functions.PARSE_LOG_LINE).cache();

        // ��ȡ��һ��.txt�и���MAC��ַ��������
        List<Tuple2<String, Long>> MAC_total_Count = accessData
                .mapToPair(Functions.GET_MAC_COUNT)
                .reduceByKey(Functions.SUM_REDUCER)
                .take(5000);
        System.out.println("***************************************");
        System.out.println("MAC Count: " + MAC_total_Count);
        System.out.println("***************************************");

        //��ȡ����MAC��ַ�����Ӧ����һ��.txt���ܵ�����ʱ��
        List<Tuple2<String, Long>> MAC_total_Onlinetime = accessData
                .mapToPair(Functions.GET_MAC_ONLINETIME)
                .reduceByKey(Functions.SUM_REDUCER)
                .take(5000);
        System.out.println("***************************************");
        System.out.println("MAC OnlineTime: " + MAC_total_Onlinetime);
        System.out.println("***************************************");

        //�����ܶȾ����㷨����һ���õ��Ľ�����з��ࣨ����������ʱ�����ֵķ������䣬�õ���������ʱ��һ��Ἧ������Щ���䣩
        //�����㷨������������ʱ�䣨��MAC��ַ��������Ǹ�����ʱ�����估�������Ӧ������
        DBScan dbScan = new DBScan();
        dbScan.doDBScanAnalysis(MAC_total_Onlinetime, 3600, 3);//������:(MAC,onlineTime)���뾶(����������ʱ�仮��)����Χ(һ���������������ٸ���)��

        sc.stop();
    }
}
