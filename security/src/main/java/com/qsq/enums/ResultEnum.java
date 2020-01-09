package com.qsq.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author qsq
 * @date 2019年10月16日17:27:35
 */
@Getter
@AllArgsConstructor
public enum ResultEnum {

    /**
     * 一般成功返回
     */
    POJO_INSERT_SUCCESS("200", "新增成功"),
    POJO_UPDATE_SUCCESS("200", "更新成功"),
    POJO_DELETE_SUCCESS("200", "删除成功"),
    POJO_QUERY_SUCCESS("200", "查询成功"),
    OPERATE_SUCCESS("200", "操作成功"),


    /**
     * 一般返回失败
     */
    POJO_INSERT_FAIL("500", "新增失败"),
    POJO_UPDATE_FAIL("500", "更新失败"),
    POJO_DELETE_FAIL("500", "删除失败"),
    POJO_QUERY_FAIL("500", "查询失败"),
    OPERATE_FAIL("500", "操作失败"),
    SYS_ERROR("500", "未知异常");

    private String code;
    private String value;


}
