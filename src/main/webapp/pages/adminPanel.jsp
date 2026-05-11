<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="com.example.demo.util.LocalizationUtil" %>
<%@ page import="com.example.demo.util.Message" %>

<%
    String lang = LocalizationUtil.getCurrentLanguage(request);
%>

<html>
<head>
    <title><%= LocalizationUtil.getMessage(Message.ADMIN_TITLE, request) %></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        * {
            box-sizing: border-box;
        }

        body {
            background-color: #f5f5f5;
            margin: 0;
            padding: 15px;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        .admin-container {
            max-width: 1200px;
            margin: 0 auto;
            background-color: white;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            padding: 20px;
        }

        /* Кнопки языка */
        .lang-buttons {
            text-align: center;
            margin-bottom: 20px;
            display: flex;
            justify-content: center;
            gap: 10px;
            flex-wrap: wrap;
        }

        .lang-btn {
            background-color: #3498db;
            color: white;
            border: none;
            padding: 8px 20px;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            font-size: 14px;
            transition: background-color 0.3s;
        }

        .lang-btn:hover {
            background-color: #2980b9;
        }

        .active-lang {
            background-color: #2c3e50;
        }

        /* Заголовки */
        h1 {
            color: #2c3e50;
            text-align: center;
            font-size: 1.8rem;
            margin-bottom: 10px;
        }

        h2 {
            color: #2c3e50;
            border-bottom: 2px solid #3498db;
            padding-bottom: 10px;
            font-size: 1.4rem;
        }

        .welcome-text {
            text-align: center;
            font-size: 1.1rem;
            margin-bottom: 20px;
            color: #555;
        }

        /* Адаптивная таблица */
        .table-wrapper {
            overflow-x: auto;
            -webkit-overflow-scrolling: touch;
            margin: 20px 0;
            border-radius: 8px;
            border: 1px solid #ddd;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            min-width: 600px;
        }

        th, td {
            padding: 12px 10px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        th {
            background-color: #3498db;
            color: white;
            font-weight: 600;
            position: sticky;
            top: 0;
        }

        tr:hover {
            background-color: #f5f5f5;
        }

        /* Кнопка удаления */
        .delete-btn {
            background-color: #e74c3c;
            color: white;
            padding: 6px 12px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 13px;
            transition: background-color 0.3s;
        }

        .delete-btn:hover {
            background-color: #c0392b;
        }

        /* Кнопка "Назад" */
        .back-btn {
            background-color: #3498db;
            color: white;
            padding: 10px 20px;
            text-decoration: none;
            border-radius: 5px;
            display: inline-block;
            transition: background-color 0.3s;
        }

        .back-btn:hover {
            background-color: #2980b9;
        }

        /* Badge статусы */
        .badge {
            display: inline-block;
            padding: 3px 8px;
            border-radius: 12px;
            font-size: 11px;
            font-weight: bold;
        }

        .badge-confirmed {
            background-color: #2ecc71;
            color: white;
        }

        .badge-not-confirmed {
            background-color: #e74c3c;
            color: white;
        }

        .badge-admin {
            background-color: #e67e22;
            color: white;
        }

        .badge-user {
            background-color: #3498db;
            color: white;
        }

        .badge-active {
            background-color: #2ecc71;
            color: white;
        }

        .badge-inactive {
            background-color: #95a5a6;
            color: white;
        }

        /* Статистика */
        .stats {
            background-color: #ecf0f1;
            padding: 10px 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            text-align: center;
            font-size: 14px;
        }

        /* Адаптив для мобильных устройств */
        @media (max-width: 768px) {
            .admin-container {
                padding: 10px;
            }

            h1 {
                font-size: 1.5rem;
            }

            h2 {
                font-size: 1.2rem;
            }

            th, td {
                padding: 8px 6px;
                font-size: 12px;
            }

            .delete-btn {
                padding: 4px 8px;
                font-size: 11px;
            }

            .lang-btn {
                padding: 6px 15px;
                font-size: 12px;
            }

            .stats {
                font-size: 12px;
            }
        }

        @media (max-width: 480px) {
            body {
                padding: 10px;
            }

            h1 {
                font-size: 1.3rem;
            }

            th, td {
                padding: 6px 4px;
                font-size: 11px;
            }
        }

        /* Пустое состояние таблицы */
        .empty-state {
            text-align: center;
            padding: 40px;
            color: #999;
        }
    </style>
</head>
<body>

<div class="admin-container">
    <!-- Кнопки переключения языка -->
    <div class="lang-buttons">
        <a href="${pageContext.request.contextPath}/controller?command=CHANGE_LANGUAGE&lang=ru"
           class="lang-btn ${lang == 'ru' ? 'active-lang' : ''}">🇷🇺 Русский</a>
        <a href="${pageContext.request.contextPath}/controller?command=CHANGE_LANGUAGE&lang=en"
           class="lang-btn ${lang == 'en' ? 'active-lang' : ''}">🇬🇧 English</a>
    </div>

    <!-- Заголовок -->
    <h1><%= LocalizationUtil.getMessage(Message.ADMIN_TITLE, request) %></h1>
    <p class="welcome-text"><%= LocalizationUtil.getMessage(Message.WELCOME, request) %>, <c:out value="${user}"/>!</p>

    <!-- Статистика -->
    <div class="stats">
        📊 <%= LocalizationUtil.getMessage(Message.ADMIN_USER_LIST, request) %>: <strong>${users.size()}</strong>
    </div>

    <h2><%= LocalizationUtil.getMessage(Message.ADMIN_USER_MANAGEMENT, request) %></h2>

    <!-- Адаптивная таблица с горизонтальной прокруткой -->
    <div class="table-wrapper">
        <table>
            <thead>
            <tr>
                <th><%= LocalizationUtil.getMessage(Message.ADMIN_ID, request) %></th>
                <th><%= LocalizationUtil.getMessage(Message.ADMIN_LOGIN, request) %></th>
                <th><%= LocalizationUtil.getMessage(Message.ADMIN_EMAIL, request) %></th>
                <th><%= LocalizationUtil.getMessage(Message.ADMIN_CONFIRMED, request) %></th>
                <th><%= LocalizationUtil.getMessage(Message.ADMIN_ROLE, request) %></th>
                <th><%= LocalizationUtil.getMessage(Message.ADMIN_ACTIVE, request) %></th>
                <th><%= LocalizationUtil.getMessage(Message.ADMIN_ACTIONS, request) %></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="u" items="${users}">
                <tr>
                    <td><c:out value="${u.id}"/></td>
                    <td><c:out value="${u.login}"/></td>
                    <td style="max-width: 200px; word-break: break-all;">
                        <c:out value="${u.email}"/>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${u.confirmed}">
                                <span class="badge badge-confirmed">✓ Yes</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge badge-not-confirmed">✗ No</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${u.role == 'ADMIN'}">
                                <span class="badge badge-admin">ADMIN</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge badge-user">USER</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${u.active}">
                                <span class="badge badge-active">✓ Active</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge badge-inactive">✗ Inactive</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <form action="${pageContext.request.contextPath}/controller" method="post"
                              onsubmit="return confirm('<%= LocalizationUtil.getMessage(Message.ADMIN_DELETE_CONFIRM, request) %>')">
                            <input type="hidden" name="command" value="DELETE_USER"/>
                            <input type="hidden" name="userId" value="${u.id}"/>
                            <button type="submit" class="delete-btn"><%= LocalizationUtil.getMessage(Message.ADMIN_DELETE, request) %></button>
                        </form>
                    </td>
                </tr>
            </c:forEach>

            <c:if test="${empty users}">
                <tr>
                    <td colspan="7" class="empty-state">
                        📭 <%= LocalizationUtil.getMessage(Message.ADMIN_USER_LIST, request) %> <%= LocalizationUtil.getMessage(Message.ADMIN_ACTIONS, request) %>
                    </td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>

    <div style="margin-top: 30px; text-align: center;">
        <a href="${pageContext.request.contextPath}/pages/main.jsp" class="back-btn">
            ← <%= LocalizationUtil.getMessage(Message.ADMIN_BACK_TO_MAIN, request) %>
        </a>
    </div>
</div>

</body>
</html>