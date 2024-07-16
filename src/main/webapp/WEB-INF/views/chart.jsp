<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<html>
<%@ include file="head.jsp" %>
<body>
<div class="container">
    <%@ include file="headInfo.jsp" %>
    <%@ include file="chartLineInfo.jsp" %>
    <%@ include file="quickCalc.jsp" %>
    <%@ include file="charts.jsp" %>
    <%@ include file="lineSettingsStatic.jsp" %>
    <jsp:include page="lineSettings.jsp"/>
</div>
<%@ include file="metrika.jsp" %>
</body>
</html>
