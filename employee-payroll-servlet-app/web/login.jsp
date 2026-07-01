<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>

<head>

    <title>Login</title>

    <style>

        body{

            font-family:Arial;
            background:#f5f5f5;

        }

        .container{

            width:350px;
            margin:80px auto;
            background:white;
            padding:30px;
            border-radius:10px;
            box-shadow:0 0 10px gray;

        }

        input{

            width:100%;
            padding:10px;
            margin:10px 0;

        }

        button{

            width:100%;
            padding:10px;
            background:#1976D2;
            color:white;
            border:none;

        }

        .error{

            color:red;

        }

    </style>

</head>

<body>

<div class="container">

    <h2 align="center">Employee Payroll Login</h2>

    <c:if test="${not empty errorMessage}">

        <p class="error">${errorMessage}</p>

    </c:if>

    <form action="login" method="post">

        <input
                type="text"
                name="username"
                placeholder="Username"
                required>

        <input
                type="password"
                name="password"
                placeholder="Password"
                required>

        <button type="submit">

            Login

        </button>

    </form>

    <br>

    <a href="register">

        Create New Account

    </a>

</div>

</body>

</html>