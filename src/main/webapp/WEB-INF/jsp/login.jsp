<%--
  Created by IntelliJ IDEA.
  User: ds
  Date: 2018-03-12
  Time: 오후 1:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
</head>
<body>
    <form method="post" action="/login-processing">
        <label>
            <input type="text" class="form-control" placeholder="id" name="id" autofocus="autofocus">
        </label>
        <label>
            <input type="password" class="form-control" placeholder="password" name="pw">
        </label>
        <button type="submit">Login</button>
    </form>
</body>
</html>
