package com.rxr.store.web.common;

import com.rxr.store.web.common.dto.RestCode;
import com.rxr.store.web.common.dto.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHanlder {
    @ExceptionHandler(value = Throwable.class)
    @ResponseBody
    public RestResponse<Object> handler(HttpServletRequest request, HttpServletResponse resp, Throwable throwable){
        log.info(request.getRequestURL().toString());
        log.error(throwable.getMessage(),throwable);
        RestCode restCode = Exception2CodeRepo.getCode(throwable);
        RestResponse<Object> response = new RestResponse<>(restCode.code,restCode.msg);
        if(restCode.compareTo(RestCode.UNKNOWN_ERROR) == 0) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return response;
    }

}
