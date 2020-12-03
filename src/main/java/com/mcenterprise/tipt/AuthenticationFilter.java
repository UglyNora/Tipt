package com.mcenterprise.tipt;

import com.mcenterprise.tipt.controllers.ShiptMateAuthController;
import com.mcenterprise.tipt.data.ShiptMateRepository;
import com.mcenterprise.tipt.models.ShiptMate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class AuthenticationFilter extends HandlerInterceptorAdapter {

    @Autowired
    ShiptMateRepository shiptMateRepository;

    @Autowired
    ShiptMateAuthController shiptMateAuthController;

    private static final List<String> whitelist = Arrays.asList("/login", "/register", "/logout", "/css");

    private static boolean isWhitelisted(String path) {
        for (String pathRoot : whitelist) {
            if (path.startsWith(pathRoot)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException {

        // Don't require sign-in for whitelisted pages
        if (isWhitelisted(request.getRequestURI())) {
            // returning true indicates that the request may proceed
            return true;
        }

        HttpSession session = request.getSession();
       ShiptMate shiptMate = shiptMateAuthController.getUserFromSession(session);

        // The user is logged in
        if (shiptMate != null) {
            return true;
        }

        // The user is NOT logged in
        response.sendRedirect("/login");
        return false;
    }

}
