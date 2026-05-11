<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="com.example.demo.util.LocalizationUtil" %>
<%@ page import="com.example.demo.util.Message" %>

<%
    String lang = LocalizationUtil.getCurrentLanguage(request);
%>

<html>
<head>
    <title><%= LocalizationUtil.getMessage(Message.CONFIRM_TITLE, request) %></title>
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

<div class="welcome">
    <h1><%= LocalizationUtil.getMessage(Message.REGISTER_TITLE, request) %></h1>

    <div class="message success" style="text-align: left;">
        <p><strong>${message}</strong></p>
        <p><%= LocalizationUtil.getMessage(Message.CONFIRM_MESSAGE, request) %></p>
    </div>

    <div style="margin-top: 30px;">
        <a href="${pageContext.request.contextPath}/index.jsp" class="logout-btn"><%= LocalizationUtil.getMessage(Message.BUTTON_GO_HOME, request) %></a>
    </div>
</div>
</body>
</html>