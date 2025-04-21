package kr.hhplus.be.server.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class RequestLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, jakarta.servlet.ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestId = UUID.randomUUID().toString();
        httpRequest.setAttribute("requestId", requestId);

        log.info("[REQUEST] {} {} requestId={}", httpRequest.getMethod(), httpRequest.getRequestURI(), requestId);

        chain.doFilter(request, response);

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        log.info("[RESPONSE] status={} requestId={}", httpResponse.getStatus(), requestId);
    }
}
