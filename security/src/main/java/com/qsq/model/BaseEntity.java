package com.qsq.model;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author QSQ
 * @create 2019/9/27 10:03
 * No, again
 * 〈〉
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BaseEntity implements Serializable {

    private Date createTime;
    private String createUser;
    private Date updateTime;
    private String updateUser;
    @TableLogic
    private String delFlag;
    @Version
    private Integer version;

}