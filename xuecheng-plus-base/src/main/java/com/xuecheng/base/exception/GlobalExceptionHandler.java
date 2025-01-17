package com.xuecheng.base.exception;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
@Api(value = "全局异常捕捉",tags = "全局异常捕捉")
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus
    public RestErrorResponse customException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<String> errors = new ArrayList<>();
        bindingResult.getFieldErrors().stream().forEach(item ->
            errors.add(item.getDefaultMessage())
        );

        String errMessage = StringUtils.join(errors, ",");

        log.error("系统异常：{}",errMessage);
        return new RestErrorResponse(errMessage);
    }

//    @ExceptionHandler(Exception.class)
//    @ResponseStatus
//    public RestErrorResponse exception(Exception e) {
//        log.error("系统异常：{}",e.getMessage(),e);
//        return new RestErrorResponse(CommonError.UNKOWN_ERROR.getErrMessage());
//    }

    @ExceptionHandler(XueChengPlusException.class)
    @ResponseStatus
    public RestErrorResponse exception(XueChengPlusException e) {
        log.error("系统异常：{}",e.getMessage(),e);
        return new RestErrorResponse(e.getErrMessage());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse exception(Exception e) {
        log.error("【系统异常】{}",e.getMessage());

        e.printStackTrace();
        if (e.getMessage().equals("不允许访问")) {
            return new RestErrorResponse("没有操作此功能的权限");
        }
        return new RestErrorResponse(CommonError.UNKOWN_ERROR.getErrMessage());
    }

}
