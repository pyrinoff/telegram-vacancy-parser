<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<html>
<%@ include file="head.jsp" %>
<body>
    <div class="flex-center position-ref full-height">
        <div class="container  col-md-4">
            <div id="infoBlock"></div>
            <form class="form-signin" method="post" action="${pageContext.request.contextPath}/auth" method="POST">
                <h2 class="form-signin-heading">LOGIN PAGE</h2>
                <label for="username" class="sr-only">Username</label>
                <input type="text" id="username" name="username" class="form-control" placeholder="Username"
                       required="" autofocus="">
                </p>
                <p>
                    <label for="password" class="sr-only">Password</label>
                    <input type="password" id="password" name="password" class="form-control" placeholder="Password"
                           required="">
                </p>
                <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
            </form>
        </div>
    </div>
</body>
</html>