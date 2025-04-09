package com.config;

import com.enums.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.response.ApiResponse;
import com.response.StatusResponse;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, java.io.IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        ApiResponse<Object> api = ApiResponse.error(ErrorCode.ACCESS_DENIED, "Access denied");
        String json = objectMapper.writeValueAsString(api);
        response.getWriter().write(json);
    }
}
