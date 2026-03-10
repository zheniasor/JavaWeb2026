<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP-tasks</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/script.js"></script>
</head>
<body>

<br/>
<form action="controller" onsubmit="return validateForm()">
    <input type="hidden" name="command" value="login"/>
    Логин: <input type="text" name="login" value="" />
    <br/>
    Пароль: <input type="password" name="pass" id="pass" value=""/>
    <br/>
    <div style="margin: 10px 0;">
        <input type="checkbox" id="showPass">
        <label for="showPass">Показать пароль</label>
    </div>
    <input type="submit" name="sub" value="Push" />
    <br/>
    ${login_msg}
</form>

<a href="${pageContext.request.contextPath}/pages/register.jsp">Регистрация нового пользователя</a>

</body>
</html>