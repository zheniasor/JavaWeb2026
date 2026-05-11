package com.example.demo.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LocalizationUtil {

    private static final String COOKIE_LANG_NAME = "app_language";
    private static final String SESSION_LANG_KEY = "language";
    private static final String DEFAULT_LANG = "ru";

    public static String getCurrentLanguage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String sessionLang = (String) session.getAttribute(SESSION_LANG_KEY);
            if (sessionLang != null && (sessionLang.equals("ru") || sessionLang.equals("en"))) {
                return sessionLang;
            }
        }

        String langParam = request.getParameter("lang");
        if (langParam != null && (langParam.equals("ru") || langParam.equals("en"))) {
            return langParam;
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (COOKIE_LANG_NAME.equals(cookie.getName())) {
                    String cookieLang = cookie.getValue();
                    if (cookieLang.equals("ru") || cookieLang.equals("en")) {
                        return cookieLang;
                    }
                }
            }
        }

        String acceptLanguage = request.getHeader("Accept-Language");
        if (acceptLanguage != null && acceptLanguage.startsWith("en")) {
            return "en";
        }

        return DEFAULT_LANG;
    }

    public static void setLanguage(HttpServletRequest request, HttpServletResponse response, String lang) {
        if (lang == null || (!lang.equals("ru") && !lang.equals("en"))) {
            return;
        }

        HttpSession session = request.getSession(true);
        session.setAttribute(SESSION_LANG_KEY, lang);
        session.setAttribute("currentLang", lang);

        Cookie cookie = new Cookie(COOKIE_LANG_NAME, lang);
        cookie.setMaxAge(30 * 24 * 60 * 60);
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        response.addCookie(cookie);

        request.setAttribute("currentLang", lang);
    }

    public static String getMessage(Message message, HttpServletRequest request) {
        String language = getCurrentLanguage(request);
        return message.get(language);
    }


}