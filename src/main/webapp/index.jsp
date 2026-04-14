<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP-tasks</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<form action="controller" method="post">
    <input type="hidden" name="command" value="LOGIN"/>
    Логин: <input type="text" name="login" value="<c:out value='${param.login}'/>" required />
    <br/>
    Пароль: <input type="password" name="pass" id="pass" required />
    <br/>
    <div style="margin: 10px 0;">
        <input type="checkbox" id="showPass">
        <label for="showPass">Показать пароль</label>
    </div>
    <input type="submit" name="sub" value="Войти" />
    <br/>
    <c:if test="${not empty errorMessage}">
        <div class="error"><c:out value="${errorMessage}"/></div>
    </c:if>
</form>

<a href="${pageContext.request.contextPath}/pages/register.jsp">Регистрация нового пользователя</a>

<script src="${pageContext.request.contextPath}/js/script.js"></script>

</body>
</html>