<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="com.example.demo.util.LocalizationUtil" %>
<%@ page import="com.example.demo.util.Message" %>

<%
    String lang = LocalizationUtil.getCurrentLanguage(request);
%>

<!DOCTYPE html>
<html>
<head>
    <title><%= LocalizationUtil.getMessage(Message.LOGIN_TITLE, request) %></title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            flex-direction: column;
            padding: 20px;
            color: #333;
        }

        h2 {
            color: #2c3e50;
            border-bottom: 2px solid #3498db;
            padding-bottom: 10px;
            margin-bottom: 20px;
        }

        /* Кнопки переключения языка */
        .lang-buttons {
            position: fixed;
            top: 20px;
            right: 20px;
            display: flex;
            gap: 10px;
            z-index: 1000;
        }

        .lang-btn {
            background: rgba(255,255,255,0.95);
            color: #333;
            padding: 8px 20px;
            border-radius: 25px;
            text-decoration: none;
            font-weight: 600;
            font-size: 14px;
            transition: all 0.3s;
            box-shadow: 0 2px 5px rgba(0,0,0,0.2);
            border: none;
            cursor: pointer;
            display: inline-block;
        }

        .lang-btn:hover {
            background: white;
            transform: translateY(-2px);
            text-decoration: none;
        }

        .active-lang {
            background: #667eea;
            color: white;
        }

        /* Стили для форм */
        form {
            background-color: white;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
            width: 100%;
            max-width: 400px;
        }

        label {
            display: block;
            margin-bottom: 5px;
            color: #555;
            font-weight: 500;
            font-size: 14px;
        }

        input[type="text"],
        input[type="password"],
        input[type="email"] {
            width: 100%;
            padding: 12px;
            margin-bottom: 20px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
            transition: border-color 0.3s;
            box-sizing: border-box;
        }

        input[type="text"]:focus,
        input[type="password"]:focus,
        input[type="email"]:focus {
            outline: none;
            border-color: #667eea;
        }

        input[type="submit"] {
            background-color: #3498db;
            color: white;
            padding: 12px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            width: 100%;
            font-weight: 600;
            transition: background-color 0.3s;
        }

        input[type="submit"]:hover {
            background-color: #2980b9;
        }

        /* Стили для ссылок */
        a {
            color: #3498db;
            text-decoration: none;
            display: inline-block;
            margin-top: 15px;
            font-size: 14px;
        }

        a:hover {
            text-decoration: underline;
        }

        /* Стили для сообщений */
        .message {
            padding: 12px;
            margin: 15px 0;
            border-radius: 5px;
            text-align: center;
            animation: slideDown 0.5s ease;
        }

        .success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        @keyframes slideDown {
            from {
                opacity: 0;
                transform: translateY(-20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        /* Стили для чекбокса */
        .checkbox-group {
            margin: 15px 0;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .checkbox-group label {
            margin: 0;
            cursor: pointer;
        }

        .checkbox-group input {
            width: auto;
            margin: 0;
            cursor: pointer;
        }

        /* Стили для требований и подсказок */
        .requirements {
            background-color: #f8f9fa;
            border-left: 3px solid #3498db;
            padding: 10px;
            margin: 10px 0;
            border-radius: 4px;
            font-size: 12px;
            color: #666;
        }

        .hint {
            font-size: 0.85em;
            color: #666;
            margin-top: 2px;
            margin-bottom: 10px;
        }

        .example {
            color: #3498db;
            font-style: italic;
            font-size: 12px;
            margin-top: 5px;
        }

        /* Стили для страницы приветствия */
        .welcome {
            text-align: center;
            background-color: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            max-width: 500px;
            margin: 50px auto;
        }

        .welcome h1 {
            color: #2c3e50;
        }

        button, .logout-btn {
            background-color: #e74c3c;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
            text-decoration: none;
            display: inline-block;
            margin-top: 20px;
            transition: background-color 0.3s;
        }

        button:hover, .logout-btn:hover {
            background-color: #c0392b;
            text-decoration: none;
        }
    </style>
</head>
<body>

<div class="lang-buttons">
    <a href="${pageContext.request.contextPath}/controller?command=CHANGE_LANGUAGE&lang=ru&page=index.jsp"
       class="lang-btn ${lang == 'ru' ? 'active-lang' : ''}">🇷🇺 Русский</a>
    <a href="${pageContext.request.contextPath}/controller?command=CHANGE_LANGUAGE&lang=en&page=index.jsp"
       class="lang-btn ${lang == 'en' ? 'active-lang' : ''}">🇬🇧 English</a>
</div>

<form action="${pageContext.request.contextPath}/controller" method="post">
    <input type="hidden" name="command" value="LOGIN"/>
    <h2><%= LocalizationUtil.getMessage(Message.LOGIN_TITLE, request) %></h2>

    <label for="login"><%= LocalizationUtil.getMessage(Message.LOGIN_LABEL, request) %>:</label>
    <input type="text" id="login" name="login" value="<c:out value='${param.login}'/>" required />

    <label for="pass"><%= LocalizationUtil.getMessage(Message.PASSWORD_LABEL, request) %>:</label>
    <input type="password" id="pass" name="pass" required />

    <div class="checkbox-group">
        <input type="checkbox" id="showPass">
        <label for="showPass"> <%= LocalizationUtil.getMessage(Message.SHOW_PASSWORD, request) %></label>
    </div>

    <input type="submit" value="<%= LocalizationUtil.getMessage(Message.LOGIN_BUTTON, request) %>" />

    <c:if test="${not empty errorMessage}">
        <div class="message error"><c:out value="${errorMessage}"/></div>
    </c:if>
    <c:if test="${not empty message}">
        <div class="message success"><c:out value="${message}"/></div>
    </c:if>
</form>

<div style="text-align: center;">
    <a href="${pageContext.request.contextPath}/pages/register.jsp">
        <%= LocalizationUtil.getMessage(Message.REGISTER_LINK, request) %>
    </a>
</div>

<script src="${pageContext.request.contextPath}/js/script.js"></script>

</body>
</html>