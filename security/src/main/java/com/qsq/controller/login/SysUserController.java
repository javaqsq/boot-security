package com.qsq.controller.login;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qsq.controller.request.SysUserRequest;
import com.qsq.enums.ExceptionEnum;
import com.qsq.enums.ResultEnum;
import com.qsq.model.ResultResponse;
import com.qsq.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.qsq.db.bean.SysUser;
import com.qsq.service.SysUserService;
import com.qsq.model.BaseController;

import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author qsq
 * @since 2019-10-16
 */
@RestController
@RequestMapping("/sysUser")
public class SysUserController extends BaseController<SysUserService, SysUser> {

    @GetMapping("/list")
    public ResultResponse list() {
        List<SysUser> list = service.list();
        CollectionUtils.forEach(list, item -> item.setPassword(null));
        return ResultResponse.returnByEnum(ResultEnum.POJO_QUERY_SUCCESS, list);
    }

    @GetMapping("/{id}")
    public ResultResponse get(@PathVariable(value = "id") String id) {
        if (StringUtils.isEmpty(id)) {
            throw ExceptionEnum.REQUEST_ID_MISS.getException();
        }
        SysUser user = service.getById(id);
        return ResultResponse.returnByEnum(ResultEnum.POJO_QUERY_SUCCESS, user);
    }

    @DeleteMapping("/{id}")
    public ResultResponse delete(@PathVariable(value = "id") String id) {
        if (StringUtils.isEmpty(id)) {
            throw ExceptionEnum.REQUEST_ID_MISS.getException();
        }
        service.removeById(id);
        return ResultResponse.returnByEnum(ResultEnum.POJO_DELETE_SUCCESS);
    }

    @PostMapping
    public ResultResponse insert(@Validated @RequestBody SysUserRequest userRequest) {
        SysUser sysUser = SysUserRequest.converter(userRequest);
        List<SysUser> username = service.list(new QueryWrapper<SysUser>().eq("username", sysUser.getUsername()));
        if (CollectionUtils.isNotEmpty(username)) {
            throw ExceptionEnum.EXIST_SAME_USERNAME.getException();
        }
        service.save(sysUser);
        return ResultResponse.returnByEnum(ResultEnum.POJO_INSERT_SUCCESS);
    }

    @PutMapping
    public ResultResponse update(@Validated @RequestBody SysUserRequest userRequest) {
        if (userRequest.getUserId() == null) {
            throw ExceptionEnum.REQUEST_ID_MISS.getException();
        }
        SysUser sysUser = SysUserRequest.converter(userRequest);
        service.updateById(sysUser);
        return ResultResponse.returnByEnum(ResultEnum.POJO_UPDATE_SUCCESS);
    }
}

