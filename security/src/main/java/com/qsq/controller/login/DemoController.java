package com.qsq.controller.login;

import com.qsq.db.bean.SysUser;
import com.qsq.model.ResultResponse;
import com.qsq.properties.SecurityProperties;
import com.qsq.service.SysUserService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author QSQ
 * @create 2019/10/16 14:11
 * No, again
 * 〈〉
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("")
    public String demo() {
        return "demo";
    }

    @GetMapping("/excel")
    public ResultResponse excelToPDF(HttpServletRequest request, HttpServletResponse response) {
        List<SysUser> list = sysUserService.list();


        return ResultResponse.success();
    }
}