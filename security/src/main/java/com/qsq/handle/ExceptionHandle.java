package com.qsq.handle;

import com.qsq.enums.ExceptionEnum;
import com.qsq.exception.BusinessRuntimeException;
import com.qsq.model.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;

/**
 * @author QSQ
 * @create 2019/10/16 20:30
 * No, again
 * 〈控制层的统一异常返回〉
 */
@ControllerAdvice(basePackages = {"com.qsq"})
@Slf4j
public class ExceptionHandle {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResultResponse system(Exception e) {
        log.error(" 系统错误 ", e);
        return converterResultResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR), e.getMessage());
    }

    /**
     * 404
     */
    @ExceptionHandler(value = MissingPathVariableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResultResponse notFoundException(MissingPathVariableException response) {
        return converterResultResponse(String.valueOf(HttpStatus.BAD_REQUEST), response.getMessage());
    }


    @ExceptionHandler(BusinessRuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResultResponse businessError(BusinessRuntimeException e) {
        log.error(" 自定义异常出错 ", e);
        return converterResultResponse(e.getErrorCode(), e.getMessage());
    }

    private ResultResponse converterResultResponse(String code, String errorMessage) {
        return ResultResponse.fail(code, errorMessage);
    }

}