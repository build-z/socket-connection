package com.xiaolvche.cloudconnection.config;

import com.xiaolvche.cloudconnection.vo.ResponseVO;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * @author liaoxh
 * @create 2021-07-25-13:02
 */

@RestControllerAdvice
public class ResponseVOWrapAdvice implements ResponseBodyAdvice {

    private boolean excludeMethod(String methodName){
        String[] array = {""};
        return Arrays.asList(array).contains(methodName);
    }


    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        if(excludeMethod(returnType.getMethod().getName())){
            return false;
        }
        Annotation[] classAnnotations = returnType.getContainingClass().getAnnotations();
        for(Annotation annotation : classAnnotations){
            if(annotation instanceof RestController){
                return true;
            }
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        if(body instanceof byte[] ||body instanceof ResponseVO || body instanceof ResponseEntity){
            return body;
        }

        return ResponseVO.ok(body);
    }
}

