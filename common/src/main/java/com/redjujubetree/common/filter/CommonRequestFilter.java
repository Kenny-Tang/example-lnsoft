package com.redjujubetree.common.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class CommonRequestFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(CommonRequestFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 可选：初始化逻辑
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String clientIp = extractClientIp(httpRequest);
            logger.info("Client real IP: {}", clientIp);

            // 设置为 request attribute，可在 Controller 中取出
            httpRequest.setAttribute("clientIp", clientIp);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // 可选：销毁逻辑
    }

    private String extractClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            // 多级代理时，第一个 IP 为真实 IP
            return ip.split(",")[0].trim();
        }

        ip = request.getHeader("X-Real-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        return request.getRemoteAddr();
    }
}
