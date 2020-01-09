package com.qsq.service;

import com.qsq.db.bean.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author qsq
 * @since 2019-10-16
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 根据用户名登录
     *
     * @param username
     * @return
     */
    SysUser getUserInfoByLoginId(String username);
}
