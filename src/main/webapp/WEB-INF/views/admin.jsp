<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@ include file="head.jsp" %>
    <script src="/static/js/admin.js"></script>
</head>
</html>
<body>
<div class="container">
    <div class="card border-none">
        <div class="card-body">
            <form onsubmit="uploadFile(event)">
                <td><label for="file">Загрузка JSON-файла:</label></td>
                <input type="file" id="file" name="file" required />
                <button type="submit">Upload</button>
            </form>
            <%--<form method="POST" action="/admin/parser/upload" enctype="multipart/form-data">
                <table>
                    <tr>
                        <td><label for="file">Загрузка JSON-файла:</label></td>
                        <td><input type="file" name="file" id="file"/></td>
                    </tr>
                    <tr>
                        <td><input type="submit" value="Отправить в обработку"/></td>
                    </tr>
                </table>
            </form>--%>
        </div>
    </div>
    <div class="card border-none">
        <div class="card-body">
            <div class="alert-info"><c:out value="${message}"/></div>
            <div class="text-info" id="status-info"></div>
        </div>
        <div class="card-body">
            <button class="btn btn-primary" onclick="checkStatus();">Проверить статус обработки</button>
            <button class="btn btn-primary" onclick="clear();">Удалить всё из базы</button>
        </div>
        <div class="card-body">
            <button class="btn btn-primary" onclick="switchMaintenance(true);">Включить режим "Ведутся работы"</button>
            <button class="btn btn-primary" onclick="switchMaintenance(false);">Выключить режим "Ведутся работы"</button>
        </div>
    </div>
    <div class="card border-none">
        <div class="card-body">
            <a href="/">На главную</a>
        </div>
    </div>
</div>
</body>
<%@ include file="metrika.jsp" %>
</html>