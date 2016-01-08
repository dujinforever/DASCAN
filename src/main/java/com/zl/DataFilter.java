package com.zl;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mr.zhu on 2015/8/25.
 * ����־���ݽ��й��ˣ����Ϻ�����ļ�¼���ֳɸ����ֶβ���ȡ����
 */
public class DataFilter implements Serializable {
    //Ϊָ����ϵͳ���һ򴴽�һ��logger
    private static final Logger logger = Logger.getLogger("Access");

    private String onlineDetailID; //������ϸID��eg:2c929293466b97a6014754607e457d68
    private String userID;         //�˺ţ�eg:U201215025
    private String userMAC;        //MAC��ַ��eg:A417314EEA7B
    private String userIPv4;       //�û�IPv4��ַ��eg:10.12.49.26
    private String loginTime;        //����ʱ�䣬eg:2014-07-20 22:44:18.540000000
    private String logoutTime;       //����ʱ�䣬eg:2014-07-20 23:10:16.540000000
    private long onlineSeconds;  //����ʱ�䣬eg:1558
    private int accessType;     //�������ͣ�eg:15��1���߿ͻ�����֤��3����Web��֤��12����MAC�޸���֤��13����AUTO�޸���֤��15����Web��֤��
    private String userTemplateID; //ģ�棬eg:��������̬IPģ��/�о�����̬IPģ��
    private String packageName;    //�ײͣ�eg:100Ԫÿ����/20Ԫÿ��/1Ԫÿ��
    private String serviceSuffix;  //����eg:internet

    private DataFilter(String onlineDetailID, String userID, String userMAC, String userIPv4, String loginTime, String logoutTime,
                        String onlineSeconds, String accessType, String userTemplateID, String packageName, String serviceSuffix){
        this.onlineDetailID = onlineDetailID;
        this.userID = userID;
        this.userMAC = userMAC;
        this.userIPv4 = userIPv4;
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
        this.onlineSeconds = Long.parseLong(onlineSeconds);
        this.accessType = Integer.parseInt(accessType);
        this.userTemplateID = userTemplateID;
        this.packageName = packageName;
        this.serviceSuffix = serviceSuffix;
    }

    public String getOnlineDetailID(){ return onlineDetailID; }

    public String getUserID(){ return userID; }

    public String getUserMAC(){ return userMAC; }

    public String getUserIPv4(){ return userIPv4; }

    public String getLoginTime(){ return loginTime; }

    public String getLogoutTime(){ return logoutTime; }

    public Long getOnlineSeconds(){ return onlineSeconds; }

    public int getAccessType(){ return accessType; }

    public String getUserTemplateID(){ return userTemplateID; }

    public String getPackageName(){ return packageName; }

    public String getServiceSuffix(){ return serviceSuffix; }

    // Example Apache log line:����־��ʽ��
    // 1��2c929293466b97a6014754607e457d68,������ϸID
    // 2��U201215025,�˺�
    // 3��A417314EEA7B,MAC��ַ
    // 4��10.12.49.26,�û�IPv4�����ַ
    // 5��2014-07-20
    // 6��22:44:18.540000000,�û�����ʱ��
    // 7��2014-07-20
    // 8��23:10:16.540000000,�û�����ʱ��
    // 9��1558,�û�����ʱ��
    // 10��15,�������ͣ�1���߿ͻ�����֤��3����Web��֤��12����MAC�޸���֤��13����AUTO�޸���֤��15����Web��֤��
    // 11����������̬IPģ��,ģ��
    // 12��100Ԫÿ����,�ײ�
    // 13��internet������
    private static final String LOG_ENTRY_PATTERN =
            "^(\\w+),(\\w+),(\\w+),(\\S+),(\\S+) (\\S+),(\\S+) (\\S+),(\\d+),(\\d+),(\\S+),(\\S+),(\\w+)";
    //������ʽ��һ�������Ͷ�����һ���飬�Ա����Matcher�ദ��
    //"\s"ƥ���κβ��ɼ��ַ��������ո��Ʊ������ҳ���ȵȡ��ȼ���[ \f\n\r\t\v]
    //"\S"ƥ�����ⲻ�ǿհ׷����ַ�
    //"\w"ƥ������»��ߵ��κε����ַ������Ƶ����ȼ��ڡ�[A-Za-z0-9_]���������"����"�ַ�ʹ��Unicode�ַ���

    //��������ʽ����Pttern����
    private static final Pattern PATTERN = Pattern.compile(LOG_ENTRY_PATTERN);

    public static DataFilter parseFromLogLine(String dataline) {
        //����־�ļ�����ƥ��
        Matcher m = PATTERN.matcher(dataline);

        if (!m.find()) {//����в�ƥ������������
            //��־����ALL��ʾ����������Ϣ����־��¼
            logger.log(Level.ALL, "Cannot parse logline" + dataline);
            throw new RuntimeException("Error parsing logline");
        }

        //���ؼ�¼�ĸ����ֶ�
        return new DataFilter(m.group(1), m.group(2), m.group(3), m.group(4), m.group(6),
                m.group(8), m.group(9), m.group(10), m.group(11),m.group(12), m.group(13));
    }
}
