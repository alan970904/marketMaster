package com.MarketMaster.controller.restock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession(false);

        boolean isLoggedIn = (session != null && session.getAttribute("employee") != null);
        boolean isLoginPage = requestURI.endsWith("/login");
        boolean isLoginAction = requestURI.endsWith("/login") && "POST".equalsIgnoreCase(request.getMethod());
        boolean isStaticResource = requestURI.contains("/resources/");
        boolean isChangePasswordPage = requestURI.endsWith("/changePassword");

        // 允許訪問登錄頁面、登錄操作、靜態資源和修改密碼頁面
        if (isLoggedIn || isLoginPage || isLoginAction || isStaticResource || isChangePasswordPage) {
            return true;
        } else {
            // 如果用戶未登錄，重定向到登錄頁面
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
    }
}