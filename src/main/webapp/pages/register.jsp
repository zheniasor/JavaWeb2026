<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Регистрация</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/script.js"></script>
</head>
<body>

<form action="${pageContext.request.contextPath}/controller" method="post" onsubmit="return validateForm()">
    <input type="hidden" name="command" value="ADD_USER"/>
    <h2>Регистрация нового пользователя</h2>

    <label for="login">Логин:</label>
    <input type="text" id="login" name="login" value="" required/>

    <label for="pass">Пароль:</label>
    <input type="password" id="pass" name="pass" value="" required/>

    <div style="margin: 10px 0;">
        <input type="checkbox" id="showPass">
        <label for="showPass">Показать пароль</label>
    </div>

    <input type="submit" value="Зарегистрироваться"/>

    <% if (request.getAttribute("message") != null) { %>
    <div class="message success">
        <%= request.getAttribute("message") %>
    </div>
    <% } %>
</form>

<div style="text-align: center;">
    <a href="${pageContext.request.contextPath}/index.jsp">На главную</a>
</div>
</body>
</html>