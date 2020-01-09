package com.qsq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qsq.db.bean.SysUser;
import com.qsq.db.dao.SysUserMapper;
import com.qsq.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author qsq
 * @since 2019-10-16
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public SysUser getUserInfoByLoginId(String username) {
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username).or().eq("phone", username);
        return super.getOne(wrapper);
    }
}
