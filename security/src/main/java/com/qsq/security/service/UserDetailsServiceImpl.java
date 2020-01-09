package com.qsq.security.service;

import com.google.common.collect.Lists;
import com.qsq.db.bean.SysUser;
import com.qsq.enums.ExceptionEnum;
import com.qsq.security.enums.SecurityEnum;
import com.qsq.service.SysUserService;
import com.qsq.util.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author QSQ
 * @create 2019/10/16 22:04
 * No, again
 * 〈UserDetailService的实现类 , 实现登录逻辑〉
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * @param username 用户登录名
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("@_@ user login by username  {}  ", username);
        if (StringUtils.isEmpty(username)) {
            throw ExceptionEnum.LOGIN_USERNAME_EMPTY.getException();
        }
        SysUser userInfo = sysUserService.getUserInfoByLoginId(username);
        if (DataUtil.isEmpty(userInfo)) {
            throw ExceptionEnum.LOGIN_USER_NOT_EXIST.getException();
        }
        String password = httpServletRequest.getParameter("password");
        String loginType = httpServletRequest.getParameter("loginType");
        if (SecurityEnum.LoginType.PWD.equals(SecurityEnum.LoginType.instance(loginType))) {
            log.info("@_@ user login by password  {}  ", password);
            if (!StringUtils.equals(password, userInfo.getPassword())) {
                throw ExceptionEnum.LOGIN_PASSWORD_ERROR.getException();
            }
        }
        return new User(userInfo.getUsername(), userInfo.getPassword(), Lists.newArrayList());
    }

}