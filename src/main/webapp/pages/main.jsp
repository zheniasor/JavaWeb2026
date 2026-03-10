<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Главная страница</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="welcome">
    <h1>Добро пожаловать!</h1>
    <p>Hello, ${user}!</p>

    <form action="${pageContext.request.contextPath}/controller" method="get">
        <input type="hidden" name="command" value="logout">
        <button type="submit" class="logout-btn">Выйти</button>
    </form>
</div>
</body>
</html>