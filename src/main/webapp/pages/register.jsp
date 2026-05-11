<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="com.example.demo.util.LocalizationUtil" %>
<%@ page import="com.example.demo.util.Message" %>

<%
    String lang = LocalizationUtil.getCurrentLanguage(request);
%>

<html>
<head>
    <title><%= LocalizationUtil.getMessage(Message.REGISTER_TITLE, request) %></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .lang-buttons {
            text-align: center;
            margin-bottom: 20px;
        }
        .lang-btn {
            background-color: #3498db;
            color: white;
            border: none;
            padding: 5px 15px;
            margin: 0 5px;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            font-size: 14px;
        }
        .lang-btn:hover {
            background-color: #2980b9;
        }
        .active-lang {
            background-color: #2c3e50;
        }
    </style>
</head>
<body>

<div class="lang-buttons">
    <a href="${pageContext.request.contextPath}/controller?command=CHANGE_LANGUAGE&lang=ru&page=pages/register.jsp"
       class="lang-btn ${lang == 'ru' ? 'active-lang' : ''}">🇷🇺 Русский</a>
    <a href="${pageContext.request.contextPath}/controller?command=CHANGE_LANGUAGE&lang=en&page=pages/register.jsp"
       class="lang-btn ${lang == 'en' ? 'active-lang' : ''}">🇬🇧 English</a>
</div>

<form action="${pageContext.request.contextPath}/controller" method="post">
    <input type="hidden" name="command" value="ADD_USER"/>
    <h2><%= LocalizationUtil.getMessage(Message.REGISTER_TITLE, request) %></h2>

    <div class="requirements">
        <strong><%= LocalizationUtil.getMessage(Message.LOGIN_REQUIREMENTS, request) %></strong>
        <ul>
            <li>3-20 <%= LocalizationUtil.getMessage(Message.LOGIN_LABEL, request).toLowerCase() %></li>
        </ul>
        <div class="example"><%= LocalizationUtil.getMessage(Message.LOGIN_EXAMPLE, request) %>: user123</div>
    </div>

    <label for="login"><%= LocalizationUtil.getMessage(Message.LOGIN_LABEL, request) %>:</label>
    <input type="text" id="login" name="login"
           value="<c:out value='${param.login}'/>"
           pattern="^[a-zA-Z0-9]{3,20}$"
           title="<%= LocalizationUtil.getMessage(Message.LOGIN_REQUIREMENTS, request) %>">

    <c:if test="${not empty errors.login}">
        <div class="error"><c:out value="${errors.login}"/></div>
    </c:if>

    <div class="requirements">
        <strong><%= LocalizationUtil.getMessage(Message.PASSWORD_REQUIREMENTS, request) %></strong>
    </div>

    <label for="pass"><%= LocalizationUtil.getMessage(Message.PASSWORD_LABEL, request) %>:</label>
    <input type="password" id="pass" name="pass" required
           minlength="8" maxlength="50"
           pattern="^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$"
           title="<%= LocalizationUtil.getMessage(Message.PASSWORD_REQUIREMENTS, request) %>">

    <c:if test="${not empty errors.password}">
        <div class="error"><c:out value="${errors.password}"/></div>
    </c:if>

    <div class="requirements">
        <strong><%= LocalizationUtil.getMessage(Message.EMAIL_REQUIREMENTS, request) %></strong>
    </div>

    <label for="email"><%= LocalizationUtil.getMessage(Message.EMAIL_LABEL, request) %>:</label>
    <input type="email" id="email" name="email"
           value="<c:out value='${param.email}'/>" required
           title="<%= LocalizationUtil.getMessage(Message.EMAIL_REQUIREMENTS, request) %>">

    <c:if test="${not empty errors.email}">
        <div class="error"><c:out value="${errors.email}"/></div>
    </c:if>

    <div style="margin: 10px 0;">
        <input type="checkbox" id="showPass">
        <label for="showPass">👁️ <%= LocalizationUtil.getMessage(Message.SHOW_PASSWORD, request) %></label>
    </div>

    <input type="submit" value="<%= LocalizationUtil.getMessage(Message.REGISTER_BUTTON, request) %>"/>

    <c:if test="${not empty message}">
        <div class="message success"><c:out value="${message}"/></div>
    </c:if>

    <c:if test="${not empty errorMessage}">
        <div class="message error"><c:out value="${errorMessage}"/></div>
    </c:if>
</form>

<div style="text-align: center; margin-top: 15px;">
    <a href="${pageContext.request.contextPath}/index.jsp">← <%= LocalizationUtil.getMessage(Message.BACK_TO_LOGIN, request) %></a>
</div>

<script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>