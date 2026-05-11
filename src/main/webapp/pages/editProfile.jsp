<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="com.example.demo.util.LocalizationUtil" %>
<%@ page import="com.example.demo.util.Message" %>

<%
    String lang = LocalizationUtil.getCurrentLanguage(request);
%>

<html>
<head>
    <title><%= LocalizationUtil.getMessage(Message.EDIT_TITLE, request) %></title>
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

<form action="${pageContext.request.contextPath}/controller" method="post" enctype="multipart/form-data">
    <input type="hidden" name="command" value="UPLOAD_AVATAR"/>
    <h2><%= LocalizationUtil.getMessage(Message.EDIT_TITLE, request) %></h2>

    <c:if test="${not empty userAvatar}">
        <div style="text-align: center; margin-bottom: 15px;">
            <img src="${pageContext.request.contextPath}/${userAvatar}"
                 alt="Avatar" style="width: 100px; height: 100px; border-radius: 50%; object-fit: cover;">
        </div>
    </c:if>

    <label for="avatar"><%= LocalizationUtil.getMessage(Message.EDIT_AVATAR, request) %>:</label>
    <input type="file" id="avatar" name="avatar" accept="image/*"/>

    <label for="login"><%= LocalizationUtil.getMessage(Message.EDIT_NEW_LOGIN, request) %>:</label>
    <input type="text" id="login" name="newLogin"
           value="<c:out value='${user}'/>"
           placeholder="<%= LocalizationUtil.getMessage(Message.LOGIN_REQUIREMENTS, request) %>"/>

    <label for="email"><%= LocalizationUtil.getMessage(Message.EDIT_NEW_EMAIL, request) %>:</label>
    <input type="email" id="email" name="email"
           value="<c:out value='${userEmail}'/>"
           placeholder="<%= LocalizationUtil.getMessage(Message.EMAIL_REQUIREMENTS, request) %>"/>

    <label for="password"><%= LocalizationUtil.getMessage(Message.EDIT_NEW_PASSWORD, request) %>:</label>
    <input type="password" id="password" name="newPassword"
           placeholder="<%= LocalizationUtil.getMessage(Message.EDIT_NEW_PASSWORD, request) %>"/>

    <div style="margin: 10px 0;">
        <input type="checkbox" id="showPass" onclick="togglePassword()">
        <label for="showPass"><%= LocalizationUtil.getMessage(Message.SHOW_PASSWORD, request) %></label>
    </div>

    <input type="submit" value="<%= LocalizationUtil.getMessage(Message.EDIT_UPDATE_BUTTON, request) %>"/>

    <c:if test="${not empty message}">
        <div class="message success"><c:out value="${message}"/></div>
    </c:if>

    <c:if test="${not empty errorMessage}">
        <div class="message error"><c:out value="${errorMessage}"/></div>
    </c:if>
</form>

<div style="text-align: center; margin-top: 15px;">
    <a href="${pageContext.request.contextPath}/pages/main.jsp">← <%= LocalizationUtil.getMessage(Message.BUTTON_BACK, request) %></a>
</div>

<script>
    function togglePassword() {
        let pass = document.getElementById("password");
        pass.type = pass.type === "password" ? "text" : "password";
    }
</script>
</body>
</html>