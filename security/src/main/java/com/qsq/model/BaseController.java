package com.qsq.model;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author  qsq
 * @data 2019年10月16日21:07:34
 * @param <D> service
 * @param <T> 对象
 */
public class BaseController<D extends IService<T>, T> {

    public Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    public D service;


}