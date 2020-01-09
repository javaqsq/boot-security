package com.qsq.generator;


/**
 * @author QSQ
 * @create 2019/4/12 10:00
 * No, again
 * 〈自动生成文件的配置文件〉
 */
public class CodeProperties {

    public static final String dbUrl = "jdbc:mysql://127.0.0.1:3306/common-template?allowMultiQueries=true&useSSL=false&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&zeroDateTimeBehavior=convertToNull&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true";
    public static final String dbUsername = "root";
    public static final String dbPassWord = "123456";
    public static final String baseControllerClassName ="com.qsq.model.BaseController" ; //控制层的继承类BaseController
    public static final String superEntityClass ="com.qsq.model.BaseEntity" ;
    public static final String packageName = "com.qsq"; //生成后的包名
    public static final String tableName ="sys_user";
    public static final String temPlateController ="templates/controller.java"; //控制层的模板名称
    public static final String[] superEntityColumns ={"create_time","create_user","update_time","update_user","version","del_flag"};
    public static final String LogicDeleteFieldName ="del_flag";

}