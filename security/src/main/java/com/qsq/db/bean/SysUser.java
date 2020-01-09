package com.qsq.db.bean;

import com.qsq.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author qsq
 * @since 2019-10-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SysUser extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键ID
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /**
     * 用户名
     */
    private String username;

    private String password;

    /**
     * 随机盐
     */
    private String salt;

    /**
     * 简介
     */
    private String phone;

    /**
     * 头像
     */
    private String icon;

    private Integer sex;

    private Date birthday;

    private String email;

    /**
     * 0-正常，9-锁定
     */
    private String lockFlag;


}
