package com.aop;

import com.enums.ErrorCode;
import com.response.ApiResponse;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@AllArgsConstructor
public class ApiResponseAspect {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ApiResponseAspect.class);

    @Around("execution(* com.controller..*(..))")
    public Object wrapApiResponse(ProceedingJoinPoint joinPoint) {
        try {
            Object result = joinPoint.proceed();

//            logger.info("Result from controller: {}", result);

            if (result == null) {
                return ApiResponse.success("No content");
            }

            if (result instanceof ApiResponse) return result;
            if (result instanceof String) return ApiResponse.successWithMessage(result);
            ApiResponse<?> wrapped = ApiResponse.success(result);
//            logger.info("Wrapped response: {}", wrapped);
            return ApiResponse.success(result);
        } catch (Throwable e) {
            logger.error("Exception in controller method: {}.{}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), e);
            e.printStackTrace(); // <--- THÊM DÒNG NÀY
            return ApiResponse.error(ErrorCode.INTERNAL_ERROR, "INTERNAL_ERROR");
        }
    }
}
