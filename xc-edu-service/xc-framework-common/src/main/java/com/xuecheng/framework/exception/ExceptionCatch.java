package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class ExceptionCatch {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);

    //定义map ,配置异常类型对应的错误代码
    private static ImmutableMap<Class<? extends Throwable>, ResultCode> EXCEPTIONS;
    //定义map的builder对象,去构建ImmutableMap
    private static ImmutableMap.Builder<Class<? extends Throwable>, ResultCode> builder = ImmutableMap.builder();

    //捕获此类异常
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult catCustomException(CustomException customException) {
        LOGGER.error("catch exception :{}", customException.getMessage());
        ResultCode resultCode = customException.getResultCode();
        return new ResponseResult(resultCode);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult catException(Exception exception) {
        LOGGER.error("catch exception :{}", exception.getMessage());
        if (EXCEPTIONS == null) {
            EXCEPTIONS = builder.build(); //构建exceptions
        }
        //从exceptions中找异常类型所对应的错误代码,如果找到了,将错误代码响应给用户,如果找不到给用户响应99999异常
        ResultCode resultCode = EXCEPTIONS.get(exception.getClass());
        if (resultCode != null) {
            return new ResponseResult(resultCode);
        }
        return new ResponseResult(CommonCode.SERVER_ERROR);
    }

    static {
        builder.put(HttpMessageNotReadableException.class, CommonCode.INVALID_PARAM);
    }

}
