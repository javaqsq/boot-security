package com.qsq.controller.login;

import com.qsq.model.ResultResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

/**
 * @author QSQ
 * @create 2019/10/18 9:23
 * No, again
 * 〈〉
 */
@RestController
public class LoginController {

    @GetMapping("/me")
    public ResultResponse me(HttpServletRequest request, Authentication user) throws Exception {
        String tokenInfo = StringUtils.substringAfter(request.getHeader("Authorization"), "bearer ");
        Claims body = Jwts.parser().setSigningKey("qsq".getBytes("UTF-8")).parseClaimsJws(tokenInfo).getBody();
        return ResultResponse.success(user);
    }


}