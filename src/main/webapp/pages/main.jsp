<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="com.example.demo.util.LocalizationUtil" %>
<%@ page import="com.example.demo.util.Message" %>

<%
    String lang = LocalizationUtil.getCurrentLanguage(request);
%>

<html>
<head>
    <title><%= LocalizationUtil.getMessage(Message.WELCOME, request) %></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .avatar {
            width: 80px;
            height: 80px;
            border-radius: 50%;
            object-fit: cover;
            margin-bottom: 10px;
            border: 2px solid #3498db;
        }
        .welcome {
            text-align: center;
        }
        .button-group {
            display: flex;
            flex-direction: column;
            gap: 15px;
            align-items: center;
            margin-top: 20px;
        }
        .edit-btn {
            background-color: #3498db;
            color: white;
            padding: 10px 20px;
            text-decoration: none;
            border-radius: 4px;
            display: inline-block;
        }
        .admin-btn {
            background-color: #e67e22;
            color: white;
            padding: 10px 20px;
            text-decoration: none;
            border-radius: 4px;
            display: inline-block;
        }
        .logout-btn {
            background-color: #e74c3c;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
        }
        .logout-btn:hover {
            background-color: #c0392b;
        }
    </style>
</head>
<body>

<div class="welcome">
    <h1><%= LocalizationUtil.getMessage(Message.WELCOME, request) %></h1>

    <c:if test="${not empty userAvatar}">
        <img src="${pageContext.request.contextPath}/${userAvatar}" alt="Avatar" class="avatar"/>
    </c:if>
    <c:if test="${empty userAvatar}">
        <div class="avatar" style="background-color: #ccc; display: inline-flex; align-items: center; justify-content: center;">📷</div>
    </c:if>

    <p><%= LocalizationUtil.getMessage(Message.HELLO, request) %>, <c:out value="${user}"/>!</p>

    <div class="button-group">
        <a href="${pageContext.request.contextPath}/pages/editProfile.jsp" class="edit-btn"><%= LocalizationUtil.getMessage(Message.EDIT_TITLE, request) %></a>

        <c:if test="${userRole == 'ADMIN'}">
            <a href="${pageContext.request.contextPath}/controller?command=ADMIN_PANEL" class="admin-btn"><%= LocalizationUtil.getMessage(Message.ADMIN_TITLE, request) %></a>
        </c:if>

        <form action="${pageContext.request.contextPath}/controller" method="get">
            <input type="hidden" name="command" value="LOGOUT">
            <button type="submit" class="logout-btn"><%= LocalizationUtil.getMessage(Message.LOGOUT, request) %></button>
        </form>
    </div>
</div>
</body>
</html>