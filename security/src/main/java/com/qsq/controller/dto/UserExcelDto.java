package com.qsq.controller.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import java.util.Date;

/**
 * @author QSQ
 * @create 2019/10/28 15:56
 * No, again
 * 〈〉
 */
public class UserExcelDto extends BaseRowModel {

    @ExcelProperty(index = 0, value = "id")
    private Integer userId;

    /**
     * 用户名
     */
    @ExcelProperty(index = 1, value = "用户名")
    private String username;

    @ExcelProperty(index = 2, value = "密码")
    private String password;

    /**
     * 随机盐
     */
    @ExcelProperty(index = 3, value = "随机盐")
    private String salt;

    /**
     * 简介
     */
    @ExcelProperty(index = 4, value = "电话号码")
    private String phone;

    /**
     * 头像
     */
    @ExcelProperty(index = 5, value = "头像")
    private String icon;

    @ExcelProperty(index = 6, value = "性别")
    private Integer sex;

    @ExcelProperty(index = 7, value = "生日")
    private Date birthday;

    @ExcelProperty(index = 8, value = "邮箱")
    private String email;


}