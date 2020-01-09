package com.qsq.controller.request;


import com.qsq.db.bean.SysUser;
import com.qsq.util.IDGenerator;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * @author QSQ
 * @create 2019/10/11 19:59
 * No, again
 * 〈〉
 */
@Data
@Builder
public class SysUserRequest implements Serializable {


    private Integer userId ;

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "手机号码不能为空")
    private String phone;

    private String userFace;

    private Integer sex;

    private Date birthday;

    private String email;

    private String lockFlag;

    public static SysUser converter(SysUserRequest userRequest) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userRequest, sysUser);
        sysUser.setSalt(IDGenerator.getUUID());
        sysUser.setSex(userRequest.getSex() == null ? 1 : userRequest.getSex());
        return sysUser;
    }
}