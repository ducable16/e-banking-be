package com.aop;

import com.enums.ErrorCode;
import com.response.ApiResponse;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@AllArgsConstructor
public class ApiResponseAspect {

    @Around("execution(* com.controller..*(..))")
    public Object wrapApiResponse(ProceedingJoinPoint joinPoint) {
        try {
            Object result = joinPoint.proceed();

            if (result == null) {
                return ApiResponse.success("No content");
            }

            if (result instanceof ApiResponse) return result;
            if (result instanceof String) return ApiResponse.successWithMessage(result);

            System.out.println("Result class: " + result.getClass().getName());
            return ApiResponse.success(result);
        } catch (Throwable e) {
            e.printStackTrace();
            return ApiResponse.error(ErrorCode.INTERNAL_ERROR, "INTERNAL_ERROR");
        }
    }
}
