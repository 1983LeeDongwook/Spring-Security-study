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
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
    <title>Title</title>
</head>
<body>

<h1>현재 로그인한 사용자 : ${user.name}</h1>
<a class="btn btn-default" href="logout" role="button" style="text-align: center">Logout</a>

</body>
</html>
