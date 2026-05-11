package com.example.demo.filter;

import com.example.demo.entity.User;
import com.example.demo.exception.DataException;
import com.example.demo.service.UserService;
import com.example.demo.service.impl.UserServiceImpl;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@WebFilter(urlPatterns = {"/*"}, filterName = "SessionFilter")
public class SessionFilter implements Filter {

    private static final Logger LOGGER = LogManager.getLogger(SessionFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession(false);

        if (session != null) {
            String currentUser = (String) session.getAttribute("user");
            if (currentUser != null) {
                try {
                    UserService userService = UserServiceImpl.getInstance();
                    Optional<User> userOpt = userService.findByLogin(currentUser);
                    if (userOpt.isPresent()) {
                        User user = userOpt.get();

                        session.setAttribute("userRole", user.getRole());
                        session.setAttribute("userEmail", user.getEmail());
                        session.setAttribute("userAvatar", user.getAvatarPath());
                        session.setAttribute("userConfirmed", user.isConfirmed());

                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("id", user.getId());
                        userInfo.put("login", user.getLogin());
                        userInfo.put("email", user.getEmail());
                        userInfo.put("role", user.getRole());
                        userInfo.put("avatar", user.getAvatarPath());
                        userInfo.put("confirmed", user.isConfirmed());
                        session.setAttribute("userInfo", userInfo);
                    }
                } catch (DataException e) {
                    LOGGER.error("Error updating session user info", e);
                }
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("SessionFilter initialized");
    }

    @Override
    public void destroy() {
        LOGGER.info("SessionFilter destroyed");
    }
}