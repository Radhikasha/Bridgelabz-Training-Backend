<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>

<html>

<head>

    <title>Register</title>

    <style>

        body{

            font-family:Arial;
            background:#f4f4f4;

        }

        .container{

            width:400px;
            margin:50px auto;
            background:white;
            padding:20px;
            border-radius:10px;
            box-shadow:0px 0px 10px gray;

        }

        input,select{

            width:100%;
            padding:10px;
            margin-top:10px;

        }

        button{

            width:100%;
            padding:10px;
            margin-top:15px;
            background:green;
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

    <h2 align="center">

        User Registration

    </h2>

    <c:if test="${not empty errorMessage}">

        <p class="error">

                ${errorMessage}

        </p>

    </c:if>

    <form action="register" method="post">

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

        <input
                type="email"
                name="email"
                placeholder="Email"
                required>

        <button>

            Register

        </button>

    </form>

    <br>

    <a href="login">

        Already Registered?

        Login

    </a>

</div>

</body>

</html>