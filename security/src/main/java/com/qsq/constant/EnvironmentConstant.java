package com.qsq.constant;

import java.util.Objects;

/**
 * @author QSQ
 * @create 2019/10/11 14:32
 * No, again
 * 〈环境常量〉
 */
public class EnvironmentConstant {

    public final static String ENVIRONMENT_DEV = "dev";
    public final static String ENVIRONMENT_PRODUCTION = "pro";


    public static boolean isDevEnv(String environment) {
        return Objects.equals(environment, ENVIRONMENT_DEV);
    }

}