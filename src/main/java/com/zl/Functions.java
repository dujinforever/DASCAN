package com.zl;

import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mr.zhu on 2015/8/25.
 * ����־���д���
 */
public class Functions {
    public static Function2<Long, Long, Long> SUM_REDUCER =
            new Function2<Long, Long, Long>() {
                @Override
                public Long call(Long a, Long b) throws Exception {
                    return a+b;
                }
            };

    public static class ValueComparator<K, V>
            implements Comparator<Tuple2<K, V>>, Serializable {
        private Comparator<V> comparator;

        public ValueComparator(Comparator<V> comparator) {
            this.comparator = comparator;
        }

        @Override
        public int compare(Tuple2<K, V> o1, Tuple2<K, V> o2) {
            return comparator.compare(o1._2(), o2._2());
        }
    }

    public static class LongComparator
            implements Comparator<Long>, Serializable {
        @Override
        /**
         * ���a����b������һ������;���������ȣ��򷵻�0;���aС��b,����һ������;
         */
        public int compare(Long a, Long b) {
            if (a > b) return 1;
            if (a.equals(b)) return 0;
            return -1;
        }
    }


    public static Comparator<Long> LONG_NATURAL_ORDER_COMPARATOR =
            new LongComparator();

    //����־����
    public static Function<String, Boolean> DATA_FILTER =
            new Function<String, Boolean>() {
                public static final String REGEX =
                        "^(\\w+),(\\w+),(\\w+),(\\S+),(\\S+) (\\S+),(\\S+) (\\S+),(\\d+),(\\d+),(\\S+),(\\S+),(\\w+)";

                public final Pattern PATTERN = Pattern.compile(REGEX);
                public Boolean call(String dataLine) throws Exception{
                    Matcher matcher = PATTERN.matcher(dataLine);
                    return matcher.find();
                }
            };

    //����DataFilter.parseFromLogLine()����־����ƥ�䣬 �ɹ��򷵻ظ����ֶ�ֵ
    public static Function<String, DataFilter> PARSE_LOG_LINE =
            new Function<String, DataFilter>() {
                @Override
                public DataFilter call(String logline) throws Exception {
                    return DataFilter.parseFromLogLine(logline);
                }
            };

    //��ȡ�û�������ϸID
    public static Function<DataFilter, String> GET_ONLINEDETAILID =
            new Function<DataFilter, String>() {
                @Override
                public String call(DataFilter dataFilter) throws Exception {
                    return dataFilter.getOnlineDetailID();
                }
            };

    //��ȡ�û�USERID
    public static Function<DataFilter, String> GET_USERID =
            new Function<DataFilter, String>() {
                @Override
                public String call(DataFilter dataFilter) throws Exception {
                    return dataFilter.getUserID();
                }
            };

    //��ȡ�û�IP��ַ
    public static PairFunction<DataFilter, String, Long> GET_USERIPv4 =
            new PairFunction<DataFilter, String, Long>() {
                @Override
                public Tuple2<String, Long> call(DataFilter dataFilter) throws Exception {
                    return new Tuple2<String, Long>(dataFilter.getUserIPv4(), 1L);
                }
            };

    //��ȡ�û�����ʱ��
    public static Function<DataFilter, Long> GET_ONLINESECONDS =
            new Function<DataFilter, Long>() {
                @Override
                public Long call(DataFilter dataFilter) throws Exception {
                    return dataFilter.getOnlineSeconds();
                }
            };

    //��ȡ�û���������
    public static Function<DataFilter, Integer> GET_ACCESSTYPE =
            new Function<DataFilter, Integer>() {
                @Override
                public Integer call(DataFilter dataFilter) throws Exception {
                    return dataFilter.getAccessType();
                }
            };

    //��ȡ�û�������̬ģ��
    public static Function<DataFilter, String> GET_USERTEMPLATEID =
            new Function<DataFilter, String>() {
                @Override
                public String call(DataFilter dataFilter) throws Exception {
                    return dataFilter.getUserTemplateID();
                }
            };

    //��ȡ�û��ײ�����
    public static Function<DataFilter, String> GET_PACKAGENAME =
            new Function<DataFilter, String>() {
                @Override
                public String call(DataFilter dataFilter) throws Exception {
                    return dataFilter.getPackageName();
                }
            };

    //��ȡ�û���������
    public static Function<DataFilter, String> GET_SERVICESUFFIX =
            new Function<DataFilter, String>() {
                @Override
                public String call(DataFilter dataFilter) throws Exception {
                    return dataFilter.getServiceSuffix();
                }
            };

    //��ȡ�û�MAC��ַ
    public static PairFunction<DataFilter, String, Long> GET_MAC_COUNT =
            new PairFunction<DataFilter, String, Long>() {
                @Override
                public Tuple2<String, Long> call(DataFilter dataFilter) throws Exception {
                    return new Tuple2<String, Long>(dataFilter.getUserMAC(), 1L);
                }
            };

    //��ȡÿ��MAC��ַ��Ӧ������ʱ��
    public static PairFunction<DataFilter, String, Long> GET_MAC_ONLINETIME =
            new PairFunction<DataFilter, String, Long>() {
                @Override
                public Tuple2<String, Long> call(DataFilter dataFilter) throws Exception {
                    return new Tuple2<String, Long>(dataFilter.getUserMAC(), dataFilter.getOnlineSeconds());
                }
            };

    //��ȡ��һ���и�ʱ�������������
//    public static PairFunction<DataFilter, String, Long> GET_TIME_ZONE =
//            new PairFunction<DataFilter, String, Long>() {
//                @Override
//                public Tuple2<String, Long> call(DataFilter dataFilter) throws Exception {
//                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    Date date = dateFormat.parse(dataFilter.getLoginTime());
//                    Calendar calendar = Calendar.getInstance();
//                    calendar.setTime(date);
//                    return new Tuple2<String, Long>(""+calendar.get(Calendar.HOUR_OF_DAY), 1L);
//                }
//            };

    public static Function<Tuple2<String, Long>, Boolean> FILTER_GREATER_10 =
            new Function<Tuple2<String, Long>, Boolean>() {
                @Override
                public Boolean call(Tuple2<String, Long> tuple) throws Exception {
                    return tuple._2() > 10;
                }
            };


    public static Function<Tuple2<String, Long>, String> GET_TUPLE_FIRST =
            new Function<Tuple2<String, Long>, String>() {
                @Override
                public String call(Tuple2<String, Long> tuple) throws Exception {
                    return tuple._1();
                }
            };

}