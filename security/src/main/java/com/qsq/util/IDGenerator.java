package com.qsq.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

/**
 * @author QSQ
 * @create 2019/4/12 13:46
 * No, again
 * 〈 随机数生成器 〉
 */
public class IDGenerator {

    /**
     * 生成随机UUID
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成数字
     *
     * @param count
     * @return
     */
    public static String getNumUUID(int count) {
        return RandomStringUtils.randomNumeric(count);
    }


}